package android.com.reznikalexey.model;

import android.com.reznikalexey.listeners.NewsFeedLoadedListener;
import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class LoadNewsFeedTask extends AsyncTask<String[], Void, ArrayList<ArticleEntry>>{
    public static final String LOG_TAG = "LoadNewsFeedTask";

    ArrayList<ArticleEntry> articleEntries;
    NewsFeedLoadedListener listner;

    public LoadNewsFeedTask(NewsFeedLoadedListener listener) {
        this.listner = listener;
        articleEntries = new ArrayList<ArticleEntry>();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d(LOG_TAG, "Trying to load RSS feed entries");
    }

    @Override
    protected ArrayList<ArticleEntry> doInBackground(String[]... params) {
        //TODO this is temporary
        for (int i = 0; i < 5; i++) {
            ArticleEntry dummyEntry = new ArticleEntry(
                    ArticleEntry.ArticleSource.LENTA,
                    "Dummy article", "Dummy description",
                    "http://lorempixel.com/1028/720/");
            articleEntries.add(dummyEntry);
        }
        return articleEntries;
    }

    @Override
    protected void onPostExecute(ArrayList<ArticleEntry> articleEntries) {
        Log.d(LOG_TAG, "Finished loading RSS entries. N of entries loaded: " + this.articleEntries.size());
        listner.onFeedLoaded(articleEntries);
        super.onPostExecute(articleEntries);
    }
}
