package io.github.livethread.activities;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.AuthenticationState;
import net.dean.jraw.auth.NoSuchTokenException;
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
    private TextView tvName;
    private TextView tvCommentKarma;
    private TextView tvLinkKarma;
    private RedditClient redditClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // init reddit stuff
        redditClient = new RedditClient(UserAgent.of("android",
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
        tvName = (TextView) findViewById(R.id.tv_main_name);
        tvCommentKarma = (TextView) findViewById(R.id.tv_main_comment_karma);
        tvLinkKarma = (TextView) findViewById(R.id.tv_main_link_karma);

        if (authState == AuthenticationState.NEED_REFRESH) {
            new AsyncCACRunner<>(new AuthenticateRefresh(((LiveThreadApplication) getApplication())
                    .getCredentials())).execute();
        } else if (authState == AuthenticationState.NONE) {
            // TODO: what should the actual request code be?
            // TODO: should login be a fragment?
            startActivityForResult(new Intent(this, LoginActivity.class), 1);
        } else if (authState == AuthenticationState.READY) {
            showUserData();
        }
    }

    public static final int REQUEST_CODE = 1;

    // gets called on success from the login activity.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println("rq " + requestCode + " rc " + resultCode);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            String url = data.getStringExtra("RESULT_URL");

            AsyncCACRunner<String> authAsync = new AsyncCACRunner<>(new Authenticate(url));
            authAsync.execute();

        }
    }


    /**
     * Show some temporary user data so we know its working.
     */
    private void showUserData() {
        new AsyncCACRunner<>(new BasicInfo()).execute();
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
                oAuthData = oAuthHelper.onUserChallenge(url,
                        ((LiveThreadApplication) getApplication()).getCredentials());
            } catch (OAuthException e) {
                e.printStackTrace();
            }
            AuthenticationManager.get().getRedditClient().authenticate(oAuthData);
            AuthenticationManager.get().onAuthenticated(oAuthData);

            return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
        }

        @Override
        public void callback(String username) {
            System.out.println("initial authentication");
            Toast.makeText(getApplicationContext(), "Logged in as " + username, Toast.LENGTH_SHORT)
                    .show();
            AuthenticationState authState = AuthenticationManager.get().checkAuthState();
            tvAuthState.setText(authState.toString());
            showUserData();
        }
    }

    /**
     * CAC for handling authentication refresh
     */
    class AuthenticateRefresh implements AsyncCommandAndCallback<String> {

        private final Credentials credentials;

        /**
         * New Authenticate Refresh CAC
         *
         * @param credentials the credentials used to authenticate the user
         */
        AuthenticateRefresh(Credentials credentials) {
            this.credentials = credentials;
        }

        @Override
        public String command() {
            try {
                AuthenticationManager.get().refreshAccessToken(((LiveThreadApplication) getApplication())
                        .getCredentials());
            } catch (NoSuchTokenException e) {
                e.printStackTrace();
            } catch (OAuthException e) {
                e.printStackTrace();
            }

            return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
        }

        @Override
        public void callback(String username) {
            System.out.println("refresh authenitcation");
            Toast.makeText(getApplicationContext(), "Logged in as " + username, Toast.LENGTH_SHORT).show();
            AuthenticationState authState = AuthenticationManager.get().checkAuthState();
            tvAuthState.setText(authState.toString());
            showUserData();
        }
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
