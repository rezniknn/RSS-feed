package android.com.reznikalexey.ui.activities;

import android.app.Activity;
import android.com.reznikalexey.R;
import android.com.reznikalexey.listeners.NewsFeedLoadedListener;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.model.LoadNewsFeedTask;
import android.com.reznikalexey.ui.adapters.ArticlesAdapter;
import android.com.reznikalexey.utils.BitmapManager;
import android.com.reznikalexey.utils.Const;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFeedActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, NewsFeedLoadedListener {
    private SwipeRefreshLayout srlContainer;
    private ListView lvContainer;

    public static final String LOG_TAG = "NewsFeedActivity";
    String[] newsSources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initViews();

        //Get the list of URLs from res file
        newsSources = getResources().getStringArray(R.array.array_sources_urls);

        //Refresh News Feed upon app launch
        onRefresh();
    }

    /***
     * Initialise UI elements, set callback listeners
     */
    private void initViews() {
        srlContainer = (SwipeRefreshLayout) findViewById(R.id.srl_main_container);
        srlContainer.setOnRefreshListener(this);
        lvContainer = (ListView) findViewById(R.id.lv_container);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_news_feed, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /***
     * Initialise RSS feed download Task
     */
    @Override
    public void onRefresh() {
        new LoadNewsFeedTask(this).execute(newsSources);
        srlContainer.setRefreshing(false);
    }

    /***
     * The callback is triggered when all the articles from all sources have been loaded, parsed and sorted
     *
     * @param articleEntries ArrayList containing news articles from all sources
     */
    @Override
    public void onFeedLoaded(ArrayList<ArticleEntry> articleEntries) {
        ArticlesAdapter adapter = new ArticlesAdapter(this, R.layout.article_entry_layout, articleEntries);
        lvContainer.setAdapter(adapter);
    }
}
