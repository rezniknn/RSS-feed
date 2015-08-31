package android.com.reznikalexey.ui.activities;

import android.app.Activity;
import android.com.reznikalexey.R;
import android.com.reznikalexey.model.ArticleEntry;
import android.com.reznikalexey.ui.adapters.ArticlesAdapter;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class NewsFeedActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String LOG_TAG = "NewsFeedActivity";

    private SwipeRefreshLayout srlContainer;
    private ListView lvContainer;
    private ArticlesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initViews();
        onRefresh();
    }

    private void initViews() {
        srlContainer = (SwipeRefreshLayout) findViewById(R.id.srl_main_container);
        srlContainer.setOnRefreshListener(this);
        lvContainer = (ListView) findViewById(R.id.lv_container);
        adapter = new ArticlesAdapter(this, R.layout.article_entry_layout);
        lvContainer.setAdapter(adapter);
        lvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.getItem(position).switchDetailedView();
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

    @Override
    public void onRefresh() {
        if (adapter != null) {
            adapter.clear();

            for (int i = 0; i < 5; i++) {
                ArticleEntry dummyEntry = new ArticleEntry(
                        ArticleEntry.ArticleSource.LENTA,
                        "Dummy article", "Dummy description",
                        "http://lorempixel.com/1028/720/");
                adapter.add(dummyEntry);
                dummyEntry.loadImage(adapter);
            }
        } else {
            Log.e(LOG_TAG, "Adapter is null");
        }

        srlContainer.setRefreshing(false);
    }
}
