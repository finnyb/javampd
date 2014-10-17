package org.bff.javampd.playlist;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.properties.DatabaseProperties;
import org.bff.javampd.server.MPDResponseException;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.DatabaseManager#getArtistDatabase} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDPlaylistDatabase implements PlaylistDatabase {
    private SongDatabase songDatabase;
    private CommandExecutor commandExecutor;
    private DatabaseProperties databaseProperties;
    private TagLister tagLister;
    private SongConverter songConverter;

    @Inject
    public MPDPlaylistDatabase(SongDatabase songDatabase,
                               CommandExecutor commandExecutor,
                               DatabaseProperties databaseProperties,
                               TagLister tagLister,
                               SongConverter songConverter) {
        this.songDatabase = songDatabase;
        this.commandExecutor = commandExecutor;
        this.databaseProperties = databaseProperties;
        this.tagLister = tagLister;
        this.songConverter = songConverter;
    }

    @Override
    public Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDDatabaseException {
        List<MPDSavedPlaylist> playlists = new ArrayList<>();

        for (String s : listPlaylists()) {
            MPDSavedPlaylist playlist = new MPDSavedPlaylist(s);
            playlist.setSongs(listPlaylistSongs(s));
            playlists.add(playlist);
        }
        return playlists;
    }

    @Override
    public Collection<String> listPlaylists() throws MPDDatabaseException {
        try {
            return tagLister.listInfo(TagLister.ListInfoType.PLAYLIST);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDDatabaseException {
        List<MPDSong> songList = new ArrayList<>();
        try {
            List<String> response = commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);
            for (String song : songConverter.getSongNameList(response)) {
                songList.add(new ArrayList<>(songDatabase.searchFileName(song)).get(0));
            }
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songList;
    }
}
