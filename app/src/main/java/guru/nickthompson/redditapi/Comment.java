package guru.nickthompson.redditapi;

import java.util.Date;
import java.util.Objects;

/**
 * Represents a comment in a Reddit {@link Post}.
 */
public class Comment {
    private final Post post;
    private final String id;
    private final String username;
    private final Date timestamp;
    private final String body;

    /**
     * Creates a comment.
     *
     * @param post the {@link Post} this comment is in.
     * @param id the ID of the comment.
     * @param username the username of the user that posted the comment.
     * @param timestamp the time the comment was originally posted.
     * @param body the content of the comment.
     */
    public Comment(Post post, String id, String username, Date timestamp, String body) {
        this.post = post;
        this.id = id;
        this.username = username;
        this.timestamp = timestamp;
        this.body = body;
    }

    /**
     * @return the ID of this comment.
     */
    public String getID() {
        return this.id;
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

    @Override
    public int hashCode() {
        return Objects.hashCode(this.id);
    }

    /**
     * Comments are equal if their string ID is equal.
     *
     * @param o another Object
     * @return if this comment is equal to the other object
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Comment)) {
            return false;
        } else {
            Comment c = (Comment) o;
            return this.id.equals(c.getID());
        }
    }
}
