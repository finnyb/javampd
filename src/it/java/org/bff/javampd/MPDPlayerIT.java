package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDPlayerException;
import org.bff.javampd.exception.MPDPlaylistException;
import org.bff.javampd.integrationdata.Songs;
import org.bff.javampd.objects.MPDAudioInfo;
import org.bff.javampd.objects.MPDSong;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDPlayerIT extends BaseTest {
    @Before
    public void setUp() throws Exception {
        List<MPDSong> songs = new ArrayList<>(Songs.songs);
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

        assertEquals(info.getSampleRate(), 44100);
        assertEquals(info.getBits(), 24);
        assertEquals(info.getChannels(), 1);
    }

    @Test
    public void testGetTime() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        getPlayer().play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertTrue(getPlayer().getElapsedTime() > 0);
    }

    @Test
    public void testGetTotalTime() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        getPlayer().play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, getPlayer().getTotalTime());
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

    @Test(expected = MPDPlayerException.class)
    public void testVolumeTooLow() throws MPDPlayerException, MPDConnectionException {
        getPlayer().setVolume(-1);
    }

    @Test(expected = MPDPlayerException.class)
    public void testVolumeTooHigh() throws MPDPlayerException, MPDConnectionException {
        getPlayer().setVolume(101);
    }

    @Test
    public void testPaused() throws MPDPlayerException {
        getPlayer().play();
        getPlayer().pause();
        assertEquals(Player.Status.STATUS_PAUSED, getPlayer().getStatus());
    }

}
