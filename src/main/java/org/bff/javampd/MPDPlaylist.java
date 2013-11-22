package org.bff.javampd;

import org.bff.javampd.events.PlaylistChangeEvent;
import org.bff.javampd.events.PlaylistChangeListener;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.*;
import org.bff.javampd.properties.PlaylistProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDPlaylist represents a playlist controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlaylist</code> method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDPlaylist extends CommandExecutor {

    private int oldVersion = -1;
    private int version = -1;
    private MPDDatabase database;
    private MPDServerStatus serverStatus;
    private List<PlaylistChangeListener> listeners;
    private PlaylistProperties playlistProperties;

    /**
     * Creates a new instance of MPDPlaylist
     *
     * @param mpd the MPD connection
     */
    MPDPlaylist(MPD mpd) {
        super(mpd);
        this.listeners = new ArrayList<PlaylistChangeListener>();
        this.database = mpd.getMPDDatabase();
        this.serverStatus = mpd.getMPDServerStatus();
        this.playlistProperties = new PlaylistProperties();
    }

    /**
     * Adds a {@link PlaylistChangeListener} to this object to receive
     * {@link PlaylistChangeEvent}s.
     *
     * @param pcl the PlaylistChangeListener to add
     */
    public synchronized void addPlaylistChangeListener(PlaylistChangeListener pcl) {
        listeners.add(pcl);
    }

    /**
     * Removes a {@link PlaylistChangeListener} from this object.
     *
     * @param pcl the PlaylistChangeListener to remove
     */
    public synchronized void removePlaylistStatusChangedListener(PlaylistChangeListener pcl) {
        listeners.remove(pcl);
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param event the {@link PlaylistChangeEvent.Event} to send
     */
    protected synchronized void firePlaylistChangeEvent(PlaylistChangeEvent.Event event) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, event);

        for (PlaylistChangeListener pcl : listeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param event the {@link PlaylistChangeEvent.Event} to send
     * @param msg   the message for the event
     */
    protected synchronized void firePlaylistChangeEvent(PlaylistChangeEvent.Event event, String msg) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, event, msg);

        for (PlaylistChangeListener pcl : listeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Loads the songs in the given playlist to the current playlist.  The playlist
     * name can be givin with or without the .m3u extension.
     *
     * @param playlistName the playlist name
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void loadPlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        String name = playlistName;
        if (name.endsWith(".m3u")) {
            name = name.substring(name.length() - 4);
        }

        try {
            sendMPDCommand(playlistProperties.getLoad(), name);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    /**
     * Adds a {@link MPDSong} to the playlist and fires a {@link PlaylistChangeEvent} for event listeners
     *
     * @param song the song to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void addSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException {
        addSong(song, true);
    }

    /**
     * Adds a {@link MPDSong} to the playlist.
     *
     * @param song      the song to add
     * @param fireEvent whether to fire song added event for the event listeners
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void addSong(MPDSong song, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException {
        try {
            sendMPDCommand(playlistProperties.getAdd(), song.getFile());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();

        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_ADDED, song.getName());
        }
    }

    /**
     * Adds a <CODE>List</CODE> of {@link MPDSong}s to the playlist.
     *
     * @param songList the list of songs to add
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public boolean addSongs(List<MPDSong> songList) throws MPDConnectionException, MPDPlaylistException {
        return addSongs(songList, true);
    }

    /**
     * Adds a <CODE>List</CODE> of {@link MPDSong}s to the playlist.
     *
     * @param songList  the list of songs to add
     * @param fireEvent true if a playlist event should be fired after adding
     * @return true if the songs are added successfully; false otherwise
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public boolean addSongs(List<MPDSong> songList, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException {
        List<MPDCommand> commandList = new ArrayList<MPDCommand>();
        for (MPDSong song : songList) {
            commandList.add(new MPDCommand(playlistProperties.getAdd(), song.getFile()));
        }

        try {
            sendMPDCommands(commandList);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        int oldCount = songList.size();
        updatePlaylist();

        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.MULTIPLE_SONGS_ADDED, Integer.toString(songList.size()));
        }

        if (oldCount < songList.size()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds a directory of songs to the playlist.
     *
     * @param file the directory to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void addFileOrDirectory(MPDFile file) throws MPDConnectionException, MPDPlaylistException {
        try {
            sendMPDCommand(playlistProperties.getAdd(), file.getPath());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();

        firePlaylistChangeEvent(PlaylistChangeEvent.Event.FILE_ADDED, file.getName());

    }

    /**
     * Removes a {@link MPDSong} from the playlist.
     *
     * @param song the song to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void removeSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException {
        try {
            if (song.getId() > -1) {
                sendMPDCommand(playlistProperties.getRemoveId(), song.getId());

            } else if (song.getPosition() > -1) {
                sendMPDCommand(playlistProperties.getRemove(), song.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    /**
     * Returns the current song.
     *
     * @return the current song
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public MPDSong getCurrentSong() throws MPDConnectionException, MPDPlaylistException {
        try {
            List<MPDSong> songs = convertResponseToSong(sendMPDCommand(playlistProperties.getCurrentSong()));
            return songs.isEmpty() ? null : songs.get(0);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    private List<MPDSong> convertResponseToSong(List<String> response) {
        return MPDSongConverter.convertResponseToSong(response);
    }

    /**
     * Removes all songs from the playlist.
     *
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void clearPlaylist() throws MPDConnectionException, MPDPlaylistException {
        try {
            sendMPDCommand(playlistProperties.getClear());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    /**
     * Deletes a {@link MPDSavedPlaylist}
     *
     * @param playlist the {@link MPDSavedPlaylist}
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void deletePlaylist(MPDSavedPlaylist playlist) throws MPDConnectionException, MPDPlaylistException {
        deletePlaylist(playlist.getName());
    }

    /**
     * Deletes the playlist from the MPD server.
     *
     * @param playlistName the playlist to delete
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void deletePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        try {
            sendMPDCommand(playlistProperties.getDelete(), playlistName);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_DELETED);
    }

    /**
     * Moves the desired song to the given position in the playlist.
     *
     * @param song the song to move
     * @param to   the position to move the song to
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void move(MPDSong song, int to) throws MPDConnectionException, MPDPlaylistException {
        String[] paramList = new String[2];
        try {
            paramList[1] = Integer.toString(to);
            if (song.getId() > -1) {
                sendMPDCommand(playlistProperties.getMoveId(), song.getId());
            } else if (song.getPosition() > -1) {
                sendMPDCommand(playlistProperties.getMoveId(), song.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    /**
     * Shuffles the songs in the playlist.
     *
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void shuffle() throws MPDConnectionException, MPDPlaylistException {
        try {
            sendMPDCommand(playlistProperties.getShuffle());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    /**
     * Swaps the given two songs in the playlist.
     *
     * @param song1 first song to swap
     * @param song2 second song to swap
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void swap(MPDSong song1, MPDSong song2) throws MPDConnectionException, MPDPlaylistException {
        try {
            if (song1.getId() > -1 && song2.getId() > -1) {
                sendMPDCommand(playlistProperties.getSwapId(), song1.getId(), song2.getId());
            } else if (song1.getPosition() > -1 && song2.getPosition() > -1) {
                sendMPDCommand(playlistProperties.getSwap(), song1.getPosition(), song2.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        updatePlaylist();
    }

    /**
     * Saves the current playlist as the passed playlist name.
     *
     * @param playlistName the playlist name for the playlist
     * @return true if the playlist is saved; otherwise false
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public boolean savePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        if (playlistName != null) {
            try {
                sendMPDCommand(playlistProperties.getSave(), playlistName);
                firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_SAVED);
            } catch (MPDResponseException re) {
                throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
            } catch (Exception e) {
                throw new MPDPlaylistException(e);
            }
            return true;
        } else {
            throw new MPDPlaylistException("Playlist name hasn't been set!");
        }
    }

    private void updatePlaylist() throws MPDConnectionException, MPDPlaylistException {
        setVersion(getPlaylistVersion());

        if (getPlaylistVersion() != oldVersion) {
            oldVersion = getVersion();
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_CHANGED);
        }
    }

    private int getPlaylistVersion() throws MPDConnectionException, MPDPlaylistException {
        //TODO playlist returning null
        try {
            return serverStatus.getPlaylistVersion();
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Returns the list of songs in the playlist.
     *
     * @return the list of songs
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    private List<MPDSong> listSongs() throws MPDConnectionException, MPDPlaylistException {
        try {
            return convertResponseToSong(sendMPDCommand(playlistProperties.getInfo()));
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a {@link MPDAlbum} by a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} for the album to add
     * @param album  the {@link MPDAlbum} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void insertAlbum(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbumByArtist(artist, album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a {@link MPDAlbum} to the playlist.
     *
     * @param album the {@link MPDAlbum} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void insertAlbum(MPDAlbum album) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbum(album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Removes a {@link MPDAlbum} by a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} for the album to remove
     * @param album  the {@link MPDAlbum} to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void removeAlbum(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDPlaylistException {
        List<MPDSong> removeList = new ArrayList<MPDSong>();

        for (MPDSong song : getSongList()) {
            if (song.getArtist().equals(artist) && song.getAlbum().equals(album)) {
                removeList.add(song);
            }
        }

        for (MPDSong song : removeList) {
            removeSong(song);
        }
    }

    /**
     * Adds a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void insertArtist(MPDArtist artist) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findArtist(artist)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED, artist.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a {@link MPDGenre} to the playlist.
     *
     * @param genre the {@link MPDGenre} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void insertGenre(MPDGenre genre) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findGenre(genre)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.GENRE_ADDED, genre.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a year to the playlist.
     *
     * @param year the {@link MPDGenre} to add
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void insertYear(String year) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findYear(year)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.Event.YEAR_ADDED, year);

        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Removes a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} to remove
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public void removeArtist(MPDArtist artist) throws MPDConnectionException, MPDPlaylistException {
        List<MPDSong> removeList = new ArrayList<MPDSong>();
        for (MPDSong song : getSongList()) {
            if (song.getArtist().equals(artist)) {
                removeList.add(song);
            }
            removeList.add(song);
        }

        for (MPDSong song : removeList) {
            removeSong(song);
        }
    }

    /**
     * @return the database
     */
    public MPDDatabase getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(MPDDatabase database) {
        this.database = database;
    }

    /**
     * Returns the playlist version.
     *
     * @return the playlist version
     */
    public int getVersion() {
        return version;
    }

    /**
     * Sets the playlist version.
     *
     * @param version the mpd version
     */
    private void setVersion(int version) {
        this.version = version;
    }

    /**
     * Returns the list of songs in the playlist.  This does query the MPD server for the list so
     * care should be taken not to call it excessively.
     *
     * @return the song list
     * @throws MPDPlaylistException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public List<MPDSong> getSongList() throws MPDPlaylistException, MPDConnectionException {
        return listSongs();
    }

    /**
     * Returns the string representation of this playlist.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Version:").append(getVersion()).append("\n");
        return sb.toString();
    }

    public void swap(MPDSong song, int i) throws MPDConnectionException, MPDPlaylistException {
        String[] paramList = new String[2];

        try {
            sendMPDCommand(playlistProperties.getSwapId(), song.getId(), i);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        updatePlaylist();
    }
}
