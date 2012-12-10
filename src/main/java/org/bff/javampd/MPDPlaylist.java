/*
 * MPDPlaylist.java
 *
 * Created on September 30, 2005, 5:51 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.events.PlaylistChangeEvent;
import org.bff.javampd.events.PlaylistChangeListener;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * MPDPlaylist represents a playlist controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlaylist</code> method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDPlaylist {

    private MPD mpd;
    private Properties prop;
    private int oldVersion = -1;
    private int version = -1;
    private MPDDatabase database;
    private List<PlaylistChangeListener> listeners;
    private static final String MPDPROPADD = "MPD_PLAYLIST_ADD";
    private static final String MPDPROPCLEAR = "MPD_PLAYLIST_CLEAR";
    private static final String MPDPROPCURRSONG = "MPD_PLAYLIST_CURRSONG";
    private static final String MPDPROPDELETE = "MPD_PLAYLIST_DELETE";
    private static final String MPDPROPCHANGES = "MPD_PLAYLIST_CHANGES";
    private static final String MPDPROPID = "MPD_PLAYLIST_LIST_ID";
    private static final String MPDPROPINFO = "MPD_PLAYLIST_LIST";
    private static final String MPDPROPLOAD = "MPD_PLAYLIST_LOAD";
    private static final String MPDPROPMOVE = "MPD_PLAYLIST_MOVE";
    private static final String MPDPROPMOVEID = "MPD_PLAYLIST_MOVE_ID";
    private static final String MPDPROPREMOVE = "MPD_PLAYLIST_REMOVE";
    private static final String MPDPROPREMOVEID = "MPD_PLAYLIST_REMOVE_ID";
    private static final String MPDPROPSAVE = "MPD_PLAYLIST_SAVE";
    private static final String MPDPROPSHUFFLE = "MPD_PLAYLIST_SHUFFLE";
    private static final String MPDPROPSWAP = "MPD_PLAYLIST_SWAP";
    private static final String MPDPROPSWAPID = "MPD_PLAYLIST_SWAP_ID";

    /**
     * Creates a new instance of MPDPlaylist
     *
     * @param mpd the MPD connection
     */
    MPDPlaylist(MPD mpd) {
        this.mpd = mpd;
        this.prop = mpd.getMPDProperties();
        this.listeners = new ArrayList<PlaylistChangeListener>();
        this.database = mpd.getMPDDatabase();
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
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id);

        for (PlaylistChangeListener pcl : listeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param id  the event id to send
     * @param msg the message for the event
     */
    protected synchronized void firePlaylistChangeEvent(int id, String msg) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id, msg);

        for (PlaylistChangeListener pcl : listeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Loads the songs in the given playlist to the current playlist.  The playlist
     * name can be givin with or without the .m3u extension.
     *
     * @param playlistName the playlist name
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void loadPlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        if (playlistName.endsWith(".m3u")) {
            playlistName = playlistName.substring(playlistName.length() - 4);
        }

        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLOAD), playlistName);

        try {
            mpd.sendMPDCommand(command);
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void addSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException {
        addSong(song, true);
    }

    /**
     * Adds a {@link MPDSong} to the playlist.
     *
     * @param song      the song to add
     * @param fireEvent whether to fire song added event for the event listeners
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void addSong(MPDSong song, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPADD), song.getFile());
        try {
            mpd.sendMPDCommand(command);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();

        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.SONG_ADDED, song.getName());
        }
    }

    /**
     * Adds a <CODE>List</CODE> of {@link MPDSong}s to the playlist.
     *
     * @param songList the list of songs to add
     * @return true if the songs are added successfully; false otherwise
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public boolean addSongs(List<MPDSong> songList, boolean fireEvent) throws MPDConnectionException, MPDPlaylistException {
        List<MPDCommand> commandList = new ArrayList<MPDCommand>();
        for (MPDSong song : songList) {
            commandList.add(new MPDCommand(prop.getProperty(MPDPROPADD), song.getFile()));
        }
        try {
            mpd.sendMPDCommands(commandList);

        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        int oldCount = songList.size();
        updatePlaylist();

        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.MULTIPLE_SONGS_ADDED, Integer.toString(songList.size()));
        }

        if (oldCount < songList.size()) {
            return (true);
        } else {
            return (false);
        }
    }

    /**
     * Adds a directory of songs to the playlist.
     *
     * @param file the directory to add
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void addFileOrDirectory(MPDFile file) throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPADD), file.getPath());
        try {
            mpd.sendMPDCommand(command);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();

        firePlaylistChangeEvent(PlaylistChangeEvent.FILE_ADDED, file.getName());

    }

    /**
     * Removes a {@link MPDSong} from the playlist.
     *
     * @param song the song to remove
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void removeSong(MPDSong song) throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command = null;

        if (song.getId() > -1) {
            command = new MPDCommand(prop.getProperty(MPDPROPREMOVEID), Integer.toString(song.getId()));

        } else if (song.getPosition() > -1) {
            command = new MPDCommand(prop.getProperty(MPDPROPREMOVE), Integer.toString(song.getPosition()));
        }
        try {
            mpd.sendMPDCommand(command);
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public MPDSong getCurrentSong() throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPCURRSONG));
        List<String> response;

        try {
            response = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        List<MPDSong> sl = new ArrayList<MPDSong>(mpd.convertResponseToSong(response));

        if (sl.isEmpty()) {
            return (null);
        } else {
            return (sl.get(0));
        }
    }

    /**
     * Removes all songs from the playlist.
     *
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void clearPlaylist() throws MPDConnectionException, MPDPlaylistException {
        try {
            mpd.sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPCLEAR)));
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void deletePlaylist(MPDSavedPlaylist playlist) throws MPDConnectionException, MPDPlaylistException {
        deletePlaylist(playlist.getName());
    }

    /**
     * Deletes the playlist from the MPD server.
     *
     * @param playlistName the playlist to delete
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void deletePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command =
                new MPDCommand(prop.getProperty(MPDPROPDELETE), playlistName);
        try {
            mpd.sendMPDCommand(command);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_DELETED);
    }

    /**
     * Moves the desired song to the given position in the playlist.
     *
     * @param song the song to move
     * @param to   the position to move the song to
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void move(MPDSong song, int to) throws MPDConnectionException, MPDPlaylistException {
        String[] paramList = new String[2];
        MPDCommand command = null;

        paramList[1] = Integer.toString(to);
        if (song.getId() > -1) {
            paramList[0] = Integer.toString(song.getId());
            command = new MPDCommand(prop.getProperty(MPDPROPMOVEID), paramList);
        } else if (song.getPosition() > -1) {
            paramList[0] = Integer.toString(song.getPosition());
            command = new MPDCommand(prop.getProperty(MPDPROPMOVEID), paramList);
        }
        try {
            mpd.sendMPDCommand(command);
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void shuffle() throws MPDConnectionException, MPDPlaylistException {
        try {
            mpd.sendMPDCommand(new MPDCommand(prop.getProperty(MPDPROPSHUFFLE)));
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void swap(MPDSong song1, MPDSong song2) throws MPDConnectionException, MPDPlaylistException {
        String[] paramList = new String[2];
        MPDCommand command = null;

        if (song1.getId() > -1 && song2.getId() > -1) {
            paramList[0] = Integer.toString(song1.getId());
            paramList[1] = Integer.toString(song2.getId());
            command = new MPDCommand(prop.getProperty(MPDPROPSWAPID), paramList);
        } else if (song1.getPosition() > -1 && song2.getPosition() > -1) {
            paramList[0] = Integer.toString(song1.getPosition());
            paramList[1] = Integer.toString(song2.getPosition());
            command = new MPDCommand(prop.getProperty(MPDPROPSWAP), paramList);
        }

        try {
            mpd.sendMPDCommand(command);
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public boolean savePlaylist(String playlistName) throws MPDConnectionException, MPDPlaylistException {
        if (playlistName != null) {
            MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPSAVE), playlistName);
            try {
                mpd.sendMPDCommand(command);
                firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_SAVED);
            } catch (MPDResponseException re) {
                throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
            } catch (Exception e) {
                throw new MPDPlaylistException(e);
            }
            return (true);
        } else {
            throw new MPDPlaylistException("Playlist name hasn't been set!");
        }
    }

    private void updatePlaylist() throws MPDConnectionException, MPDPlaylistException {
        setVersion(getPlaylistVersion());

        if (getPlaylistVersion() != oldVersion) {
            oldVersion = getVersion();
            firePlaylistChangeEvent(PlaylistChangeEvent.PLAYLIST_CHANGED);
        }
    }

    private int getPlaylistVersion() throws MPDConnectionException, MPDPlaylistException {
        //TODO playlist returning null
        try {
            return (Integer.parseInt(mpd.getStatus(MPD.StatusList.PLAYLIST)));
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    private List<MPDSong> listSongs() throws MPDConnectionException, MPDPlaylistException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPINFO));
        List<String> response;

        try {
            response = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        List<MPDSong> list = new ArrayList<MPDSong>(mpd.convertResponseToSong(response));
        return (list);
    }

    /**
     * Adds a {@link MPDAlbum} by a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} for the album to add
     * @param album  the {@link MPDAlbum} to add
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void insertAlbum(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbumByArtist(artist, album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a {@link MPDAlbum} to the playlist.
     *
     * @param album the {@link MPDAlbum} to add
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void insertAlbum(MPDAlbum album) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbum(album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Removes a {@link MPDAlbum} by a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} for the album to remove
     * @param album  the {@link MPDAlbum} to remove
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void insertArtist(MPDArtist artist) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findArtist(artist)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.ARTIST_ADDED, artist.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a {@link MPDGenre} to the playlist.
     *
     * @param genre the {@link MPDGenre} to add
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void insertGenre(MPDGenre genre) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findGenre(genre)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.GENRE_ADDED, genre.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Adds a year to the playlist.
     *
     * @param year the {@link MPDGenre} to add
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public void insertYear(String year) throws MPDConnectionException, MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findYear(year)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.YEAR_ADDED, year);

        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    /**
     * Removes a {@link MPDArtist} to the playlist.
     *
     * @param artist the {@link MPDArtist} to remove
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
     * @throws org.bff.javampd.exception.MPDPlaylistException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
        return (sb.toString());
    }

    public void swap(MPDSong song, int i) throws MPDConnectionException, MPDPlaylistException {
        String[] paramList = new String[2];

        MPDCommand command;
        paramList[0] = Integer.toString(song.getId());
        paramList[1] = Integer.toString(i);
        command = new MPDCommand(prop.getProperty(MPDPROPSWAPID), paramList);

        try {
            mpd.sendMPDCommand(command);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        updatePlaylist();
    }
}
