package android.com.reznikalexey.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class BitmapManager {
    private static BitmapManager instance = null;

    private final Map<String, SoftReference<Bitmap>> cache;
    private final ExecutorService pool;
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());
    private Bitmap placeholder;

    public static BitmapManager getInstance() {
        if (instance == null) {
            instance = new BitmapManager();
        }
        return instance;
    }

    private BitmapManager() {
        cache = new HashMap<String, SoftReference<Bitmap>>();
        pool = Executors.newFixedThreadPool(Const.NUM_OF_THREADS);
    }

    public void setPlaceholder(Bitmap bmp) {
        placeholder = bmp;
    }

    public Bitmap getBitmapFromCache(String url) {
        if (cache.containsKey(url)) {
            return cache.get(url).get();
        }

        return null;
    }

    public void queueJob(final String url, final ImageView imageView,
                         final int width, final int height) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                String tag = imageViews.get(imageView);
                if (tag != null && tag.equals(url)) {
                    if (msg.obj != null) {
                        imageView.setImageBitmap((Bitmap) msg.obj);
                    } else {
                        imageView.setImageBitmap(placeholder);
                    }
                }
            }
        };

        pool.submit(new Runnable() {
            @Override
            public void run() {
                final Bitmap bmp = downloadBitmap(url, width, height);
                Message message = Message.obtain();
                message.obj = bmp;
                handler.sendMessage(message);
            }
        });
    }

    public void loadBitmap(final String url, final ImageView imageView,
                           final int width, final int height) {
        imageViews.put(imageView, url);
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageBitmap(placeholder);
            queueJob(url, imageView, width, height);
        }
    }

    private Bitmap downloadBitmap(String url, int width, int height) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                    url).getContent());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
            cache.put(url, new SoftReference<Bitmap>(bitmap));
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
