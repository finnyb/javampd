package org.bff.javampd.playlist;

import com.google.inject.Inject;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.file.MPDFile;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDPlaylist represents a playlist controller to a MPD server.  To obtain
 * an instance of the class you must use the <code>getMPDPlaylist</code> method from
 * the {@link org.bff.javampd.server.MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDPlaylist implements Playlist {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlaylist.class);

    private int oldVersion = -1;
    private int version = -1;

    private List<PlaylistChangeListener> listeners;
    private SongDatabase songDatabase;
    private ServerStatus serverStatus;
    private PlaylistProperties playlistProperties;
    private CommandExecutor commandExecutor;
    private SongConverter songConverter;

    /**
     * Playlist constructor
     *
     * @param songDatabase       the song database
     * @param serverStatus       the server status
     * @param playlistProperties playlist properties
     * @param commandExecutor    command runner
     * @param songConverter      song marshaller
     */
    @Inject
    public MPDPlaylist(SongDatabase songDatabase,
                       ServerStatus serverStatus,
                       PlaylistProperties playlistProperties,
                       CommandExecutor commandExecutor,
                       SongConverter songConverter) {
        this.songDatabase = songDatabase;
        this.serverStatus = serverStatus;
        this.playlistProperties = playlistProperties;
        this.commandExecutor = commandExecutor;
        this.songConverter = songConverter;
        this.listeners = new ArrayList<>();
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
     * @param name  name of the added entity
     */
    protected synchronized void firePlaylistChangeEvent(PlaylistChangeEvent.Event event, String name) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, event, name);

        for (PlaylistChangeListener pcl : listeners) {
            pcl.playlistChanged(pce);
        }
    }

    @Override
    public void loadPlaylist(String playlistName) {
        String name = playlistName;
        if (name.endsWith(".m3u")) {
            name = name.substring(0, name.length() - 4);
        }

        commandExecutor.sendCommand(playlistProperties.getLoad(), name);
        updatePlaylist();
    }

    @Override
    public void addSong(MPDSong song) {
        addSong(song, true);
    }

    @Override
    public void addSong(MPDSong song, boolean fireEvent) {
        addSong(song.getFile(), fireEvent);
    }

    @Override
    public void addSong(String file) {
        this.addSong(file, true);
    }

    @Override
    public void addSong(String file, boolean fireEvent) {
        commandExecutor.sendCommand(playlistProperties.getAdd(), file);
        updatePlaylist();
        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_ADDED, file);
        }
    }

    @Override
    public boolean addSongs(List<MPDSong> songList) {
        return addSongs(songList, true);
    }

    @Override
    public boolean addSongs(List<MPDSong> songList, boolean fireEvent) {
        List<MPDCommand> list = new ArrayList<>();
        for (MPDSong song : songList) {
            MPDCommand mpdCommand = new MPDCommand(playlistProperties.getAdd(), song.getFile());
            list.add(mpdCommand);
        }
        commandExecutor.sendCommands(list);

        int oldCount = songList.size();
        updatePlaylist();

        if (fireEvent) {
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.MULTIPLE_SONGS_ADDED, Integer.toString(songList.size()));
        }

        return oldCount < songList.size();
    }

    @Override
    public void addFileOrDirectory(MPDFile file) {
        commandExecutor.sendCommand(playlistProperties.getAdd(), file.getPath());
        updatePlaylist();
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.FILE_ADDED, file.getPath());
    }

    @Override
    public void removeSong(MPDSong song) {
        if (song.getId() > -1) {
            updatePlaylist();
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_DELETED, song.getName());
            commandExecutor.sendCommand(playlistProperties.getRemoveId(), song.getId());
        } else {
            removeSong(song.getPosition());
        }
    }

    @Override
    public void removeSong(int position) {
        if (position > -1) {
            commandExecutor.sendCommand(playlistProperties.getRemove(), position);
            updatePlaylist();
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_DELETED);
        }
    }

    @Override
    public MPDSong getCurrentSong() {
        List<MPDSong> songs = convertResponseToSong(commandExecutor.sendCommand(playlistProperties.getCurrentSong()));
        return songs.isEmpty() ? null : songs.get(0);
    }

    private List<MPDSong> convertResponseToSong(List<String> response) {
        return songConverter.convertResponseToSong(response);
    }

    @Override
    public void clearPlaylist() {
        commandExecutor.sendCommand(playlistProperties.getClear());
        updatePlaylist();
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_CLEARED);
    }

    @Override
    public void deletePlaylist(MPDSavedPlaylist playlist) {
        deletePlaylist(playlist.getName());
    }

    @Override
    public void deletePlaylist(String playlistName) {
        commandExecutor.sendCommand(playlistProperties.getDelete(), playlistName);
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_DELETED);
    }

    @Override
    public void move(MPDSong song, int to) {
        if (song.getId() > -1) {
            commandExecutor.sendCommand(playlistProperties.getMoveId(), song.getId(), to);
        } else if (song.getPosition() > -1) {
            commandExecutor.sendCommand(playlistProperties.getMove(), song.getPosition(), to);
        }

        updatePlaylist();
    }

    @Override
    public void shuffle() {
        commandExecutor.sendCommand(playlistProperties.getShuffle());
        updatePlaylist();
    }

    @Override
    public void swap(MPDSong song1, MPDSong song2) {
        if (song1.getId() > -1 && song2.getId() > -1) {
            commandExecutor.sendCommand(playlistProperties.getSwapId(), song1.getId(), song2.getId());
        } else if (song1.getPosition() > -1 && song2.getPosition() > -1) {
            commandExecutor.sendCommand(playlistProperties.getSwap(), song1.getPosition(), song2.getPosition());
        }
        updatePlaylist();
    }

    @Override
    public boolean savePlaylist(String playlistName) {
        if (playlistName != null) {
            commandExecutor.sendCommand(playlistProperties.getSave(), playlistName);
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_SAVED);

            return true;
        } else {
            LOGGER.error("Playlist not saved since name was null");
            return false;
        }
    }

    private void updatePlaylist() {
        setVersion(getPlaylistVersion());

        if (getPlaylistVersion() != oldVersion) {
            oldVersion = getVersion();
            firePlaylistChangeEvent(PlaylistChangeEvent.Event.PLAYLIST_CHANGED);
        }
    }

    private int getPlaylistVersion() {
        return serverStatus.getPlaylistVersion();
    }

    /**
     * Returns the list of songs in the playlist.
     *
     * @return the list of songs
     */
    private List<MPDSong> listSongs() {
        return convertResponseToSong(commandExecutor.sendCommand(playlistProperties.getInfo()));
    }

    @Override
    public void insertAlbum(MPDArtist artist, MPDAlbum album) {
        for (MPDSong song : songDatabase.findAlbumByArtist(artist, album)) {
            addSong(song, false);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
    }

    @Override
    public void insertAlbum(String artist, String album) {
        for (MPDSong song : songDatabase.findAlbumByArtist(artist, album)) {
            addSong(song, false);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album);
    }

    @Override
    public void insertAlbum(MPDAlbum album) {
        for (MPDSong song : songDatabase.findAlbum(album)) {
            addSong(song, false);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album.getName());
    }

    @Override
    public void insertAlbum(String album) {
        for (MPDSong song : songDatabase.findAlbum(album)) {
            addSong(song, false);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED, album);
    }

    @Override
    public void removeAlbum(MPDArtist artist, MPDAlbum album) {
        removeAlbum(artist.getName(), album.getName());
    }

    @Override
    public void removeAlbum(String artistName, String albumName) {
        List<MPDSong> removeList = new ArrayList<>();
        for (MPDSong song : getSongList()) {
            if (song.getArtistName().equals(artistName) && song.getAlbumName().equals(albumName)) {
                removeList.add(song);
            }
        }

        for (MPDSong mpdSong : removeList) {
            removeSong(mpdSong);
        }
    }

    @Override
    public void insertArtist(MPDArtist artist) {
        insertArtist(artist.getName());
    }

    @Override
    public void insertArtist(String artistName) {
        for (MPDSong song : songDatabase.findArtist(artistName)) {
            addSong(song, false);
        }

        firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED, artistName);
    }

    @Override
    public void insertGenre(MPDGenre genre) {
        insertGenre(genre.getName());
    }

    @Override
    public void insertGenre(String genreName) {
        for (MPDSong song : songDatabase.findGenre(genreName)) {
            addSong(song, false);
        }
        firePlaylistChangeEvent(PlaylistChangeEvent.Event.GENRE_ADDED, genreName);
    }

    @Override
    public void removeGenre(MPDGenre genre) {
        removeGenre(genre.getName());
    }

    @Override
    public void removeGenre(String genreName) {
        List<MPDSong> removeList =
                new ArrayList<>();
        for (MPDSong song : getSongList()) {
            if (song.getGenre().equals(genreName)) {
                removeList.add(song);
            }
        }

        for (MPDSong mpdSong : removeList) {
            removeSong(mpdSong);
        }
    }

    @Override
    public void insertYear(String year) {
        for (MPDSong song : songDatabase.findYear(year)) {
            addSong(song, false);
        }

        firePlaylistChangeEvent(PlaylistChangeEvent.Event.YEAR_ADDED, year);
    }

    @Override
    public void removeYear(String year) {
        List<MPDSong> removeList = new ArrayList<>();
        for (MPDSong song : getSongList()) {
            if (song.getYear().equals(year)) {
                removeList.add(song);
            }
        }

        for (MPDSong mpdSong : removeList) {
            removeSong(mpdSong);
        }
    }

    @Override
    public void removeArtist(MPDArtist artist) {
        removeArtist(artist.getName());
    }

    @Override
    public void removeArtist(String artistName) {
        List<MPDSong> removeList = new ArrayList<>();
        for (MPDSong song : getSongList()) {
            if (song.getArtistName().equals(artistName)) {
                removeList.add(song);
            }
        }

        for (MPDSong mpdSong : removeList) {
            removeSong(mpdSong);
        }
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
    public List<MPDSong> getSongList() {
        return listSongs();
    }

    @Override
    public void swap(MPDSong song, int i) {
        commandExecutor.sendCommand(playlistProperties.getSwapId(), song.getId(), i);
        updatePlaylist();
    }
}