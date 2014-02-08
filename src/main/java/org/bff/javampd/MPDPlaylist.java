package org.bff.javampd;

import com.google.inject.Inject;
import org.bff.javampd.events.PlaylistChangeEvent;
import org.bff.javampd.events.PlaylistChangeListener;
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
 * @author Bill
 */
public class MPDPlaylist implements Playlist {

    private int oldVersion = -1;
    private int version = -1;

    private List<PlaylistChangeListener> listeners;
    @Inject
    private Database database;
    @Inject
    private ServerStatus serverStatus;
    @Inject
    private PlaylistProperties playlistProperties;
    @Inject
    private CommandExecutor commandExecutor;

    /**
     * Creates a new instance of MPDPlaylist
     */
    MPDPlaylist() {
        this.listeners = new ArrayList<PlaylistChangeListener>();
        this.playlistProperties = new PlaylistProperties();
    }

    @Override
    public synchronized void addPlaylistChangeListener(PlaylistChangeListener pcl) {
        listeners.add(pcl);
    }

    @Override
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

    @Override
    public void loadPlaylist(String playlistName) throws MPDPlaylistException {
        String name = playlistName;
        if (name.endsWith(".m3u")) {
            name = name.substring(name.length() - 4);
        }

        try {
            commandExecutor.sendCommand(playlistProperties.getLoad(), name);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    @Override
    public void addSong(MPDSong song) throws MPDPlaylistException {
        addSong(song, true);
    }

    @Override
    public void addSong(MPDSong song, boolean fireEvent) throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getAdd(), song.getFile());
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

    @Override
    public boolean addSongs(List<MPDSong> songList) throws MPDPlaylistException {
        return addSongs(songList, true);
    }

    @Override
    public boolean addSongs(List<MPDSong> songList, boolean fireEvent) throws MPDPlaylistException {
        List<MPDCommand> commandList = new ArrayList<MPDCommand>();
        for (MPDSong song : songList) {
            commandList.add(new MPDCommand(playlistProperties.getAdd(), song.getFile()));
        }

        try {
            commandExecutor.sendCommands(commandList);
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

        return oldCount < songList.size();
    }

    @Override
    public void addFileOrDirectory(MPDFile file) throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getAdd(), file.getPath());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();

        firePlaylistChangeEvent(PlaylistChangeEvent.Event.FILE_ADDED, file.getName());

    }

    @Override
    public void removeSong(MPDSong song) throws MPDPlaylistException {
        try {
            if (song.getId() > -1) {
                commandExecutor.sendCommand(playlistProperties.getRemoveId(), song.getId());

            } else if (song.getPosition() > -1) {
                commandExecutor.sendCommand(playlistProperties.getRemove(), song.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    @Override
    public MPDSong getCurrentSong() throws MPDPlaylistException {
        try {
            List<MPDSong> songs = convertResponseToSong(commandExecutor.sendCommand(playlistProperties.getCurrentSong()));
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

    @Override
    public void clearPlaylist() throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getClear());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    @Override
    public void deletePlaylist(MPDSavedPlaylist playlist) throws MPDPlaylistException {
        deletePlaylist(playlist.getName());
    }

    @Override
    public void deletePlaylist(String playlistName) throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getDelete(), playlistName);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_DELETED);
    }

    @Override
    public void move(MPDSong song, int to) throws MPDPlaylistException {
        String[] paramList = new String[2];
        try {
            paramList[1] = Integer.toString(to);
            if (song.getId() > -1) {
                commandExecutor.sendCommand(playlistProperties.getMoveId(), song.getId());
            } else if (song.getPosition() > -1) {
                commandExecutor.sendCommand(playlistProperties.getMoveId(), song.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    @Override
    public void shuffle() throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getShuffle());
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }

        updatePlaylist();
    }

    @Override
    public void swap(MPDSong song1, MPDSong song2) throws MPDPlaylistException {
        try {
            if (song1.getId() > -1 && song2.getId() > -1) {
                commandExecutor.sendCommand(playlistProperties.getSwapId(), song1.getId(), song2.getId());
            } else if (song1.getPosition() > -1 && song2.getPosition() > -1) {
                commandExecutor.sendCommand(playlistProperties.getSwap(), song1.getPosition(), song2.getPosition());
            }
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        updatePlaylist();
    }

    @Override
    public boolean savePlaylist(String playlistName) throws MPDPlaylistException {
        if (playlistName != null) {
            try {
                commandExecutor.sendCommand(playlistProperties.getSave(), playlistName);
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

    private void updatePlaylist() throws MPDPlaylistException {
        setVersion(getPlaylistVersion());

        if (getPlaylistVersion() != oldVersion) {
            oldVersion = getVersion();
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_CHANGED);
        }
    }

    private int getPlaylistVersion() throws MPDPlaylistException {
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
     * @throws MPDPlaylistException if the MPD responded with an error
     */
    private List<MPDSong> listSongs() throws MPDPlaylistException {
        try {
            return convertResponseToSong(commandExecutor.sendCommand(playlistProperties.getInfo()));
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void insertAlbum(MPDArtist artist, MPDAlbum album) throws MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbumByArtist(artist, album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void insertAlbum(MPDAlbum album) throws MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findAlbum(album)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void removeAlbum(MPDArtist artist, MPDAlbum album) throws MPDPlaylistException {
        List<MPDSong> removeList = new ArrayList<MPDSong>();

        for (MPDSong song : getSongList()) {
            if (song.getArtistName().equals(artist.getName()) && song.getAlbumName().equals(album.getName())) {
                removeList.add(song);
            }
        }

        for (MPDSong song : removeList) {
            removeSong(song);
        }
    }

    @Override
    public void insertArtist(MPDArtist artist) throws MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findArtist(artist)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED, artist.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void insertGenre(MPDGenre genre) throws MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findGenre(genre)) {
                addSong(song, false);
            }
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.GENRE_ADDED, genre.getName());
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void insertYear(String year) throws MPDPlaylistException {
        try {
            for (MPDSong song : getDatabase().findYear(year)) {
                addSong(song, false);
            }

            firePlaylistChangeEvent(PlaylistChangeEvent.Event.YEAR_ADDED, year);

        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
    }

    @Override
    public void removeArtist(MPDArtist artist) throws MPDPlaylistException {
        List<MPDSong> removeList = new ArrayList<MPDSong>();
        for (MPDSong song : getSongList()) {
            if (song.getArtistName().equals(artist.getName())) {
                removeList.add(song);
            }
            removeList.add(song);
        }

        for (MPDSong song : removeList) {
            removeSong(song);
        }
    }

    @Override
    public Database getDatabase() {
        return database;
    }

    @Override
    public void setDatabase(Database database) {
        this.database = database;
    }

    @Override
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

    @Override
    public List<MPDSong> getSongList() throws MPDPlaylistException {
        return listSongs();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Version:").append(getVersion()).append("\n");
        return sb.toString();
    }

    @Override
    public void swap(MPDSong song, int i) throws MPDPlaylistException {
        try {
            commandExecutor.sendCommand(playlistProperties.getSwapId(), song.getId(), i);
        } catch (MPDResponseException re) {
            throw new MPDPlaylistException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDPlaylistException(e);
        }
        updatePlaylist();
    }
}
