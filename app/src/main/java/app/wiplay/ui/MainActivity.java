package app.wiplay.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import app.wiplay.com.wiplay.R;
import app.wiplay.connection.WiPlayClient;
import app.wiplay.connection.WiPlayHotSpot;
import app.wiplay.connection.WiPlayServer;
import app.wiplay.constants.Constants;

import app.wiplay.framework.WiPlayFramework;
import app.wiplay.qr.QRWrapper;


public class MainActivity extends Activity {

    private Button connect = null;
    private Button browse = null;
    private Button startSharing = null;
    private TextView file = null;

    private String file_path = null;
    private String qr_data = null;

    private Button cancel = null;
    private Button play = null;
    private ImageView imageView = null;
    private WiPlayHotSpot hotspot = null;

    private String host_name = null;
    private String hotspot_name = null;
    private String hotspot_psk = null;

    WiPlayFramework framework = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button)findViewById(R.id.connect);
        browse = (Button)findViewById(R.id.browse);
        startSharing = (Button)findViewById(R.id.startServer);
        file = (TextView)findViewById(R.id.file_path);

        /* Init hotspot & framework */
        hotspot = new WiPlayHotSpot(getApplicationContext());
        framework = new WiPlayFramework(getApplicationContext());

        /* connect ClickHandlers */
        connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CaptureActivity.class);
                startActivityForResult(intent, Constants.SCAN);
            }
        });

        /* browse clickHandlers */
        browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(getApplicationContext(), ListFileActivity.class);
                    startActivityForResult(intent, Constants.BROWSE);
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
                setContentView(R.layout.qr_code);
                play = (Button)findViewById(R.id.play);
                cancel = (Button)findViewById(R.id.cancel);
                imageView = (ImageView)findViewById(R.id.imageView);
                StartServer();

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        setContentView(R.layout.activity_main);
                        cleanUp();
                    }
                });

            }
        });
    }

    public void StartServer() throws IOException {


        /* start the hotspot */
        hotspot.StartHotSpot();
        WiPlayServer server = new WiPlayServer(getApplication().getApplicationContext());
        server.Init();
        /* Generate QR Code */
        String data = "";
        data += "HOTSPOT:"+hotspot.getHotspot_name() + "\n";
        data += "PSK:" + hotspot.getHotspot_psk() + "\n";
        data += "HOST:" + server.getWifiApIpAddress() + "\n";
        QRWrapper.CreateQR(data, imageView);
    }


    public void StartClient() throws IOException {
        /* Get Server Details from QR
         * Connect to server
         * Start receving the file
         * Start Playing the file */

        QRWrapper.ScanQR(host_name, hotspot_name, hotspot_psk, qr_data);
        hotspot.setHotspot(hotspot_name, hotspot_psk);
        hotspot.ConnectToHotSpot();
        WiPlayClient client  = new WiPlayClient(host_name);
        client.Init();
    }

    public void cleanUp()
    {
        Thread cleanUp = new Thread(new Runnable() {
            @Override
            public void run() {
                if(hotspot != null)
                {
                    hotspot.cleanUp();
                    hotspot = null;
                }

                if(framework != null)
                {
                    framework.cleanUp();
                    framework = null;
                }
            }
        });

        cleanUp.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //TODO: Find a way to do cleanup here
        //cleanUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cleanUp();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Constants.BROWSE)
            if(resultCode == RESULT_OK) {
                file_path = data.getStringExtra("path");
                file.setText(file_path);
                startSharing.setEnabled(true);
            }
            else
                Toast.makeText(getApplicationContext(), "No File Selected", Toast.LENGTH_SHORT).show();
        else if(requestCode == Constants.SCAN)
            if(resultCode == RESULT_OK) {
                qr_data = data.getStringExtra("qr_data");
                StartClient();
            }
            else
                Toast.makeText(getApplicationContext(), "No Data captured", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(getApplicationContext(), "This is impossible", Toast.LENGTH_SHORT).show();

    }
}
