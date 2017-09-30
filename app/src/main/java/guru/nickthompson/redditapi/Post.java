package guru.nickthompson.redditapi;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * Represents a post in a Reddit subreddit.
 */
public class Post implements Serializable {
    private final String id;
    private final String title;
    private final int score;
    private final int numComments;
    private final Date created;
    private final String author;

    /**
     * Creates a new post based off of a String ID.
     *
     * @param id          the Reddit ID of the post.
     * @param title       the title of the post.
     * @param score       the overall score of the post.
     * @param numComments the number of comments in a post.
     * @param created     the date and time the post was created.
     * @param author      the username of the author of the post.
     */
    public Post(String id, String title, int score, int numComments, Date created, String author) {
        this.id = id;
        this.title = title;
        this.score = score;
        this.numComments = numComments;
        this.created = created;
        this.author = author;
    }

    /**
     * @return the id of this post.
     */
    public String getID() {
        return this.id;
    }

    /**
     * @return the title of the post.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return the current overall score of the post.
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the number of comments in the post.
     */
    public int getNumComments() {
        return numComments;
    }

    /**
     * @return the date the post was submitted.
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @return the username of the author of the post.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Gets all comments from the post, sorted newest first.
     *
     * @return an ArrayList of Comments sorted by newest first.
     */
    public ArrayList<Comment> getAllComments() {
        ArrayList<Comment> comments = new ArrayList<>();

        try {
            JsonArray rawData = new JsonParser().parse(UrlUtils.readUrlID(this.id)).getAsJsonArray();

            JsonObject postListing = rawData.get(0).getAsJsonObject();
            JsonObject commentListing = rawData.get(1).getAsJsonObject();

            JsonArray commentChildren = commentListing.get("data").getAsJsonObject().getAsJsonArray("children");

            for (JsonElement t1 : commentChildren) {
                if (t1.getAsJsonObject().get("kind").getAsString().equals("t1")) {
                    JsonObject commentData = t1.getAsJsonObject().getAsJsonObject("data");

                    if (!commentData.get("stickied").getAsBoolean()) {
                        String commentID = commentData.get("id").getAsString();
                        String username = commentData.get("author").getAsString();
                        Date timestamp = new Date(commentData.get("created_utc").getAsLong() * 1000);
                        String body = commentData.get("body_html").getAsString();

                        comments.add(new Comment(this, commentID, username, timestamp, body));
                    }
                }
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

//        String comments = "";
//        for (Comment c : allComments) {
//            comments += "\nID: " + c.getID() + "   User: " + c.getUsername() + "   Body: " + Html.fromHtml(c.getBody()).toString();
//        }
//        Log.d("getCommentAfter", comments);

//        String newDebugComments = "";
        for (int i = 0; i < allComments.size(); i++) {
            if (allComments.get(i).getID().equals(commentID)) {
//                newDebugComments += "\nMATCHES: ID: " + allComments.get(i).getID() + "   Body: " + Html.fromHtml(allComments.get(i).getBody()).toString() + "NEW COMMENTS:\n\n";
                newComments = new ArrayList<>(allComments.subList(0, i));
            }
        }

//        for (Comment c : newComments) {
//            newDebugComments += "\nNEW COMMENT ID: " + c.getID() + "   User: " + c.getUsername() + "   Body: " + Html.fromHtml(c.getBody()).toString();
//        }
//
//        Log.d("getCommentAfterNew",  newDebugComments);

        return newComments;
    }
}
