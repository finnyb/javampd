package org.bff.javampd.playlist;

import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.song.MPDSong;

import java.util.Collection;

/**
 * Database for playlist related items
 *
 * @author bill
 */
public interface PlaylistDatabase {

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.playlist.MPDSavedPlaylist}s of all saved playlists.  This is an expensive
     * call so use it cautiously.
     *
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.playlist.MPDSavedPlaylist}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of all available playlist names on the server.
     *
     * @return a list of playlist names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listPlaylists() throws MPDDatabaseException;

    Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDDatabaseException;
}
