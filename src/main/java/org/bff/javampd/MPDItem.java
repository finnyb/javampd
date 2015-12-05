package org.bff.javampd;

/**
 * Abstract base class for all MPD related objects.
 *
 * @author Bill
 */
public abstract class MPDItem implements Comparable<MPDItem> {

    private String name;

    /**
     * Default constructor for a MPDItem
     */
    public MPDItem() {
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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MPDItem mpdItem = (MPDItem) o;

        return !(name != null ? !name.equals(mpdItem.name) : mpdItem.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(MPDItem item) {
        return this.getName().compareTo(item.getName());
    }
}
