package android.com.reznikalexey.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class BitmapUtils {
    public static final String LOG_TAG = "ButmapUtils";

    public static Bitmap loadBitmapFromUrl(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to load Bitmap from url: " + src);
            return null;
        }
    }
}
