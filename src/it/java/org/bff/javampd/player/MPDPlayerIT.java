package org.bff.javampd.player;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.playlist.MPDPlaylistException;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.song.MPDSong;
import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDPlayerIT extends BaseTest {
    private Playlist playlist;
    private Player player;

    @Before
    public void setUp() throws Exception {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSongs(songs);
    }

    @After
    public void tearDown() throws Exception {
        playlist.clearPlaylist();
    }

    @Test
    public void testAudioDetailsStopped() throws MPDConnectionException, MPDPlayerException {
        player.stop();
        Assert.assertNull(player.getAudioDetails());
    }

    @Test
    public void testAudioDetails() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        player.play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        MPDAudioInfo info = player.getAudioDetails();

        assertEquals(info.getSampleRate(), 44100);
        assertEquals(info.getBits(), 24);
        assertEquals(info.getChannels(), 1);
    }

    @Test
    public void testGetTime() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        player.play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertTrue(player.getElapsedTime() > 0);
    }

    @Test
    public void testGetTotalTime() throws MPDConnectionException, MPDPlayerException, MPDPlaylistException {
        player.play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(5, player.getTotalTime());
    }

    @Test
    @Ignore
    public void testSetVolume() throws MPDException, IOException {
        player.setVolume(0);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        player.setVolume(5);

        assertTrue(5 == player.getVolume());
    }

    @Test(expected = MPDPlayerException.class)
    public void testVolumeTooLow() throws MPDPlayerException, MPDConnectionException {
        player.setVolume(-1);
    }

    @Test(expected = MPDPlayerException.class)
    public void testVolumeTooHigh() throws MPDPlayerException, MPDConnectionException {
        player.setVolume(101);
    }

    @Test
    public void testPaused() throws MPDPlayerException {
        player.play();
        player.pause();
        assertEquals(Player.Status.STATUS_PAUSED, player.getStatus());
    }

}
