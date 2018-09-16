package org.bff.javampd.playlist;

import org.bff.javampd.song.MPDSong;

import java.util.Collection;

/**
 * Database for playlist related items
 *
 * @author bill
 */
public interface PlaylistDatabase {

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.playlist.MPDSavedPlaylist}s of all
     * saved playlists.  This is an expensive call so use it cautiously.
     *
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.playlist.MPDSavedPlaylist}s
     */
    Collection<MPDSavedPlaylist> listSavedPlaylists();

    /**
     * Returns a {@link java.util.Collection} of all available playlist names on the server.
     *
     * @return a list of playlist names
     */
    Collection<String> listPlaylists();

    Collection<MPDSong> listPlaylistSongs(String playlistName);

    int countPlaylistSongs(String playlistName);
}
