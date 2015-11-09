package app.wiplay.ui;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import app.wiplay.com.wiplay.R;

public class MainActivity extends Activity {

    private Button connect = null;
    private Button browse = null;
    private Button startSharing = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button)findViewById(R.id.connect);
        browse = (Button)findViewById(R.id.browse);
        startSharing = (Button)findViewById(R.id.startServer);

        /* connect ClickHandlers */
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Connect clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /* browse clickHandlers */
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Browse clicked", Toast.LENGTH_SHORT).show();
            }
        });

        /* startSharing clickHandlers */

        startSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start Sharing clicked", Toast.LENGTH_SHORT).show();
                try
                {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE, PRODUCT_MODE");
                    startActivityForResult(intent, 0);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if(requestCode == 0)
            if(resultCode == RESULT_OK)
            {
                Toast.makeText(getApplicationContext(), intent.getStringExtra("SCAN_RESULT_FORMAT"), Toast.LENGTH_SHORT).show();
                Toast.makeText(getApplicationContext(), intent.getStringExtra("SCAN_RESULT"), Toast.LENGTH_SHORT).show();
            }
    }
}
