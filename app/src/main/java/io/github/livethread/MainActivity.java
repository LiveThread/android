package io.github.livethread;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.github.livethread.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.drawerLayout) DrawerLayout drawerLayout;
    @BindView(R.id.navigationView) NavigationView navigationView;
    private ActionBarDrawerToggle drawerToggle;

    private static final int REQ_CODE_LOGIN = 0;
    private static final String TAG = "lt_MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
        initNavigationDrawer();
        authenticateUser();

        // make sure a user is authenticated
        if (LiveThreadApplication.getAccountHelper().isAuthenticated())
            openProfile();
        else
            Log.e(TAG, "User not authenticated");
    }

    /**
     * Ensure a user is currently authenticated or can be refreshed or prompt user for login.
     */
    private void authenticateUser() {
        // check whats up with the user
        if (LiveThreadApplication.getTokenStore().size() == 0) {
            Log.d(TAG, "no tokens available- starting authentication view");
            newAuthentication();
            return;
        }

        String username = "";
        Log.d(TAG, "attempting to find old user and refresh that");
        // find the last logged in user
        SharedPreferences settings = getSharedPreferences(LiveThreadApplication.PREFS_NAME, 0);
        username = settings.getString("username", null);
        if (username == null) {
            Log.d(TAG, "user is null - starting authentication view");
            newAuthentication();
            return;
        } else {
            try {
                // try and renew for that user
                Log.d(TAG, "trying to refresh user");
                LiveThreadApplication.getAccountHelper().switchToUser(username);
                Log.d(TAG, "refresh successful");
            } catch (IllegalStateException e) {
                // nothing useable to do with that user.
                Log.d(TAG, "refresh failed");
                newAuthentication();
                return;
            }
        }
    }

    /**
     * Start the NewUserActivty for its result. To call when no other users are active.
     */
    private void newAuthentication() {
        startActivityForResult(new Intent(this, NewUserActivity.class), REQ_CODE_LOGIN);
    }

    // accept responses from the NewUserActivity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // The user could have pressed the back button before authorizing our app, make sure we have
        // an authenticated user before starting the UserOverviewActivity.
        if (requestCode == REQ_CODE_LOGIN && resultCode == RESULT_OK) {
            // store the account in prefs for quick checking later.
            SharedPreferences settings = getSharedPreferences(LiveThreadApplication.PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("username", LiveThreadApplication.getAccountHelper().getReddit().requireAuthenticatedUser());
            editor.commit();

            // Create new fragment and transaction
            Toast.makeText(this, "success", Toast.LENGTH_SHORT);
        }
    }

    /**
     * Initialize the navigation drawer.
     */
    private void initNavigationDrawer() {
        // handles some changes when the drawer is open or not
        drawerToggle = new ActionBarDrawerToggle(
                this,          /* host Activity */
                drawerLayout,          /* DrawerLayout object */
                R.string.main_navigation_drawer_open,   /* "open drawer" description */
                R.string.main_navigation_drawer_closed  /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("LiveThread");
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("Menu");
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        navigationView.setNavigationItemSelectedListener(new NavDrawerItemClickListener());

        // are these any use?
        assert (drawerLayout != null);
    }

    private void openProfile() {
        if (LiveThreadApplication.getAccountHelper().isAuthenticated()) {
            Log.d(TAG, "authenticated; starting user profile fragment");
            Bundle args = new Bundle();
            SharedPreferences settings = getSharedPreferences(LiveThreadApplication.PREFS_NAME, 0);
            String username = settings.getString("username", null);
            assert (username != null);
            ProfileFragment newFragment = ProfileFragment.newInstance(username);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack
            transaction.replace(R.id.frameLayout, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
    }

    /**
     * Handle clicks to the main navigation drawer (likely to switch fragments).
     */
    private class NavDrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            switch (id) {
                case R.id.navDrawer_home:
                    break;
                case R.id.navDrawer_popular:
                    break;
                case R.id.navDrawer_profile:
                    openProfile();
                    break;
                case R.id.navDrawer_settings:
                    break;
                case R.id.navDrawer_subreddits:
                    break;
                default:
                    return false;
            }
            drawerLayout.closeDrawers();
            return true;
        }
    }
}
