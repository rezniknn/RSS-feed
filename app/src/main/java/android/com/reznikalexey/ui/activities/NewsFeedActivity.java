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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_feed);
        initViews();
    }

    private void initViews() {
        srlContainer = (SwipeRefreshLayout) findViewById(R.id.srl_main_container);
        srlContainer.setOnRefreshListener(this);
        lvContainer = (ListView) findViewById(R.id.lv_container);
        ArticlesAdapter adapter = new ArticlesAdapter(this, R.layout.article_entry_layout);
        lvContainer.setAdapter(adapter);
        lvContainer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView tvDescription = (TextView) view.findViewById(R.id.tv_description);
                if (tvDescription != null) {
                    if (tvDescription.getVisibility() == View.VISIBLE) {
                        tvDescription.setVisibility(View.GONE);
                    } else {
                        tvDescription.setVisibility(View.VISIBLE);
                    }
                } else {
                    Log.e(LOG_TAG, "Failed to find Article Description view");
                }
            }
        });
        ArticleEntry dummyEntry = new ArticleEntry(
                ArticleEntry.ArticleSource.LENTA,
                "Dummy article", "Dummy description",
                "http://icdn.lenta.ru/images/2015/08/31/14/20150831144500846/pic_2d8e8828351bc14c4e89fe083f03a918.jpg");
        for (int i=0; i<10; i++) {
            adapter.add(dummyEntry);
        }
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
        //TODO refresh logic
        srlContainer.setRefreshing(false);
    }
}
