package guru.nickthompson.livethread;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

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
    public void onSubredditClick(String subredditName) {
        // TODO: start new fragment with this subreddit
        Toast.makeText(getApplicationContext(), subredditName, Toast.LENGTH_SHORT).show();
    }
}
