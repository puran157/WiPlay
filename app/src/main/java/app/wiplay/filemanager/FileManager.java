package app.wiplay.filemanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import app.wiplay.constants.Constants;

/**
 * Created by pchand on 11/2/2015.
 */
public class FileManager {

    private  String file_path;
    private  File file;
    private  BufferedReader reader;
    private  BufferedWriter writer;
    private  int startOffset;

    public FileManager(String path)
    {
        file_path = path;
    }

    public int GetOffset()
    {
        return startOffset;
    }
    public void InitialiseReader()
    {
        file = new File(file_path);
        startOffset = 0;
        try{

            reader = new BufferedReader(new FileReader(file));

        }catch (IOException e)
        {

        }
    }

    public void InitialiseWriter()
    {
        file = new File(file_path);
        startOffset = 0;
        try{

            writer = new BufferedWriter(new FileWriter(file));

        }catch (IOException e)
        {

        }
    }

    public byte[] GetChunk(int bytesRead)
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

    public void WriteChunk(byte[] data, int offSet)
    {
       String data_ = new String(data);
        try {
            writer.write(data_.toCharArray(), offSet, data_.length());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void DeInit()
    {
        try {
            if(reader != null)
                reader.close();
            if(writer != null)
                writer.close();
        }catch (IOException e)
        {

        }
        file = null;
        reader = null;
        writer = null;
        startOffset = 0;
    }

}
