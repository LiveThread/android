package io.github.livethread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import io.github.livethread.AndroidTokenStore;
import io.github.livethread.AsyncCACRunner;
import io.github.livethread.AsyncCommandAndCallback;
import io.github.livethread.LiveThreadApplication;
import io.github.livethread.R;

/**
 * Main activity upon load.
 * Basically a testing activity it seems.
 */
public class MainActivity extends AppCompatActivity {
    private TextView tvAuthState;

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

        tvAuthState = (TextView) findViewById(R.id.tv_main_auth_state);
        tvAuthState.setText(authState.toString());
    }

    public void login(View view) {
        // TODO: what should the actual request code be?
        startActivityForResult(new Intent(this, LoginActivity.class), 1);
    }

    public static final int REQUEST_CODE = 1;

    // gets called on success from the login activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("rq " + requestCode + " rc " + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            Credentials credentials = ((LiveThreadApplication) getApplication()).getCredentials();
            String url = data.getStringExtra("RESULT_URL");

            // TODO make request on seperate thread to authenticate
            AsyncCACRunner<String> authAsync = new AsyncCACRunner<>(new Authenticate(url));
            authAsync.execute();

        }
    }

    /**
     * Little CAC to help with off main thread networking.
     */
    class Authenticate implements AsyncCommandAndCallback<String> {

        private final String url;

        /**
         * New Authenticate CAC
         *
         * @param url the special url obtained from authorizing the app in a users profile
         */
        Authenticate(String url) {
            this.url = url;
        }

        @Override
        public String command() {
            OAuthHelper oAuthHelper = AuthenticationManager.get().getRedditClient().getOAuthHelper();
            OAuthData oAuthData = null;
            try {
                oAuthData = oAuthHelper.onUserChallenge(url, ((LiveThreadApplication) getApplication()).getCredentials());
            } catch (OAuthException e) {
                e.printStackTrace();
            }
            AuthenticationManager.get().getRedditClient().authenticate(oAuthData);
            AuthenticationManager.get().onAuthenticated(oAuthData);

            return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
        }

        @Override
        public void callback(String username) {
            Toast.makeText(getApplicationContext(), "Logged in as " + username, Toast.LENGTH_SHORT).show();
            AuthenticationState authState = AuthenticationManager.get().checkAuthState();
            tvAuthState.setText(authState.toString());
        }
    }

    // TODO: need a refresh function
    // see https://github.com/jorgegil96/JRAW-Android-Sample
}
