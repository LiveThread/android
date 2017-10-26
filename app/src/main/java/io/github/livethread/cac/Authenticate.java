package io.github.livethread.cac;

import android.widget.Toast;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.http.oauth.OAuthData;
import net.dean.jraw.http.oauth.OAuthException;
import net.dean.jraw.http.oauth.OAuthHelper;

import io.github.livethread.LiveThreadApplication;

/**
 * Little CAC to help with off main thread networking.
 */
public class Authenticate implements AsyncCommandAndCallback<String> {

    private final LiveThreadApplication app;
    private final String url;

    /**
     * New Authenticate CAC
     *
     * @param app the liveThreadApplication
     * @param url the special url obtained from authorizing the app in a users profile
     */
    public Authenticate(LiveThreadApplication app, String url) {
        this.app = app;
        this.url = url;
    }

    @Override
    public String command() {
        OAuthHelper oAuthHelper = AuthenticationManager.get().getRedditClient().getOAuthHelper();
        OAuthData oAuthData = null;
        try {
            oAuthData = oAuthHelper.onUserChallenge(url,
                    app.getCredentials());
        } catch (OAuthException e) {
            e.printStackTrace();
        }
        AuthenticationManager.get().getRedditClient().authenticate(oAuthData);
        AuthenticationManager.get().onAuthenticated(oAuthData);

        return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
    }

    @Override
    public void callback(String username) {
        Toast.makeText(app.getApplicationContext(), username + " logged in.", Toast.LENGTH_SHORT).show();
    }
}