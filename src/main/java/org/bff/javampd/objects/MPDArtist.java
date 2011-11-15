/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd.objects;

/**
 * MPDArtist represents an artist.
 *
 * @author Bill Findeisen
 */
public class MPDArtist extends MPDItem {

    /**
     * Creates a MPDArtist object
     *
     * @param name the name of the artist
     */
    public MPDArtist(String name) {
        setName(name);
    }
}
