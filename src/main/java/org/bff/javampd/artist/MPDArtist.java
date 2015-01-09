package org.bff.javampd.artist;

import org.bff.javampd.MPDItem;

/**
 * String represents an artist.
 *
 * @author Bill
 */
public class MPDArtist extends MPDItem {

    /**
     * Creates a String object
     *
     * @param name the name of the artist
     */
    public MPDArtist(String name) {
        setName(name);
    }
}
