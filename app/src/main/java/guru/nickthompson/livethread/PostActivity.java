package guru.nickthompson.livethread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Activity for handling a post in Live Thread mode
 */
public class PostActivity extends AppCompatActivity {

    private String postId;
    private TextView tvPostId;

    ArrayList<PostComment> comments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postId = getIntent().getStringExtra("POST_ID");
        tvPostId = (TextView) findViewById(R.id.tv_post_id);
        tvPostId.setText("Post ID: " + postId);

        setupComments();
    }

    /**
     * Setup recycler view and get the data setup.
     */
    private void setupComments() {
        // Lookup the recyclerview in activity layout
        RecyclerView rvComments = (RecyclerView) findViewById(R.id.rv_post_comments);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        // add a horizontal line between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvComments.getContext(),
                DividerItemDecoration.VERTICAL);
        rvComments.addItemDecoration(dividerItemDecoration);

        // TODO: actually populate comments here, this is just dummy data
        comments = PostComment.testComments(20);
        // Create adapter passing in the sample user data
        PostCommentsAdapter adapter = new PostCommentsAdapter(this, comments);
        // Attach the adapter to the recyclerview to populate items
        rvComments.setAdapter(adapter);
        // Set layout manager to position the items
        rvComments.setLayoutManager(layoutManager);


    }
}
