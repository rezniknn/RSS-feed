package android.com.reznikalexey.model;

import android.com.reznikalexey.listeners.NewsFeedLoadedListener;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public class LoadNewsFeedTask extends AsyncTask<String[], Void, ArrayList<ArticleEntry>>{
    public static final String LOG_TAG = "LoadNewsFeedTask";

    ArrayList<ArticleEntry> articleEntries;
    NewsFeedLoadedListener listener;

    public LoadNewsFeedTask(NewsFeedLoadedListener listener) {
        this.listener = listener;
        articleEntries = new ArrayList<ArticleEntry>();
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Log.d(LOG_TAG, "Trying to load RSS feed entries");
    }

    @Override
    protected ArrayList<ArticleEntry> doInBackground(String[]... params) {

        for (String source : params[0]) {
            try {
                URL url = new URL(source);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                //To avoid being redirected to mobile web-page
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
                httpURLConnection.connect();

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();

                    //TODO think of something better
                    if (source.contains("gazeta")) {
                        articleEntries.addAll(ArticleXMLParser.parse(inputStream, "windows-1251"));
                    } else {
                        articleEntries.addAll(ArticleXMLParser.parse(inputStream, "UTF-8"));
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return articleEntries;
    }

    @Override
    protected void onPostExecute(ArrayList<ArticleEntry> articleEntries) {
        Log.d(LOG_TAG, "Finished loading RSS entries. N of entries loaded: " + this.articleEntries.size());
        listener.onFeedLoaded(articleEntries);
        super.onPostExecute(articleEntries);
    }
}
