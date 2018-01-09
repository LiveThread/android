package io.github.livethread;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initNavigationDrawer();
    }

    /**
     * Initialize the navigation drawer.
     */
    private void initNavigationDrawer() {
        drawerLayout = findViewById(R.id.mainActivity_drawerLayout);

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
        // TODO: setup click handlers / populate drawer, etc.


        // are these any use?
        assert (drawerLayout != null);
    }

    /**
     * Handle clicks to the main navigation drawer (likely to switch fragments).
     */
    private class NavDrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();

            switch (id) {
                case R.id.mainActivity_navDrawer_home:
                    break;
                case R.id.mainActivity_navDrawer_popular:
                    break;
                case R.id.mainActivity_navDrawer_profile:
                    break;
                case R.id.mainActivity_navDrawer_settings:
                    break;
                case R.id.mainActivity_navDrawer_subreddits:
                    break;
            }
        }
    }
}
