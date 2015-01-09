package org.bff.javampd.genre;

import org.bff.javampd.MPDItem;

/**
 * MPDGenre represents a genre
 *
 * @author Bill
 */
public class MPDGenre extends MPDItem {

    /**
     * Creates a MPDGenre object
     *
     * @param name the name of the genre
     */
    public MPDGenre(String name) {
        setName(name);
    }
}
