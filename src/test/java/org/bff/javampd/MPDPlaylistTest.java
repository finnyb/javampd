package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.MPDSong;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MPDPlaylistTest extends BaseTest {

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() throws MPDPlaylistException, MPDConnectionException {
        getPlaylist().clearPlaylist();
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
        new MPD(controller.getServer(), controller.getPort(), controller.getPassword()).getMPDPlaylist().addSong(songs.get(1));
        Assert.assertEquals(getPlaylist().getSongList().size(), 2);
    }

    @Test(expected = MPDResponseException.class)
    public void testAddingNonexistentPlaylist() throws Exception {
        getPlaylist().loadPlaylist("DOESNTEXIST");
    }
}
