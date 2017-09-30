package guru.nickthompson.livethread;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Activity for handling a post in Live Thread mode
 */
public class PostActivity extends AppCompatActivity {

    private String postId;
    private TextView tvPostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        postId = getIntent().getStringExtra("POST_ID");
        tvPostId = (TextView) findViewById(R.id.tv_post_id);
        tvPostId.setText(postId);
    }
}
