package android.com.reznikalexey.test;

import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.model.LoadNewsFeedTask;
import android.com.reznikalexey.ui.activities.NewsFeedActivity;
import android.com.reznikalexey.utils.BitmapManager;
import android.graphics.Bitmap;
import android.test.ActivityInstrumentationTestCase2;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class NetworkFunctionsTest
        extends ActivityInstrumentationTestCase2<NewsFeedActivity> {

    public NetworkFunctionsTest() {
        super(NewsFeedActivity.class);
    }

    public void testSourcesAvailability() {
        String[] sources = getActivity().getResources().getStringArray(R.array.array_sources_urls);

        for (String source : sources) {
            try {
                URL url = new URL(source);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                //To avoid being redirected to mobile web-page set user-agent property to desktop browser
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36");
                httpURLConnection.connect();
                int responseCode = httpURLConnection.getResponseCode();

                assertTrue(responseCode == HttpURLConnection.HTTP_OK);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void testFeedLoader() {
        String[] sources = getActivity().getResources().getStringArray(R.array.array_sources_urls);
        ArrayList<ArticleEntry> entries = new LoadNewsFeedTask(getActivity(), getActivity()).loadArticleEntries(sources);

        assertTrue(entries.size() != 0);
    }

    public void testBitmapLoader() {
        String TEST_URL = "http://lorempixel.com/800/600/";
        int WIDTH = 800;
        int HEIGHT = 600;
        Bitmap bitmap = BitmapManager.getInstance().downloadBitmap(TEST_URL, 800, 600);

        assertTrue(bitmap != null);
        assertTrue(bitmap.getWidth() == WIDTH);
        assertTrue(bitmap.getHeight() == HEIGHT);
    }
}