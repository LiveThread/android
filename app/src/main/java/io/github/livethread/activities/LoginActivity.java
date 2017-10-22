package io.github.livethread.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import net.dean.jraw.RedditClient;
import net.dean.jraw.auth.AuthenticationManager;
import net.dean.jraw.auth.RefreshTokenHandler;
import net.dean.jraw.auth.TokenStore;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.http.oauth.Credentials;
import net.dean.jraw.http.oauth.OAuthHelper;

import java.net.URL;

import io.github.livethread.AndroidTokenStore;
import io.github.livethread.R;

public class LoginActivity extends AppCompatActivity {
    WebView webView;
    private static final String CLIENT_ID = "ZNyXFcDBJ0TNBQ";
    // URL needs to be the same as the one in the reddit app in prefs
    private static final String REDIRECT_URL = "https://github.com/inickt/LiveThread-Android";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        RedditClient redditClient = new RedditClient(UserAgent.of("android",
                "io.github.livethread", "v0.0.1", "rillweed"));
        TokenStore store = new AndroidTokenStore(
                PreferenceManager.getDefaultSharedPreferences(this));
        RefreshTokenHandler refreshTokenHandler = new RefreshTokenHandler(store, redditClient);

        AuthenticationManager manager = AuthenticationManager.get();
        manager.init(redditClient, refreshTokenHandler);

        webView = (WebView) findViewById(R.id.wv_login);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                return false;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                if (url.contains("code=")) {
                    webView.stopLoading();
                    setResult(RESULT_OK, new Intent().putExtra("RESULT_URL", url));
                    finish();
                } else if (url.contains("error=")) {
                    Toast.makeText(
                            LoginActivity.this,
                            "Login access denied",
                            Toast.LENGTH_LONG
                    ).show();
                    webView.loadUrl(getAuthorizationUrl().toExternalForm());
                }
            }
        });
        webView.loadUrl(getAuthorizationUrl().toExternalForm());
    }

    private URL getAuthorizationUrl() {
        OAuthHelper oAuthHelper = AuthenticationManager.get().getRedditClient().getOAuthHelper();
        Credentials credentials = Credentials.installedApp(CLIENT_ID, REDIRECT_URL);
        String[] scopes = {"identity", "edit", "flair", "mysubreddits", "read", "save", "subscribe", "vote"};
        URL url = oAuthHelper.getAuthorizationUrl(credentials, true, true, scopes);
        System.out.println(url.toString());
        return url;
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
