package io.github.livethread;

import android.app.Application;

import net.dean.jraw.http.oauth.Credentials;

/**
 * Created by williamreed on 10/22/17.
 * Handles some unique instances we want to keep around.
 */
public class LiveThreadApplication extends Application {
    private static final String CLIENT_ID = "ZNyXFcDBJ0TNBQ";
    // URL needs to be the same as the one in the reddit app in prefs
    // TODO: we should change this to something like an in app url
    private static final String REDIRECT_URL = "https://github.com/inickt/LiveThread-Android";

    private Credentials credentials;

    /**
     * @return the credentials for OAuth / JRAW.
     */
    public Credentials getCredentials() {
        if (credentials == null) {
            credentials = Credentials.installedApp(CLIENT_ID, REDIRECT_URL);
        }
        return credentials;
    }

}
