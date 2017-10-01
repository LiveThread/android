package guru.nickthompson.livethread.activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import guru.nickthompson.livethread.R;
import guru.nickthompson.livethread.fragments.PostListFragment;
import guru.nickthompson.livethread.fragments.SelectSubredditFragment;
import guru.nickthompson.redditapi.Post;

/**
 * Handles choosing a subreddit and navigating to a certain post from the sub through fragments.
 */
public class SubredditActivity extends AppCompatActivity implements SelectSubredditFragment.OnFragmentInteractionListener, PostListFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);

        // set up initial fragment to be the subreddit selector
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fl_subreddit, new SelectSubredditFragment());
            ft.commit();

            // setup action bar
            Toolbar toolbar = (Toolbar) findViewById(R.id.t_subreddit);
            toolbar.setTitle("LiveThread");
            setSupportActionBar(toolbar);
            toolbar.setNavigationIcon(R.mipmap.ic_launcher);
        }
    }

    /**
     * Called when a subreddit is entered / clicked.
     *
     * @param subredditName name of the subreddit.
     */
    @Override
    public void onSubredditClick(String subredditName) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
        ft.replace(R.id.fl_subreddit, PostListFragment.newInstance(subredditName))
                .addToBackStack(null);
        ft.commit();

        // if keyboard is visible hide it
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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

    /**
     * Handle back button returning to previous fragment (and activity).
     */
    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();

        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
