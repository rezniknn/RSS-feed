package android.com.reznikalexey.ui.activities;

import android.app.Activity;
import android.com.reznikalexey.R;
import android.com.reznikalexey.listeners.NewsFeedLoadedListener;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.model.LoadNewsFeedTask;
import android.com.reznikalexey.ui.adapters.ArticlesAdapter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

public class NewsFeedActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener, NewsFeedLoadedListener {
    private SwipeRefreshLayout srlContainer;
    private ListView lvContainer;

    public static final String LOG_TAG = "NewsFeedActivity";
    String[] newsSources;
    private ArticlesAdapter adapter;

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
        adapter = new ArticlesAdapter(this, R.layout.article_entry_layout);
        lvContainer = (ListView) findViewById(R.id.lv_container);
        lvContainer.setAdapter(adapter);
        lvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Switch between detailed/non-detailed view for the entry
                adapter.getItem(position).switchDetailedView();

                //Notify adapter that views need to be updated
                adapter.notifyDataSetChanged();
            }
        });
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
     * @param articleEntries ArrayList containing news articles from all sources
     */
    @Override
    public void onFeedLoaded(ArrayList<ArticleEntry> articleEntries) {
        if (adapter != null) {
            adapter.clear();
            for (ArticleEntry entry : articleEntries) {
                adapter.add(entry);
                //Initiate image download if imageUrl is present for each news article
                entry.loadImage(adapter);
            }
            adapter.notifyDataSetChanged();
        } else {
            Log.e(LOG_TAG, "Adapter is null");
        }
    }
}
