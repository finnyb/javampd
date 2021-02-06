package org.bff.javampd;

import lombok.EqualsAndHashCode;

/**
 * Abstract base class for all MPD related objects.
 *
 * @author Bill
 */
@EqualsAndHashCode
public abstract class MPDItem implements Comparable<MPDItem> {

    private String name;

    /**
     * Default constructor for a MPDItem
     */
    protected MPDItem() {
    }

    /**
     * Constructor for a MPDItem
     *
     * @param name the name of the item
     */
    protected MPDItem(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the item.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the item.
     *
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int compareTo(MPDItem item) {
        return this.getName().compareTo(item.getName());
    }
}
