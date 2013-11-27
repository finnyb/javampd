package org.bff.javampd;

import org.bff.javampd.events.PlaylistChangeListener;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.objects.*;

import java.util.List;

/**
 * @author bill
 * @since: 11/24/13 10:21 AM
 */
public interface Playlist {
    /**
     * Adds a {@link org.bff.javampd.events.PlaylistChangeListener} to this object to receive
     * {@link org.bff.javampd.events.PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addPlaylistChangeListener(PlaylistChangeListener pcl);

    /**
     * Removes a {@link org.bff.javampd.events.PlaylistChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    void removePlaylistStatusChangedListener(PlaylistChangeListener pcl);

    /**
     * Loads the songs in the given playlist to the current playlist.  The playlist
     * name can be givin with or without the .m3u extension.
     *
     * @param playlistName the playlist name
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void loadPlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDSong} to the playlist and fires a {@link org.bff.javampd.events.PlaylistChangeEvent} for event listeners
     *
     * @param song the song to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void addSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDSong} to the playlist.
     *
     * @param song      the song to add
     * @param fireEvent whether to fire song added event for the event listeners
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void addSong(MPDSong song, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a <CODE>List</CODE> of {@link org.bff.javampd.objects.MPDSong}s to the playlist.
     *
     * @param songList the list of songs to add
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    boolean addSongs(List<MPDSong> songList) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a <CODE>List</CODE> of {@link org.bff.javampd.objects.MPDSong}s to the playlist.
     *
     * @param songList  the list of songs to add
     * @param fireEvent true if a playlist event should be fired after adding
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    boolean addSongs(List<MPDSong> songList, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a directory of songs to the playlist.
     *
     * @param file the directory to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void addFileOrDirectory(MPDFile file) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.objects.MPDSong} from the playlist.
     *
     * @param song the song to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void removeSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Returns the current song.
     *
     * @return the current song
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    MPDSong getCurrentSong() throws MPDConnectionException, MPDPlaylistException;

    /**
     * Removes all songs from the playlist.
     *
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void clearPlaylist() throws MPDConnectionException, MPDPlaylistException;

    /**
     * Deletes a {@link org.bff.javampd.objects.MPDSavedPlaylist}
     *
     * @param playlist the {@link org.bff.javampd.objects.MPDSavedPlaylist}
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void deletePlaylist(MPDSavedPlaylist playlist) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Deletes the playlist from the MPD server.
     *
     * @param playlistName the playlist to delete
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void deletePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Moves the desired song to the given position in the playlist.
     *
     * @param song the song to move
     * @param to   the position to move the song to
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void move(MPDSong song, int to) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Shuffles the songs in the playlist.
     *
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void shuffle() throws MPDConnectionException, MPDPlaylistException;

    /**
     * Swaps the given two songs in the playlist.
     *
     * @param song1 first song to swap
     * @param song2 second song to swap
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void swap(MPDSong song1, MPDSong song2) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Saves the current playlist as the passed playlist name.
     *
     * @param playlistName the playlist name for the playlist
     * @return true if the playlist is saved; otherwise false
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    boolean savePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDAlbum} by a {@link org.bff.javampd.objects.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.objects.MPDArtist} for the album to add
     * @param album  the {@link org.bff.javampd.objects.MPDAlbum} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void insertAlbum(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDAlbum} to the playlist.
     *
     * @param album the {@link org.bff.javampd.objects.MPDAlbum} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void insertAlbum(MPDAlbum album) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.objects.MPDAlbum} by a {@link org.bff.javampd.objects.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.objects.MPDArtist} for the album to remove
     * @param album  the {@link org.bff.javampd.objects.MPDAlbum} to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void removeAlbum(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.objects.MPDArtist} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void insertArtist(MPDArtist artist) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.objects.MPDGenre} to the playlist.
     *
     * @param genre the {@link org.bff.javampd.objects.MPDGenre} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void insertGenre(MPDGenre genre) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Adds a year to the playlist.
     *
     * @param year the {@link org.bff.javampd.objects.MPDGenre} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void insertYear(String year) throws MPDConnectionException, MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.objects.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.objects.MPDArtist} to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    void removeArtist(MPDArtist artist) throws MPDConnectionException, MPDPlaylistException;

    /**
     * @return the database
     */
    Database getDatabase();

    /**
     * @param database the database to set
     */
    void setDatabase(Database database);

    /**
     * Returns the playlist version.
     *
     * @return the playlist version
     */
    int getVersion();

    /**
     * Returns the list of songs in the playlist.  This does query the MPD server for the list so
     * care should be taken not to call it excessively.
     *
     * @return the song list
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    List<MPDSong> getSongList() throws MPDPlaylistException, MPDConnectionException;

    /**
     * Returns the string representation of this playlist.
     *
     * @return the string representation
     */
    @Override
    String toString();

    void swap(MPDSong song, int i) throws MPDConnectionException, MPDPlaylistException;
}
