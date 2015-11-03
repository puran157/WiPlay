package app.wiplay.filemanager;

import java.io.BufferedReader;
import java.io.File;
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

    FileManager(String path)
    {
        file_path = path;
    }

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

    public byte[] SendChunk(int bytesRead)
    {
        char[] data = new char[Constants.BUFFER_SIZE];
        try {
            bytesRead = reader.read(data, startOffset, Constants.BUFFER_SIZE);
        }
        catch(IOException e)
        {

        }
        startOffset+= bytesRead;
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
