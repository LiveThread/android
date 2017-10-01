package guru.nickthompson.livethread;

import java.util.Comparator;
import java.util.TreeSet;

/**
 * Created by nick on 9/30/17.
 */

public class TreeSetIndex<E> extends TreeSet<E> {


    public TreeSetIndex(Comparator<? super E> comparator) {
        super(comparator);
    }

    public int insert(E toInsert) {
        if (this.contains(toInsert)) {
            throw new IllegalArgumentException();
        }

        this.add(toInsert);

        int i = 0;

        for (E e : this) {
            if (e == toInsert) {
                return i;
            }
            i++;
        }

        return -1;
    }

    public E get(int n) {
        if (n < 0 || n >= this.size()) {
            throw new IllegalArgumentException();
        }

        int i = 0;

        for (E e : this) {
            if (i == n) {
                return e;
            }
            i++;
        }

        // THIS SHOULD NEVER HAPPEN
        return null;
    }


}
