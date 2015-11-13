package app.wiplay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import app.wiplay.com.wiplay.R;
import app.wiplay.connection.WiPlayHotSpot;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.qr.QRWrapper;


public class MainActivity extends Activity {

    private Button connect = null;
    private Button browse = null;
    private Button startSharing = null;
    private TextView file = null;

    private static String file_path = null;

    private Button cancel = null;
    private Button play = null;
    private ImageView imageView = null;
    WiPlayHotSpot hotspot = null;
    WiPlayServer server = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button)findViewById(R.id.connect);
        browse = (Button)findViewById(R.id.browse);
        startSharing = (Button)findViewById(R.id.startServer);
        file = (TextView)findViewById(R.id.file_path);

        if(file_path != null) {
            file.setText(file_path);
            startSharing.setEnabled(true);
        }

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
                try {
                    Intent intent = new Intent(getApplicationContext(), ListFileActivity.class);
                    startActivityForResult(intent, 0);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

        /* startSharing clickHandlers */

        startSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Start Sharing clicked", Toast.LENGTH_SHORT).show();

                /* start the hotspot */
                hotspot = new WiPlayHotSpot(getApplicationContext());
                hotspot.StartHotSpot();

                /* start the control & data server */
                server = new WiPlayServer(file_path);
                server.GoLive();

                setContentView(R.layout.qr_code);
                play = (Button)findViewById(R.id.play);
                cancel = (Button)findViewById(R.id.cancel);
                imageView = (ImageView)findViewById(R.id.imageView);

                 /* Generate QR Code */
                String data = "";
                data += "HOTSPOT:"+hotspot.getHotspot_name() + "\n";
                data += "PSK:" + hotspot.getHotspot_psk() + "\n";
                data += "HOST:" + server.gethostName() + "\n";
                QRWrapper.CreateQR(data, imageView);
            }
        });
    }

    public static void setFile_path(String path)
    {
        file_path = path;
    }

    public void cleanUp()
    {
        if(hotspot != null)
        {
            hotspot.cleanUp();
            hotspot = null;
        }

        if(server != null)
        {
            server.cleanUp();
            server = null;
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanUp();
    }
}
