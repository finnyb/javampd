package org.bff.javampd.player;

import org.bff.javampd.BaseTest;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.*;

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
    public void testAudioDetailsStopped() {
        player.stop();
        Assert.assertNull(player.getAudioDetails());
    }

    @Test
    public void testAudioDetails() {
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
    public void testGetTime() {
        player.play();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        assertTrue(player.getElapsedTime() > 0);
    }

    @Test
    public void testGetTotalTime() {
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
    public void testSetVolume() {
        player.setVolume(0);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        player.setVolume(5);

        assertTrue(5 == player.getVolume());
    }

    @Test
    public void testVolumeTooLow() {
        int volume = player.getVolume();
        player.setVolume(-1);

        assertEquals(volume, player.getVolume());
    }

    @Test
    public void testVolumeTooHigh() {
        int volume = player.getVolume();
        player.setVolume(101);

        assertEquals(volume, player.getVolume());
    }

    @Test
    public void testPaused() {
        player.play();
        player.pause();
        assertEquals(Player.Status.STATUS_PAUSED, player.getStatus());
    }

}
