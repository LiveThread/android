package guru.nickthompson.livethread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

/**
 * Activity for handling a post in Live Thread mode
 */
public class PostActivity extends AppCompatActivity {

    private String postId;
    private TextView tvPostId;

    private ArrayList<PostComment> comments;
    private PostCommentsAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postId = getIntent().getStringExtra("POST_ID");
        tvPostId = (TextView) findViewById(R.id.tv_post_id);
        tvPostId.setText("Post ID: " + postId);

        setupComments();

        progressBar = (ProgressBar) findViewById(R.id.pb_post_refresh);//.get();
        // new DelayRefreshTask(5000, progressBar).execute();
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

        // TODO: actually populate comments here, this is just dummy data
        comments = PostComment.testComments(20);
        // Create adapter passing in the sample user data
        adapter = new PostCommentsAdapter(this, comments);
        // Attach the adapter to the recyclerview to populate items
        recyclerView.setAdapter(adapter);
        // Set layout manager to position the items
        recyclerView.setLayoutManager(layoutManager);
    }

    // TODO: remove after button is removed. just for testing
    public void addCommentTest(View view) {
        addComment(new PostComment("new-post-username", new Date(), "commenta a;sd;fi asdf"));
    }

    /**
     * Called onClick of R.id.b_post_update_comments (labeled "update comments")
     */
    public void updateComments(View view) {
        // TODO: fill in with implemented parser
        if (comments.size() == 0) {
            // fetch all comments
        } else {
            // just update comments
        }
    }

    /**
     * Add a comment to the RecyclerView.
     *
     * @param comment the new comment.
     */
    private void addComment(PostComment comment) {
        comments.add(0, comment);
        adapter.notifyItemInserted(0);
        recyclerView.smoothScrollToPosition(0);
    }
}
