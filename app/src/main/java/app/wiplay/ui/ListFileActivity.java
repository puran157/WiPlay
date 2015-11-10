package app.wiplay.ui;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.wiplay.com.wiplay.R;
import app.wiplay.constants.Constants;
//import app.wiplay.constants.FileTypes;

public class ListFileActivity extends ListActivity {

    private String path;
    //private FileTypes types;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_file);

        // Use the current directory as title
        path = Environment.getExternalStorageDirectory().getAbsolutePath();
        //types = new FileTypes(getApplicationContext());
        Toast.makeText(getApplicationContext(), path, Toast.LENGTH_SHORT).show();
        if (getIntent().hasExtra("path")) {
            path = getIntent().getStringExtra("path");
        }
        setTitle(path);

        // Read all supported files types sorted into the values-array
        List values = new ArrayList();
        File dir = new File(path);
        if (!dir.canRead()) {
            setTitle(getTitle() + " (inaccessible)");
        }

        //File[] list = dir.listFiles(types);
        String[] list = dir.list();
        if (list != null) {
            //for(int i = 0; i < list.length; ++i)
            for(String file: list)
            {
                if(!file.equals("."))
                    values.add(file);
            }
        }
        Collections.sort(values);

        // Put the data into the list
        ArrayAdapter adapter = new ArrayAdapter(this,
                android.R.layout.simple_list_item_2, android.R.id.text1, values);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String filename = (String) getListAdapter().getItem(position);
        if (path.endsWith(File.separator)) {
            filename = path + filename;
        } else {
            filename = path + File.separator + filename;
        }
        if (new File(filename).isDirectory()) {
            Intent intent = new Intent(this, ListFileActivity.class);
            intent.putExtra("path", filename);
            startActivity(intent);
            finish();
        } else {
           Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra(Constants.action, Constants.ACTION[0]);
           intent.putExtra("file", filename);
           setResult(RESULT_OK, intent);
           finish();
        }
    }

}
