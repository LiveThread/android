package guru.nickthompson.livethread;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by williamreed on 9/29/17.
 * <p>
 * Represents a post comment. Dead simple for now.
 */

public class PostComment {
    private String username;
    private Date timestamp;
    private String content;

    public PostComment(String username, Date timestamp, String content) {
        this.username = username;
        this.timestamp = timestamp;
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public Date getTimeStamp() {
        return timestamp;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return "Comment by " + username + " on " + timestamp.toString() + " saying: " + content;
    }

    /**
     * Temporary for testing. Creates dummy comments. contains html tests too.
     *
     * @param numComments amount you want
     * @return all of the dummy comments.
     */
    public static ArrayList<PostComment> testComments(int numComments) {
        ArrayList<PostComment> comments = new ArrayList<>(numComments);

        for (int i = 0; i < numComments; i++) {
            comments.add(new PostComment("username", new Date(),
                    "comment <b>comment</b> comment comment <i>comment</i> comment comment"));
        }

        return comments;
    }
}
