package io.github.livethread.redditapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by williamreed on 9/30/17.
 * <p>
 * Represent information for a subreddit to show in the suggest subreddits. Minimal information needed.
 */

public class Subreddit {
    private final String name;
    private final String description;

    /**
     * @param name
     * @param description
     */
    public Subreddit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * @return the name of the subreddit.
     */
    public String getName() {
        return name;
    }

    /**
     * @return the description of the subreddit.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets all hot posts from this subreddit.
     *
     * @param subreddit the name of the subreddit.
     * @return an ArrayList of Posts.
     */
    public static ArrayList<Post> getAllPosts(String subreddit) {
        ArrayList<Post> posts = new ArrayList<>();

        try {
            JsonArray rawData = new JsonParser().parse(UrlUtils.readUrlSubreddit(subreddit))
                    .getAsJsonObject().get("data")
                    .getAsJsonObject().get("children")
                    .getAsJsonArray();

            for (JsonElement t3 : rawData) {
                JsonObject postData = t3.getAsJsonObject().getAsJsonObject("data");

                if (!postData.get("stickied").getAsBoolean()) {
                    String id = postData.get("id").getAsString();
                    String author = postData.get("author").getAsString();
                    Date timestamp = new Date(postData.get("created_utc").getAsLong() * 1000);
                    String title = postData.get("title").getAsString();
                    int score = postData.get("score").getAsInt();
                    int numComments = postData.get("num_comments").getAsInt();

                    posts.add(new Post(id, title, score, numComments, timestamp, author));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }
}
