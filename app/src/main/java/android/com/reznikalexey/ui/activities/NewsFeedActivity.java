package android.com.reznikalexey.ui.activities;

import android.app.ProgressDialog;
import android.com.reznikalexey.R;
import android.com.reznikalexey.listeners.NewsFeedLoadedListener;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.model.LoadNewsFeedTask;
import android.com.reznikalexey.ui.adapters.ArticlesAdapter;
import android.com.reznikalexey.utils.Const;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.ViewConfiguration;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;

@ContentView(R.layout.activity_news_feed)
public class NewsFeedActivity extends RoboActivity implements SwipeRefreshLayout.OnRefreshListener, NewsFeedLoadedListener {
    @InjectView(R.id.srl_main_container)
    private SwipeRefreshLayout srlContainer;
    @InjectView(R.id.lv_container)
    private ListView lvContainer;

    @InjectResource(R.array.array_sources_urls)
    String[] newsSources;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();

        //Refresh News Feed upon app launch
        onRefresh();
    }

    /***
     * Configure UI elements
     */
    private void initViews() {
        //Set refresh listener for SwipeRefreshLayout
        srlContainer.setOnRefreshListener(this);

        //Reduce scrolling speed to avoid concurrency issues with ListView on slow networks
        lvContainer.setFriction(ViewConfiguration.getScrollFriction() * Const.FRICTION_SCALE_FACTOR);
    }

    /***
     * Initialise RSS feed download Task
     */
    @Override
    public void onRefresh() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getResources().getString(R.string.loading));
        dialog.setCancelable(false);
        dialog.show();
        new LoadNewsFeedTask(this, this).execute(newsSources);
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
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /***
     * This callback is triggered when a error has occurred while trying to update the feed
     * @param message
     */
    @Override
    public void onError(String message) {
        if (dialog != null) {
            dialog.dismiss();
        }
        //Display error message to the user
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
