package guru.nickthompson.redditapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a post in a Reddit subreddit.
 */
public class Post {
    private final String postID;

    /**
     * Creates a new post based off of a String ID.
     *
     * @param postID the Reddit ID of the post.
     */
    public Post(String postID) {
        this.postID = postID;
    }

    /**
     * @return the ID of this Post.
     */
    public String getID() {
        return this.postID;
    }

    /**
     * Gets all comments from the post, sorted newest first.
     *
     * @return an ArrayList of Comments sorted by newest first.
     */
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

    /**
     * Gets comments from the post only after the provided comment ID.
     *
     * @param commentID the unique Reddit ID of the comment in a post.
     * @return an ArrayList of Comments newer than the given comment ID.
     */
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
