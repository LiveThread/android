package guru.nickthompson.livethread;

import java.util.Comparator;

import guru.nickthompson.redditapi.Comment;

/**
 * Created by nick on 9/30/17.
 */

public class SortCommentsByTime implements Comparator<Comment> {
    @Override
    public int compare(Comment c1, Comment c2) {

        long c1time = c1.getTimeStamp().getTime();
        long c2time = c2.getTimeStamp().getTime();

        return Long.compare(c2time, c1time);
    }
}
