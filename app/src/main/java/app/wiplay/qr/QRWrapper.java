package app.wiplay.qr;

/**
 * Created by pchand on 11/10/2015.
 */

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import app.wiplay.constants.Constants;

public class QRWrapper {

    public static void CreateQR(String data, ImageView image) throws WriterException{
        Writer writer = new QRCodeWriter();
        String finalData = Uri.encode(data, "utf-8");
        BitMatrix bm = writer.encode(finalData, BarcodeFormat.QR_CODE, 150, 150);
        Bitmap imageBitmap = Bitmap.createBitmap(150, 150, Bitmap.Config.ARGB_8888);

        for(int i = 0; i < 150; ++i)
            for(int j = 0; j < 150; ++j)
                imageBitmap.setPixel(i, j, bm.get(i, j) ? Color.BLACK : Color.WHITE);

        if(imageBitmap != null)
        {
            image.setImageBitmap(imageBitmap);
        }
        else
            Log.i(Constants.Tag, "QR Code Creation Error");
    }
}
