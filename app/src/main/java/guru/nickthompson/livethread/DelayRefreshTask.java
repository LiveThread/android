package guru.nickthompson.livethread;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.widget.ProgressBar;

import java.util.ArrayList;

import guru.nickthompson.redditapi.Comment;

/**
 * Created by williamreed on 9/29/17.
 * <p>
 * A delayed task to handle periodic refreshes for the comments.<br/>
 * param should be void<br/>
 * progress tracking is Integer<br/>
 * response is ArrayList<PostComment><br/>
 */
public class DelayRefreshTask extends AsyncTask<Void, Integer, ArrayList<Comment>> {
    private float interval;
    private ProgressBar progress;
    private AsyncCommandAndCallback<ArrayList<Comment>> commandAndCallback;

    /**
     * Create a new DelayRefreshTask.
     *
     * @param duration           in milliseconds
     * @param progress           the progress bar
     * @param commandAndCallback the command and response for this task.
     */
    public DelayRefreshTask(long duration, ProgressBar progress,
                            AsyncCommandAndCallback<ArrayList<Comment>> commandAndCallback) {
        this.progress = progress;
        this.commandAndCallback = commandAndCallback;

        interval = duration / 1000;
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... params) {
        for (int i = 0; i < 1000; i++) {
            SystemClock.sleep((long) interval);
            publishProgress(i);
        }
        publishProgress(1000);

        return commandAndCallback.command();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        progress.setProgress(values[0]);
    }

    // called on main UI thread wohoo
    @Override
    protected void onPostExecute(ArrayList<Comment> result) {
        commandAndCallback.callback(result);
    }


}
