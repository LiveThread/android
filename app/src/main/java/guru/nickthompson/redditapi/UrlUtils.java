package guru.nickthompson.redditapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Utilities for reading Reddit URLs.
 */
public class UrlUtils {

    // User agent for the app. TODO get version number
    private static final String userAgent = "android:guru.nickthompson.livethread:v0.0.1";

    /**
     * Returns the content from a provided String URL.
     *
     * @param urlString is a String representation of a URL.
     * @return the content from the given URL.
     * @throws Exception when there is a problem reading the URL. TODO fix up Exceptions
     */
    protected static String readUrl(String urlString) throws Exception {

        BufferedReader reader = null;

        try {
            // Creates a new URL and sets the user agent
            URLConnection url = new URL(urlString).openConnection();
            url.setRequestProperty("User-Agent", userAgent);

            // magic!!
            reader = new BufferedReader(new InputStreamReader(url.getInputStream()));
            StringBuffer buffer = new StringBuffer();
            int read;
            char[] chars = new char[1024];
            while ((read = reader.read(chars)) != -1)
                buffer.append(chars, 0, read);

            return buffer.toString();
        } finally {
            if (reader != null)
                reader.close();
        }
    }

    /**
     * Returns the Reddit JSON content from a provided post ID.
     *
     * @param postID the Reddit ID of the {@link Post}.
     * @return the Reddit content from the given URL.
     * @throws Exception when there is a problem reading the URL. TODO fix up Exceptions
     */
    protected static String readUrlID(String postID) throws Exception {
        return readUrl(getPostUrl(postID));
    }

    /**
     * Provides the full URL of the new JSON data for a given post ID.
     *
     * @param postID the Reddit ID of the {@link Post}.
     * @return a String representation of the full URL.
     */
    protected static String getPostUrl(String postID) {
        return "https://www.reddit.com/comments/" + postID + ".json?sort=new&raw_json=1";
    }

    /**
     * Provides the full URL of the new JSON data for a given subreddit.
     *
     * @param subreddit
     * @return a String representation of the full URL.
     */
    protected static String getSubredditUrl(String subreddit) {
        return "https://www.reddit.com/r/" + subreddit + ".json?sort=new&raw_json=1";
    }

    /**
     * Returns the Reddit JSON content from a provided post ID.
     *
     * @param subreddit the
     * @return the Reddit content from the given URL.
     * @throws Exception when there is a problem reading the URL. TODO fix up Exceptions
     */
    protected static String readUrlSubreddit(String subreddit) throws Exception {
        return readUrl(getSubredditUrl(subreddit));
    }
}
