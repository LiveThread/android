package io.github.livethread.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import net.dean.jraw.RedditClient;

import io.github.livethread.R;
import io.github.livethread.cac.AsyncCACRunner;
import io.github.livethread.cac.AsyncCommandAndCallback;

/**
 * Main activity upon load.
 * Basically a testing activity it seems.
 */
public class MainActivityOld extends AppCompatActivity {
    private TextView tvAuthState;
    private TextView tvName;
    private TextView tvCommentKarma;
    private TextView tvLinkKarma;
    private RedditClient redditClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }




    /**
     * Show some temporary user data so we know its working.
     */
    private void showUserData() {
        new AsyncCACRunner<>(new BasicInfo()).execute();
    }





    /**
     * CAC for getting basic user Data.
     * Must be replaced by a more versatile network Queue. too much to create this for every network call.
     */
    class BasicInfo implements AsyncCommandAndCallback<Void> {

        int linkKarma;
        int commentKarma;
        String name;

        @Override
        public Void command() {
            linkKarma = redditClient.me().getLinkKarma();
            commentKarma = redditClient.me().getCommentKarma();
            name = redditClient.me().getFullName();

            return null;
        }

        @Override
        public void callback(Void result) {
            System.out.println("set basic info");
            tvName.setText("Name: " + name);
            tvCommentKarma.setText("Comment Karma: " + commentKarma);
            tvLinkKarma.setText("Link Karma: " + linkKarma);
        }
    }
}
