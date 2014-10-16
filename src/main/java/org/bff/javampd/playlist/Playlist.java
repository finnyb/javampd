package org.bff.javampd.playlist;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.file.MPDFile;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.song.MPDSong;

import java.util.List;

/**
 * @author bill
 */
public interface Playlist {
    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    void addPlaylistChangeListener(PlaylistChangeListener pcl);

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    void removePlaylistStatusChangedListener(PlaylistChangeListener pcl);

    /**
     * Loads the songs in the given playlist to the current playlist.  The playlist
     * name can be givin with or without the .m3u extension.
     *
     * @param playlistName the playlist name
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void loadPlaylist(String playlistName) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist and fires a {@link PlaylistChangeEvent} for event listeners
     *
     * @param song the song to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void addSong(MPDSong song) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist.
     *
     * @param song      the song to add
     * @param fireEvent whether to fire song added event for the event listeners
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void addSong(MPDSong song, boolean fireEvent) throws MPDPlaylistException;

    /**
     * Adds a <CODE>List</CODE> of {@link org.bff.javampd.song.MPDSong}s to the playlist.
     *
     * @param songList the list of songs to add
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    boolean addSongs(List<MPDSong> songList) throws MPDPlaylistException;

    /**
     * Adds a <CODE>List</CODE> of {@link org.bff.javampd.song.MPDSong}s to the playlist.
     *
     * @param songList  the list of songs to add
     * @param fireEvent true if a playlist event should be fired after adding
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    boolean addSongs(List<MPDSong> songList, boolean fireEvent) throws MPDPlaylistException;

    /**
     * Adds a directory of songs to the playlist.
     *
     * @param file the directory to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void addFileOrDirectory(MPDFile file) throws MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.song.MPDSong} from the playlist.
     *
     * @param song the song to remove
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void removeSong(MPDSong song) throws MPDPlaylistException;

    /**
     * Returns the current song.
     *
     * @return the current song
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    MPDSong getCurrentSong() throws MPDPlaylistException;

    /**
     * Removes all songs from the playlist.
     *
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void clearPlaylist() throws MPDPlaylistException;

    /**
     * Deletes a {@link org.bff.javampd.playlist.MPDSavedPlaylist}
     *
     * @param playlist the {@link org.bff.javampd.playlist.MPDSavedPlaylist}
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void deletePlaylist(MPDSavedPlaylist playlist) throws MPDPlaylistException;

    /**
     * Deletes the playlist from the MPD server.
     *
     * @param playlistName the playlist to delete
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void deletePlaylist(String playlistName) throws MPDPlaylistException;

    /**
     * Moves the desired song to the given position in the playlist.
     *
     * @param song the song to move
     * @param to   the position to move the song to
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void move(MPDSong song, int to) throws MPDPlaylistException;

    /**
     * Shuffles the songs in the playlist.
     *
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void shuffle() throws MPDPlaylistException;

    /**
     * Swaps the given two songs in the playlist.
     *
     * @param song1 first song to swap
     * @param song2 second song to swap
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void swap(MPDSong song1, MPDSong song2) throws MPDPlaylistException;

    /**
     * Saves the current playlist as the passed playlist name.
     *
     * @param playlistName the playlist name for the playlist
     * @return true if the playlist is saved; otherwise false
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    boolean savePlaylist(String playlistName) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.album.MPDAlbum} by a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.artist.MPDArtist} for the album to add
     * @param album  the {@link org.bff.javampd.album.MPDAlbum} to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertAlbum(MPDArtist artist, MPDAlbum album) throws MPDPlaylistException;

    /**
     * Adds a album by a artist to the playlist.
     *
     * @param artistName the album's artist
     * @param albumName  the album name
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertAlbum(String artistName, String albumName) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.album.MPDAlbum} to the playlist.
     *
     * @param album the {@link org.bff.javampd.album.MPDAlbum} to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertAlbum(MPDAlbum album) throws MPDPlaylistException;

    /**
     * Adds a album to the playlist.
     *
     * @param albumName the album to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertAlbum(String albumName) throws MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.album.MPDAlbum} by a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.artist.MPDArtist} for the album to remove
     * @param album  the {@link org.bff.javampd.album.MPDAlbum} to remove
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void removeAlbum(MPDArtist artist, MPDAlbum album) throws MPDPlaylistException;

    /**
     * Removes a album by a artist to the playlist.
     *
     * @param artistName the artist for the album to remove
     * @param albumName  the album to remove
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void removeAlbum(String artistName, String albumName) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.artist.MPDArtist} to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertArtist(MPDArtist artist) throws MPDPlaylistException;

    /**
     * Adds a artist to the playlist.
     *
     * @param artistName the artist to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertArtist(String artistName) throws MPDPlaylistException;

    /**
     * Adds a {@link org.bff.javampd.genre.MPDGenre} to the playlist.
     *
     * @param genre the {@link org.bff.javampd.genre.MPDGenre} to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertGenre(MPDGenre genre) throws MPDPlaylistException;

    /**
     * Adds a genre to the playlist.
     *
     * @param genreName the genre to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertGenre(String genreName) throws MPDPlaylistException;

    /**
     * Adds a year to the playlist.
     *
     * @param year the {@link org.bff.javampd.genre.MPDGenre} to add
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void insertYear(String year) throws MPDPlaylistException;

    /**
     * Removes a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
     *
     * @param artist the {@link org.bff.javampd.artist.MPDArtist} to remove
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void removeArtist(MPDArtist artist) throws MPDPlaylistException;

    /**
     * Removes a artist to the playlist.
     *
     * @param artistName the artist to remove
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    void removeArtist(String artistName) throws MPDPlaylistException;

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
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    List<MPDSong> getSongList() throws MPDPlaylistException;

    /**
     * Returns the string representation of this playlist.
     *
     * @return the string representation
     */
    @Override
    String toString();

    void swap(MPDSong song, int i) throws MPDPlaylistException;
}
