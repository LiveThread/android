package io.github.livethread.subreddit;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import net.dean.jraw.models.Subreddit;

/**
 * Created by williamreed on 12/31/17.
 * <p>
 * ViewModel for Subreddits. Likely containing sidebar info & listing of posts in the subreddit.
 * Prepares the data for the UI.
 */
public class SubredditViewModel extends ViewModel {
    private String subredditName;
    // TODO: what does Subreddit.class actually contain?
    private LiveData<Subreddit> subreddit;

    public void init(String subredditName) {
        this.subredditName = subredditName;
    }

    public LiveData<Subreddit> getSubreddit() {
        return subreddit;
    }
}
