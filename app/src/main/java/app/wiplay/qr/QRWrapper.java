package app.wiplay.qr;

/**
 * Created by pchand on 11/10/2015.
 */

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.qrcode.QRCodeWriter;

import app.wiplay.constants.Constants;

public class QRWrapper {

    public static void CreateQR( String data,  ImageView image){
                Writer writer = new QRCodeWriter();
                BitMatrix bm = null;
                try {
                    bm = writer.encode(data, BarcodeFormat.QR_CODE, 150, 150);
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                Bitmap imageBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

                for (int i = 0; i < 150; ++i)
                    for (int j = 0; j < 150; ++j)
                        imageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);

                if (imageBitmap != null) {
                    image.setImageBitmap(imageBitmap);
                } else
                    Log.i(Constants.Tag, "QR Code Creation Error");
    }

    public static void ScanQR(String host, String hotspot, String psk, String content)
    {
        /* Lets parse the information here */
        String data[] = content.split("\n");
        host = data[0].substring(data[0].indexOf(":"));
        hotspot = data[1].substring(data[0].indexOf(":"));
        psk = data[2].substring(data[0].indexOf(":"));
    }
}
