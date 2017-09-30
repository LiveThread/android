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

public class Post {
    private final String postID;

    public Post(String postID) {
        this.postID = postID;
    }

    public String getID() {
        return this.postID;
    }

    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> comments = new ArrayList<>();

        try {
            JsonArray rawData = new JsonParser().parse(UrlUtils.readUrlID(this.postID)).getAsJsonArray();

            JsonObject postListing = rawData.get(0).getAsJsonObject();
            JsonObject commentListing = rawData.get(1).getAsJsonObject();

            JsonArray commentChildren = commentListing.get("data").getAsJsonObject().getAsJsonArray("children");

            for (JsonElement t1 : commentChildren) {
                JsonObject commentData = t1.getAsJsonObject().getAsJsonObject("data");

                String commentID = commentData.get("id").getAsString();
                String username = commentData.get("author").getAsString();
                Date timestamp = new Date(commentData.get("created_utc").getAsLong() * 1000);
                String body = commentData.get("body_html").getAsString();

                comments.add(new Comment(this, commentID, username, timestamp, body));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return comments;
    }

    public ArrayList<Comment> getCommentsAfter(String commentID) {
        ArrayList<Comment> allComments = this.getAllComments();
        ArrayList<Comment> newComments = new ArrayList<>();

        for (int i = 0; i < allComments.size(); i++) {
            if (allComments.get(i).getID().equals(commentID)) {
               newComments = new ArrayList<>(allComments.subList(0, i - 1));
            }
        }

        return newComments;
    }


}
