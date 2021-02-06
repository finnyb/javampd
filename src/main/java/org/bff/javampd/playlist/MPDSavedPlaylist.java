package org.bff.javampd.playlist;

import lombok.EqualsAndHashCode;
import org.bff.javampd.MPDItem;
import org.bff.javampd.song.MPDSong;

import java.util.Collection;

/**
 * MPDSavedPlaylist represents a saved playlist.
 *
 * @author Bill
 */
@EqualsAndHashCode(callSuper = true)
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
