package guru.nickthompson.livethread;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import guru.nickthompson.redditapi.Comment;

/**
 * Created by williamreed on 9/29/17.
 * <p>
 * A delayed task to handle periodic refreshes for the comments. Handles recalling itself
 * until cancelled (most likely a hacky way to do this, but the advantages of an AsyncTask are
 * neccessary and could not be completed with ScheduledExecutorService)<br/>
 * param should be void<br/>
 * progress tracking is Integer<br/>
 * response is ArrayList<PostComment><br/>
 */
public class DelayRefreshTask extends AsyncTask<Void, Integer, ArrayList<Comment>> {
    private long duration;
    private float interval;

    private ProgressBar progress;
    private int maxProgress;

    private AsyncCommandAndCallback<ArrayList<Comment>> commandAndCallback;
    private AtomicBoolean run;
    private boolean runOnce = false;

    private static final String TAG = "LT.DelayRefreshTask";

    /**
     * Create a new DelayRefreshTask.
     *
     * @param duration           in milliseconds
     * @param progress           the progress bar
     * @param commandAndCallback the command and response for this task.
     * @param run                reference to boolean to signal stopping
     */
    public DelayRefreshTask(long duration, ProgressBar progress,
                            AsyncCommandAndCallback<ArrayList<Comment>> commandAndCallback,
                            AtomicBoolean run) {
        Log.d(TAG, "created new DelayRefreshTask");
        this.duration = duration;
        this.progress = progress;
        this.commandAndCallback = commandAndCallback;
        this.run = run;
        this.maxProgress = progress.getMax();

        interval = duration / maxProgress;
    }

    /**
     * Create a new DelayRefreshTask to only be run once.
     *
     * @param progress           the progress bar.
     * @param commandAndCallback the command and response for this task.
     */
    public DelayRefreshTask(ProgressBar progress, AsyncCommandAndCallback<ArrayList<Comment>> commandAndCallback) {
        Log.d(TAG, "created single run DelayRefreshTask");
        this.duration = 0;
        this.progress = progress;
        this.commandAndCallback = commandAndCallback;
        interval = 0;
        runOnce = true;
        run = new AtomicBoolean(false);
    }

    @Override
    protected ArrayList<Comment> doInBackground(Void... params) {
        if (runOnce) {
            publishProgress(maxProgress);
            return commandAndCallback.command();
        }

        for (int i = 0; i < maxProgress; i++) {
            SystemClock.sleep((long) interval);
            publishProgress(i);

            if (!run.get()) {
                Log.d(TAG, "running cancelled mid execution");
                this.cancel(true);
                return new ArrayList<Comment>();
            }
        }
        publishProgress(maxProgress);

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

        // if we can continue running, continue running.
        if (run.get()) {
            new DelayRefreshTask(duration, progress, commandAndCallback, run).execute();
        } else {
            Log.d(TAG, "running canceled");
        }
    }


}
