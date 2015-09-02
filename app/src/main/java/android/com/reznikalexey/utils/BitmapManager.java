package android.com.reznikalexey.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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

    //Cache to store loaded images
    private final Map<String, SoftReference<Bitmap>> cache;

    //Tread pool
    private final ExecutorService pool;

    //Collection of URLs and ImageView references
    private Map<ImageView, String> imageViews = Collections
            .synchronizedMap(new WeakHashMap<ImageView, String>());

    //Placeholder BitMap to be displayed whil the image is loading
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

    public void queueJob(final String url, final ImageView imageView, final ProgressBar pbProgress,
                         final int width, final int height) {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                //Make sure the image corresponds to the right URL and right ImageView
                String tag = imageViews.get(imageView);
                if (tag != null && tag.equals(url)) {
                    if (msg.obj != null) {
                        //Update ImageView. Remove ProgressBar
                        imageView.setImageBitmap((Bitmap) msg.obj);
                        pbProgress.setVisibility(View.GONE);
                    } else {
                        //This should not happen, we got the wrong image. Display placeholder and ProgressBar
                        imageView.setImageBitmap(placeholder);
                        pbProgress.setVisibility(View.VISIBLE);
                    }
                }
            }
        };

        pool.submit(new Runnable() {
            @Override
            public void run() {
                //Download image in a new Thread
                final Bitmap bmp = downloadBitmap(url, width, height);
                Message message = Message.obtain();
                message.obj = bmp;

                //Notify UI thread that image has been downloaded
                handler.sendMessage(message);
            }
        });
    }

    public void loadBitmap(final String url, final ImageView imageView, ProgressBar pbProgress,
                           final int width, final int height) {
        //Save image view and url in the Map
        imageViews.put(imageView, url);

        //Try to get bitmap from cache in case it has already been downloaded
        Bitmap bitmap = getBitmapFromCache(url);

        if (bitmap != null) {
            //Found image in cache. Update ImageView and remove ProgressBar
            imageView.setImageBitmap(bitmap);
            pbProgress.setVisibility(View.GONE);
        } else {
            //Image is not in cache. Set placeholder while the image is loading and display ProgressBar
            imageView.setImageBitmap(placeholder);
            pbProgress.setVisibility(View.VISIBLE);

            //Create and queue a new job for image download.
            queueJob(url, imageView, pbProgress, width, height);
        }
    }

    //Download Bitmap from URL and scale it
    public Bitmap downloadBitmap(String url, int width, int height) {
        try {
            Bitmap bitmap = BitmapFactory.decodeStream((InputStream) new URL(
                    url).getContent());
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);

            //Store BitMap in cache
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
