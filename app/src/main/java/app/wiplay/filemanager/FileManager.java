package app.wiplay.filemanager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 11/2/2015.
 */
public class FileManager {

    private  String file_path;
    private  File file;
    private  BufferedReader reader;
    private  int startOffset;

    public  void Initialise()
    {
        file = new File(file_path);
        startOffset = 0;
        try{

            reader = new BufferedReader(new FileReader(file));

        }catch (IOException e)
        {

        }
    }

    public byte[] SendChunk()
    {
        char[] data = new char[Constants.BUFFER_SIZE];
        try {
            reader.read(data, startOffset, Constants.BUFFER_SIZE);
        }
        catch(IOException e)
        {

        }
        startOffset+= Constants.BUFFER_SIZE;
        return new String(data).getBytes();
    }

    public void DeInit()
    {
        try {
            reader.close();
        }catch (IOException e)
        {

        }
        startOffset = 0;
    }

}
