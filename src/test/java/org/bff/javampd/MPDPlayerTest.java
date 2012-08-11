package org.bff.javampd;

import org.bff.javampd.integrationdata.Songs;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.objects.MPDAudioInfo;
import org.bff.javampd.objects.MPDSong;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Category(IntegrationTest.class)
public class MPDPlayerTest extends BaseTest {
    @Before
    public void setUp() throws Exception {
        List<MPDSong> songs = new ArrayList<MPDSong>(Songs.getTestSongs());
        getPlaylist().addSongs(songs);
    }

    @After
    public void tearDown() throws Exception {
        getPlaylist().clearPlaylist();
    }

    @Test
    public void testAudioDetailsStopped() throws MPDConnectionException, MPDPlayerException {
        getPlayer().stop();
        Assert.assertNull(getPlayer().getAudioDetails());
    }

    @Test
    public void testAudioDetails() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        getPlayer().play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MPDAudioInfo info = getPlayer().getAudioDetails();

        Assert.assertEquals(info.getSampleRate(), 44100);
        Assert.assertEquals(info.getBits(), 24);
        Assert.assertEquals(info.getChannels(), 1);
    }

    @Test
    @Ignore
    public void testSetVolume() throws MPDException, IOException {
        getPlayer().setVolume(0);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getPlayer().setVolume(5);

        Assert.assertTrue(5 == getPlayer().getVolume());
    }
}
