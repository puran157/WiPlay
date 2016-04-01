package app.wiplay.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.widget.MediaController;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.VideoView;

import app.wiplay.com.wiplay.R;
import app.wiplay.connection.WiPlaySocket;
import app.wiplay.constants.Constants;

public class FilePlay extends Activity {

    private VideoView video = null;
    private Uri vidUri = null;
    private ProgressDialog dialog = null;
    private MediaController.MediaPlayerControl player;
    private WiPlaySocket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_play);
        String path = savedInstanceState.getString("path");
        video = (VideoView)findViewById(R.id.video);


        dialog = new ProgressDialog(FilePlay.this);
        dialog.setTitle("WiPlay");
        dialog.setMessage("Getting file...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(false);
        dialog.show();


        player = new MediaController.MediaPlayerControl() {
            @Override
            public void start() {
                video.start();

                /* Broadcast Play Event */
                //TODO: extend this to all client
                int time = getCurrentPosition();

                /*if(socket.getCallbackMaster() != null)
                    socket.getCallbackMaster().PlayFile(time);*/
            }

            @Override
            public void pause() {
                video.pause();
                /* Broadcast Pause Event */
                //TODO: Extend this functionality to client also
                int time = getCurrentPosition();

                /*if(socket.getCallbackMaster() != null)
                    socket.getCallbackMaster().Pause(time);*/
            }

            @Override
            public int getDuration() {
                return video.getDuration();
            }

            @Override
            public int getCurrentPosition() {
                return video.getCurrentPosition();
            }

            @Override
            public void seekTo(int pos) {
                video.seekTo(pos);
            }

            @Override
            public boolean isPlaying() {
                return video.isPlaying();
            }

            @Override
            public int getBufferPercentage() {
                return video.getBufferPercentage();
            }

            @Override
            public boolean canPause() {
                return video.canPause();
            }

            @Override
            public boolean canSeekBackward() {
                return video.canSeekBackward();
            }

            @Override
            public boolean canSeekForward() {
                return video.canSeekForward();
            }

            @Override
            public int getAudioSessionId() {
                return video.getAudioSessionId();
            }
        };

        try {
            MediaController mController = new MediaController(FilePlay.this);
            mController.setAnchorView(video);
            mController.setPrevNextListeners(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Do nothing */
                }
            }, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Do nothing */
                }
            });

            mController.setMediaPlayer(player);
            vidUri = Uri.parse(path);
            video.setMediaController(mController);
            video.setVideoURI(vidUri);
        }
        catch (Exception e)
        {
            Log.e(Constants.Tag, e.getMessage());
            e.printStackTrace();
        }

        video.requestFocus();
        video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                dialog.dismiss();
                video.pause();
                video.setKeepScreenOn(true);
            }
        });
    }

    public void PlayVideo(int time)
    {
        if(!video.isPlaying()) {
            video.start();
        }

        if(video.getCurrentPosition() != time)
            video.seekTo(time);
    }

    public void PauseVideo(int time)
    {
        if(video.isPlaying()) {
            video.pause();
        }

        if(video.getCurrentPosition() != time)
            video.seekTo(time);

    }

    public void  StopVideo()
    {
        video.stopPlayback();
    }
}
