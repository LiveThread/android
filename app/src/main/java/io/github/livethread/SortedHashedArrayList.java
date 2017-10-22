package io.github.livethread;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

/**
 * Keeps a sorted ArrayList with no duplicates.
 */
public class SortedHashedArrayList<T> {
    private HashSet<T> contents;
    private ArrayList<T> elements;
    private Comparator<T> sorter;

    /**
     * Creates a new SortedHashedArrayList.
     *
     * @param sorter the way that this SortedHashedArrayList should be sorted.
     */
    public SortedHashedArrayList(Comparator<T> sorter) {
        this.sorter = sorter;
        this.contents = new HashSet<T>();
        this.elements = new ArrayList<T>();
    }

    /**
     * @param o another object.
     * @return a boolean if this SortedHashedArrayList contains the given object.
     */
    public boolean contains(Object o) {
        return this.contents.contains(o);
    }

    /**
     * Inserts a new element T into the SortedHashedArrayList and returns it's index.
     *
     * @param elt the element T to be added.
     * @return the index that the element T was sorted into.
     */
    public int insert(T elt) {
        if (this.contents.add(elt)) {

            this.elements.add(elt);
            Collections.sort(this.elements, this.sorter);
            return this.elements.indexOf(elt);
        } else {
            throw new IllegalArgumentException("The element was already added.");
        }
    }

    /**
     * Gets the element T at the provided index.
     *
     * @param index the index to get the value from.
     * @return the T at the given index.
     */
    public T get(int index) {
        if (index < 0 || index >= this.elements.size()) {
            throw new IllegalArgumentException();
        }

        return this.elements.get(index);
    }

    /**
     * @return the size of this SortedHashedArrayList.
     */
    public int size() {
        return this.contents.size();
    }
}
