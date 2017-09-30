package guru.nickthompson.redditapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by nick on 9/29/17.
 */

public class ParsePost {
    private final Post post;

    public ParsePost(Post post) {
        this.post = post;
    }

    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> comments = new ArrayList<>();

        try {
            JsonArray rawData = new JsonParser().parse(UrlUtils.readUrlID(this.post.getID())).getAsJsonArray();

            JsonObject postListing = rawData.get(0).getAsJsonObject();

            JsonObject commentListing = rawData.get(1).getAsJsonObject();

            JsonArray commentChildren = commentListing.get("data").getAsJsonObject().getAsJsonArray("children");

            for (JsonElement t1 : commentChildren) {
                JsonObject commentData = t1.getAsJsonObject().getAsJsonObject("data");

                Date timestamp = new Date(commentData.get("created_utc").getAsLong() * 1000);
                String username = commentData.get("author").getAsString();
                String body = commentData.get("body_html").getAsString();

                comments.add(new Comment(this.post, username, timestamp, body));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }

    public ArrayList<Comment> getCommentsAfter(String postID) {



        return null;
    }
}
