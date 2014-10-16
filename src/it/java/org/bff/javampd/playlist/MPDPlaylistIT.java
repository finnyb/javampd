package org.bff.javampd.playlist;

import org.bff.javampd.BaseTest;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDResponseException;
import org.bff.javampd.song.MPDSong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MPDPlaylistIT extends BaseTest {
    private Playlist playlist;
    private PlaylistDatabase playlistDatabase;

    @Before
    public void setUp() throws MPDPlaylistException, MPDConnectionException, MPDDatabaseException {
        this.playlist = getMpd().getPlaylist();
        this.playlistDatabase = getMpd().getDatabaseManager().getPlaylistDatabase();

        playlist.clearPlaylist();
        for (String pl : playlistDatabase.listPlaylists()) {
            playlist.deletePlaylist(pl);
        }
    }

    @Test
    public void testGetVersion() {
        Assert.assertTrue(playlist.getVersion() > -1);
    }

    @Test
    public void testGetSongList() throws MPDPlaylistException, MPDConnectionException, IOException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        Assert.assertEquals(playlist.getSongList().size(), 1);
        playlist.addSong(songs.get(1));
        Assert.assertEquals(playlist.getSongList().size(), 2);

    }

    @Test
    public void testGetSongListRemoteAdded() throws MPDPlaylistException, MPDConnectionException, IOException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        Assert.assertEquals(playlist.getSongList().size(), 1);

        getNewMpd().getPlaylist().addSong(songs.get(1));

        Assert.assertEquals(playlist.getSongList().size(), 2);
    }

    @Test
    public void testSavePlaylist() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "testPlaylist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "Test Playlist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistSongsWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "Test Playlist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test
    public void testSavePlaylistSongs() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "testPlaylist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test(expected = MPDResponseException.class)
    public void testAddingNonexistentPlaylist() throws Exception {
        playlist.loadPlaylist("DOESNTEXIST");
    }
}
