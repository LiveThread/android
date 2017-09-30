package guru.nickthompson.redditapi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by nick on 9/29/17.
 */

public class UrlUtils {

    private static final String userAgent = "android:guru.nickthompson.livethread:v0.0.1";

    public static String readUrl(String urlString) throws Exception {

        BufferedReader reader = null;

        try {
            URLConnection url = new URL(urlString).openConnection();
            url.setRequestProperty("User-Agent", userAgent);

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

    public static String readUrlID(String postID) throws Exception {
        return readUrl(getPostUrl(postID));
    }

    public static String getPostUrl(String postID) {
        return "https://www.reddit.com/comments/" + postID + ".json?sort=new";
    }


}
