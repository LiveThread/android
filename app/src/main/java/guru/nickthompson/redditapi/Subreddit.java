package guru.nickthompson.redditapi;

/**
 * Created by williamreed on 9/30/17.
 * <p>
 * Represent information for a subreddit to show in the suggest subreddits. Minimal information needed.
 */

public class Subreddit {
    private final String name;
    private final String description;

    public Subreddit(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
