package io.github.livethread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;

import io.github.livethread.AndroidTokenStore;
import io.github.livethread.LiveThreadApplication;
import io.github.livethread.R;

/**
 * Main activity upon load.
 * Basically a testing activity it seems.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init reddit stuff
        RedditClient redditClient = new RedditClient(UserAgent.of("android",
                "io.github.livethread", "v0.0.1", "rillweed"));
        TokenStore store = new AndroidTokenStore(
                PreferenceManager.getDefaultSharedPreferences(this));
        RefreshTokenHandler refreshTokenHandler = new RefreshTokenHandler(store, redditClient);

        AuthenticationManager manager = AuthenticationManager.get();
        manager.init(redditClient, refreshTokenHandler);

        // check auth state and display it
        AuthenticationState authState = AuthenticationManager.get().checkAuthState();

        TextView tvAuthState = (TextView) findViewById(R.id.tv_main_auth_state);
        tvAuthState.setText(authState.toString());


        // String username = AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
        // Toast.makeText(this, "Logged in as " + username, Toast.LENGTH_SHORT);
    }

    public void login(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }

    public static final int REQUEST_CODE = 1;

    // gets called on success from the login activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Credentials credentials = ((LiveThreadApplication) getApplication()).getCredentials();

            // TODO make request on seperate thread to authenticate
        }
    }
}
