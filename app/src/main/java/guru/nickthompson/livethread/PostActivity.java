package guru.nickthompson.livethread;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicBoolean;

import guru.nickthompson.redditapi.Comment;
import guru.nickthompson.redditapi.Post;

/**
 * Activity for handling a post in Live Thread mode
 */
public class PostActivity extends AppCompatActivity {
    // 5000 ms (5s) delay between refreshses
    private static final long DELAY = 5000;
    private static final String TAG = "LT.PostActivity";

    private Post post;
    private TextView tvPostId;

    private ArrayList<Comment> comments;
    private CommentsAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private Handler refreshHandler;
    private Runnable refreshRunnable;
    private volatile AtomicBoolean runRefresh = new AtomicBoolean(true);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        String postID = getIntent().getStringExtra("POST_ID");
        tvPostId = (TextView) findViewById(R.id.tv_post_id);
        tvPostId.setText("Post ID: " + postID);
        post = new Post(postID);

        setupComments();

        progressBar = (ProgressBar) findViewById(R.id.pb_post_refresh);//.get();
        // new DelayRefreshTask(5000, progressBar).execute();

        Log.d(TAG, "calling repeating refresh");
        initializeRepeatingRefresh();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();

        cancelRefresh();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Cancel the refresh. Lets the current AsyncTask finish then it stops repeatedly calling.
     */
    private void cancelRefresh() {
        runRefresh.set(false);
    }

    /**
     * Responsible for repeatedly refreshing the comments.
     */
    private void initializeRepeatingRefresh() {
        final CommentRefresher commentRefresherFunctionObject = new CommentRefresher();
        //initial run
        new DelayRefreshTask(progressBar, commentRefresherFunctionObject).execute();

        // helps run code on a given thread after a delay & periodically
        refreshHandler = new Handler();

        refreshRunnable = new Runnable() {
            @Override
            public void run() {
                new DelayRefreshTask(DELAY, progressBar, commentRefresherFunctionObject, runRefresh)
                        .execute();
            }
        };

        refreshHandler.post(refreshRunnable);
    }


    /**
     * Setup recycler view and get the data setup.
     */
    private void setupComments() {
        // Lookup the recyclerview in activity layout
        recyclerView = (RecyclerView) findViewById(R.id.rv_post_comments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // add a horizontal line between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        comments = new ArrayList<>();
        // Create adapter passing in the sample user data
        adapter = new CommentsAdapter(this, comments);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(layoutManager);
    }

    /**
     * Add a comment to the RecyclerView.
     *
     * @param comment the new comment.
     */
    private void addComment(Comment comment) {
        comments.add(0, comment);
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    }

    /**
     * The function object that handles comment refreshing.
     */
    public class CommentRefresher implements AsyncCommandAndCallback<ArrayList<Comment>> {

        @Override
        public ArrayList<Comment> command() {
            Log.d(TAG, "running command");
            if (comments.size() == 0) {
                return post.getAllComments();
            } else {
                return post.getCommentsAfter(comments.get(0).getID());
            }
        }

        @Override
        public void callback(ArrayList<Comment> result) {
            if (comments.size() == 0) {
                Collections.reverse(result);
                for (Comment c : result) {
                    addComment(c);
                }
            } else {
                Collections.reverse(result);
                for (Comment c : result) {
                    addComment(c);
                }
            }
        }
    }
}
