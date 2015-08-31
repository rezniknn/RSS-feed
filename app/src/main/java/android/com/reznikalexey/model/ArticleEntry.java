package android.com.reznikalexey.model;

import android.com.reznikalexey.ui.adapters.ArticlesAdapter;
import android.com.reznikalexey.utils.BitmapUtils;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class ArticleEntry {
    public static final String LOG_TAG = "ArticleEntry";

    private ArticleSource source;
    private String title;
    private String description;
    private String imageUrl;
    private Bitmap imageBitmap;

    private ArticlesAdapter adapter;

    public enum ArticleSource {
        LENTA ("lenta.ru"),
        GAZETA ("gazeta.ru");

        private final String textDescription;

        private ArticleSource(String s) {
            textDescription = s;
        }

        public String toString() {
            return this.textDescription;
        }
    }

    public ArticleEntry(ArticleSource source, String title, String description, String imageUrl) {
        this.source = source;
        this.title = title;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public ArticleSource getSource() {
        return source;
    }

    public void setSource(ArticleSource source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }

    public void setImageBitmap(Bitmap imageBitmap) {
        this.imageBitmap = imageBitmap;
    }

    public ArticlesAdapter getAdapter() {
        return adapter;
    }

    public void setAdapter(ArticlesAdapter adapter) {
        this.adapter = adapter;
    }

    public void loadImage(ArticlesAdapter adapter) {
        this.adapter = adapter;
        if (imageUrl != null && !imageUrl.equals("")) {
            new ImageLoadTask().execute(imageUrl);
        }
    }

    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.d(LOG_TAG, "Loading image...");
        }

        protected Bitmap doInBackground(String... param) {
            Log.d(LOG_TAG, "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = BitmapUtils.loadBitmapFromUrl(param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {

        }

        protected void onPostExecute(Bitmap image) {
            if (image != null) {
                Log.d(LOG_TAG, "Successfully loaded image for " + title);
                imageBitmap = image;
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            } else {
                Log.e(LOG_TAG, "Failed to load image for " + title);
            }
        }
    }
}
