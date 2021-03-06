package app.wiplay.qr;

/**
 * Created by pchand on 11/16/2015.
 */
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import app.wiplay.com.wiplay.R;

/**
 */
public class CaptureHandler extends Handler {
    public static final String DECODED_DATA = "decoded_data";
    private CameraManager cameraManager;
    private Context context;
    private OnDecodedCallback callback;

    public CaptureHandler(CameraManager cameraManager, Context context, OnDecodedCallback callback) {
        this.cameraManager = cameraManager;
        this.context = context;
        this.callback = callback;
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case R.id.decoded:
                String data = msg.getData().getString(DECODED_DATA);
                if (callback != null){
                    callback.onDecoded(data);
                }
                break;
            case R.id.decode_failed:
                //getting new frame
                cameraManager.requestNextFrame(new PreviewCallback(this, cameraManager));
                break;
        }
    }

    public static interface OnDecodedCallback {
        void onDecoded(String decodedData);
    }
}