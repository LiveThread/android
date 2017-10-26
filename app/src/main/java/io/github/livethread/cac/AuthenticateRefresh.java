package io.github.livethread.cac;

import android.widget.Toast;

import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.NoSuchTokenException;
import net.dean.jraw.http.oauth.OAuthException;

import io.github.livethread.LiveThreadApplication;

/**
 * CAC for handling authentication refresh
 */
public class AuthenticateRefresh implements AsyncCommandAndCallback<String> {

    private final LiveThreadApplication app;

    /**
     * New Authenticate Refresh CAC
     *
     * @param app the liveThreadApplication
     */
    public AuthenticateRefresh(LiveThreadApplication app) {
        this.app = app;
    }

    @Override
    public String command() {
        try {
            AuthenticationManager.get().refreshAccessToken(app.getCredentials());
        } catch (NoSuchTokenException e) {
            e.printStackTrace();
        } catch (OAuthException e) {
            e.printStackTrace();
        }

        return AuthenticationManager.get().getRedditClient().getAuthenticatedUser();
    }

    @Override
    public void callback(String username) {
        Toast.makeText(app.getApplicationContext(), username + " logged in.", Toast.LENGTH_SHORT).show();
    }
}