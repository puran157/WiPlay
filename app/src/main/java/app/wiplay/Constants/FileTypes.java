package app.wiplay.constants;

import android.content.Context;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

/**
 * Created by pchand on 11/10/2015.
 */
public class FileTypes implements FileFilter{

    private Context context;
    public FileTypes(Context c)
    {
        context = c;
    }
    @Override
    public boolean accept(File pathname) {
        if(Arrays.asList(Constants.FILE_TYPE).contains(getExtension(pathname)))
            return true;
        else
            return false;
    }

    private String getExtension(File file)
    {
        String ext = null;
        String s = file.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        Toast.makeText(context, ext, Toast.LENGTH_SHORT).show();
        return ext;
    }
}
