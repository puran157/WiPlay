package app.wiplay.ui;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

import app.wiplay.com.wiplay.R;

public class FilePlay extends Activity {

    private VideoView video = null;
    private Uri vidUri = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_play);
        String path = savedInstanceState.getString("path");


        video = (VideoView)findViewById(R.id.video);
        vidUri = Uri.parse(path);
        video.setVideoURI(vidUri);


        video.start();
    }
}
