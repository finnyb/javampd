package org.bff.javampd.playlist;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.song.MPDSong;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MPDPlaylistIT extends BaseTest {
    private Playlist playlist;
    private PlaylistDatabase playlistDatabase;

    @BeforeEach
    public void setUp() {
        this.playlist = getMpd().getPlaylist();
        this.playlistDatabase = getMpd().getMusicDatabase().getPlaylistDatabase();

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
    public void testGetSongList() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        Assert.assertEquals(playlist.getSongList().size(), 1);
        playlist.addSong(songs.get(1));
        Assert.assertEquals(playlist.getSongList().size(), 2);

    }

    @Test
    public void testGetSongListRemoteAdded() throws Exception {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        Assert.assertEquals(playlist.getSongList().size(), 1);

        getNewMpd().getPlaylist().addSong(songs.get(1));

        Assert.assertEquals(playlist.getSongList().size(), 2);
    }

    @Test
    public void testSavePlaylist() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "testPlaylist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistWithSpace() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "Test Playlist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistSongsWithSpace() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "Test Playlist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test
    public void testSavePlaylistSongs() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        String playlistName = "testPlaylist";

        playlist.savePlaylist(playlistName);

        Assert.assertTrue(playlistDatabase.listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test(expected = MPDException.class)
    public void testAddingNonexistentPlaylist() {
        playlist.loadPlaylist("DOESNTEXIST");
    }

    @Test
    public void testMove() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        playlist.addSong(songs.get(1));
        playlist.addSong(songs.get(2));
        playlist.addSong(songs.get(3));

        MPDSong song = playlist.getSongList().get(3);

        assertEquals(song, playlist.getSongList().get(3));
        playlist.move(song, 1);
        assertEquals(song, playlist.getSongList().get(1));
    }
}
