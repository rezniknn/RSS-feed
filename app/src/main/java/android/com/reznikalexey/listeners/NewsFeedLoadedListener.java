package android.com.reznikalexey.listeners;

import android.com.reznikalexey.model.ArticleEntry;

import java.util.ArrayList;

/**
 * Created by alexeyreznik on 31/08/15.
 */
public interface NewsFeedLoadedListener {
    public abstract void onFeedLoaded(ArrayList<ArticleEntry> articleEntries);
    public abstract void onError(String message);
}
