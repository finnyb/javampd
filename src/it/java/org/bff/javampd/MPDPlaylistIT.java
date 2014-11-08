package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.integrationdata.Songs;
import org.bff.javampd.objects.MPDSong;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MPDPlaylistIT extends BaseTest {

    @Before
    public void setUp() throws MPDPlaylistException, MPDConnectionException, MPDDatabaseException {
        getPlaylist().clearPlaylist();
        for (String playlist : getDatabase().listPlaylists()) {
            getPlaylist().deletePlaylist(playlist);
        }
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testGetVersion() {
        Assert.assertTrue(getPlaylist().getVersion() > -1);
    }

    @Test
    public void testGetSongList() throws MPDPlaylistException, MPDConnectionException, IOException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        assertEquals(getPlaylist().getSongList().size(), 1);
        getPlaylist().addSong(songs.get(1));
        assertEquals(getPlaylist().getSongList().size(), 2);

    }

    @Test
    public void testGetSongListRemoteAdded() throws MPDPlaylistException, MPDConnectionException, IOException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        assertEquals(getPlaylist().getSongList().size(), 1);

        getNewMpd().getPlaylist().addSong(songs.get(1));

        assertEquals(getPlaylist().getSongList().size(), 2);
    }

    @Test
    public void testSavePlaylist() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        String playlistName = "testPlaylist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        String playlistName = "Test Playlist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistSongsWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        String playlistName = "Test Playlist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test
    public void testSavePlaylistSongs() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        String playlistName = "testPlaylist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test(expected = MPDResponseException.class)
    public void testAddingNonexistentPlaylist() throws Exception {
        getPlaylist().loadPlaylist("DOESNTEXIST");
    }

    @Test
    public void testMove() throws MPDPlaylistException {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
        getPlaylist().addSong(songs.get(0));
        getPlaylist().addSong(songs.get(1));
        getPlaylist().addSong(songs.get(2));
        getPlaylist().addSong(songs.get(3));

        MPDSong song = getPlaylist().getSongList().get(3);

        assertEquals(song, getPlaylist().getSongList().get(3));
        getPlaylist().move(song, 1);
        assertEquals(song, getPlaylist().getSongList().get(1));
    }
}
