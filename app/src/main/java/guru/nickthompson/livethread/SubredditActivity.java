package guru.nickthompson.livethread;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class SubredditActivity extends AppCompatActivity implements SelectSubreddit.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subreddit);

        // set up initial fragment to be the subreddit selector
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fl_subreddit, new SelectSubreddit());
        ft.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
