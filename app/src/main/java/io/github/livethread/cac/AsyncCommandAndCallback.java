package io.github.livethread.cac;

/**
 * Created by williamreed on 9/30/17.
 * <p>
 * To be used as a function object called during and after an async task.
 */

public interface AsyncCommandAndCallback<T> {

    /**
     * To be called to get the new comments. Async thread.
     *
     * @return new comments.
     */
    T command();

    /**
     * Adds comments to UI. Main UI thread.
     *
     * @param result result from command method.
     */
    void callback(T result);
}
