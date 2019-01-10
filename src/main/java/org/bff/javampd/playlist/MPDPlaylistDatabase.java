package org.bff.javampd.playlist;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.DatabaseProperties;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * MPDPlaylistDatabase represents a playlist database to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getPlaylistDatabase()} method from
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
    public Collection<MPDSavedPlaylist> listSavedPlaylists() {
        List<MPDSavedPlaylist> playlists = new ArrayList<>();

        for (String s : listPlaylists()) {
            MPDSavedPlaylist playlist = new MPDSavedPlaylist(s);
            playlist.setSongs(listPlaylistSongs(s));
            playlists.add(playlist);
        }
        return playlists;
    }

    @Override
    public Collection<String> listPlaylists() {
        return tagLister.listInfo(TagLister.ListInfoType.PLAYLIST);
    }

    @Override
    public Collection<MPDSong> listPlaylistSongs(String playlistName) {
        List<MPDSong> songList = new ArrayList<>();
        List<String> response = commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);
        List<MPDSong> list = new ArrayList<>();
        for (String song : songConverter.getSongFileNameList(response)) {
            final Iterator<MPDSong> found = songDatabase.searchFileName(song).iterator();
            MPDSong mpdSong = found.hasNext() ? found.next() : new MPDSong(song, playlistName);
            list.add(mpdSong);
        }
        songList.addAll(list);

        return songList;
    }
}
