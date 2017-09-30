package guru.nickthompson.livethread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

import guru.nickthompson.livethread.R;
import guru.nickthompson.redditapi.Post;

/**
 * Main activity upon load
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void loadPost(View view) {
        EditText etPostId = (EditText) findViewById(R.id.et_post_id);
        String postId = etPostId.getText().toString();

        if (postId.equals("")) {
            Toast.makeText(getApplicationContext(), "Post ID is empty", Toast.LENGTH_SHORT).show();
            return;
        } else if (postId.length() != 6) {
            Toast.makeText(getApplicationContext(), "Illegal Post ID given", Toast.LENGTH_SHORT).show();
            return;
        }

        Post post = new Post(postId, "title", 0, 0, new Date(150800546), "nick");

        // pass into next activity
        Intent intent = new Intent(this, PostActivity.class);
        intent.putExtra("POST", post);
        startActivity(intent);
    }
}
