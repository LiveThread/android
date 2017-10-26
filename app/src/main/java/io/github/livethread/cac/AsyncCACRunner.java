package io.github.livethread.cac;

import android.os.AsyncTask;

/**
 * Created by williamreed on 9/30/17.
 * <p>
 * AsyncTask meant to execute an {@link AsyncCommandAndCallback}.
 * Just meant for the common format of fetching from online and doing something with the data
 * to the UI.
 */
public class AsyncCACRunner<T> extends AsyncTask<Void, Void, T> {

    AsyncCommandAndCallback<T> func;

    /**
     * Create a new PostFetcherAsync.
     *
     * @param func the function object to use
     */
    public AsyncCACRunner(AsyncCommandAndCallback<T> func) {
        this.func = func;
    }

    @Override
    protected T doInBackground(Void... voids) {
        return func.command();
    }

    @Override
    protected void onPostExecute(T result) {
        func.callback(result);
    }
}