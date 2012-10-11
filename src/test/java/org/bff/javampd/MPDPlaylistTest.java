package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Category(IntegrationTest.class)
public class MPDPlaylistTest extends BaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

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
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        Assert.assertEquals(getPlaylist().getSongList().size(), 1);
        getPlaylist().addSong(songs.get(1));
        Assert.assertEquals(getPlaylist().getSongList().size(), 2);

    }

    @Test
    public void testGetSongListRemoteAdded() throws MPDPlaylistException, MPDConnectionException, IOException {
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        Assert.assertEquals(getPlaylist().getSongList().size(), 1);

        Controller controller = Controller.getInstance();
        controller.getNewMPD().getMPDPlaylist().addSong(songs.get(1));

        Assert.assertEquals(getPlaylist().getSongList().size(), 2);
    }

    @Test
    public void testSavePlaylist() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        String playlistName = "testPlaylist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        String playlistName = "Test Playlist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylists().contains(playlistName));
    }

    @Test
    public void testSavePlaylistSongsWithSpace() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        String playlistName = "Test Playlist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test
    public void testSavePlaylistSongs() throws MPDPlaylistException, MPDConnectionException, IOException, MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<MPDSong>(Controller.getInstance().getSongs());
        getPlaylist().addSong(songs.get(0));
        String playlistName = "testPlaylist";

        getPlaylist().savePlaylist(playlistName);

        Assert.assertTrue(getDatabase().listPlaylistSongs(playlistName).contains(songs.get(0)));
    }

    @Test(expected = MPDResponseException.class)
    public void testAddingNonexistentPlaylist() throws Exception {
        getPlaylist().loadPlaylist("DOESNTEXIST");
    }
}
