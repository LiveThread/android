package guru.nickthompson.livethread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import guru.nickthompson.livethread.R;
import guru.nickthompson.livethread.fragments.PostListFragment;
import guru.nickthompson.livethread.fragments.SelectSubredditFragment;
import guru.nickthompson.redditapi.Post;

public class SubredditActivity extends AppCompatActivity implements SelectSubredditFragment.OnFragmentInteractionListener, PostListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);

        // set up initial fragment to be the subreddit selector
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_subreddit, new SelectSubredditFragment());
        ft.commit();
    }

    /**
     * Called when a subreddit is entered / clicked.
     *
     * @param subredditName name of the subreddit.
     */
    @Override
    public void onSubredditClick(String subredditName) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fl_subreddit, PostListFragment.newInstance(subredditName));
        ft.commit();
    }

    /**
     * Called on click of a Post.
     *
     * @param item the Post that was clicked.
     */
    @Override
    public void onListFragmentInteraction(Post item) {
        Intent intent = new Intent(getApplicationContext(), PostActivity.class);
        intent.putExtra("POST", item);
        startActivity(intent);
    }
}
