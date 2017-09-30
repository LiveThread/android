package guru.nickthompson.redditapi;

import java.util.Date;

/**
 * Represents a comment in a Reddit {@link Post}.
 */
public class Comment {
    private final Post post;
    private final String commentID;
    private final String username;
    private final Date timestamp;
    private final String body;

    /**
     * Creates a comment.
     *
     * @param post the {@link Post} this comment is in.
     * @param commentID the ID of the comment.
     * @param username the username of the user that posted the comment.
     * @param timestamp the time the comment was originally posted.
     * @param body the content of the comment.
     */
    public Comment(Post post, String commentID, String username, Date timestamp, String body) {
        this.post = post;
        this.commentID = commentID;
        this.username = username;
        this.timestamp = timestamp;
        this.body = body;
    }

    /**
     * @return the ID of this comment.
     */
    public String getID() {
        return this.commentID;
    }

    /**
     * @return the username of the user that posted the comment.
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the time that the comment was originally posted.
     */
    public Date getTimeStamp() {
        return this.timestamp;
    }

    /**
     * @return the body of this comment.
     */
    public String getBody() {
        return this.body;
    }
}
