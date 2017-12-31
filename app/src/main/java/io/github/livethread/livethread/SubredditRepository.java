package io.github.livethread.livethread;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import net.dean.jraw.models.Subreddit;

/**
 * Created by williamreed on 12/31/17.
 * Repository for getting subreddit information. Meant to serve as an abstraction to allow for
 * changing of data sources down the line in case of new API's / testing / etc.
 */

public class SubredditRepository {

    LiveData<Subreddit> getSubredit(String name) {
        final MutableLiveData<Subreddit> data = new MutableLiveData<>();

        // TODO: actually call JRAW to get the subreddit...

        return data;
    }


}
