/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd.objects;

import java.util.Collection;

/**
 * MPDSavedPlaylist represents a saved playlist.
 *
 * @author Bill Findeisen
 */
public class MPDSavedPlaylist extends MPDItem {
    private Collection<MPDSong> songs;

    /**
     * Creates a MPDSavedPlaylist object
     *
     * @param name the name of the saved playlist
     */
    public MPDSavedPlaylist(String name) {
        super(name);
    }

    /**
     * Returns the list of {@link MPDSong}s for the playlist
     *
     * @return a {@link Collection} of {@link MPDSong}s
     */
    public Collection<MPDSong> getSongs() {
        return songs;
    }

    /**
     * Sets the {@link MPDSong}s for the playlist
     *
     * @param songs the {@link Collection} of {@link MPDSong}s
     */
    public void setSongs(Collection<MPDSong> songs) {
        this.songs = songs;
    }
}
