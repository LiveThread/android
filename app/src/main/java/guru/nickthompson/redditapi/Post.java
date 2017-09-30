package guru.nickthompson.redditapi;

/**
 * Created by nick on 9/29/17.
 */

public class Post {
    private final String id;

    public Post(String id) {
        this.id = id;
    }

    public String getID() {
        return this.id;
    }
}
