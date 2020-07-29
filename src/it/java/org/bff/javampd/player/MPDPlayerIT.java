package org.bff.javampd.player;

import org.bff.javampd.BaseTest;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.AfterEach;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MPDPlayerIT extends BaseTest {
    private Playlist playlist;
    private Player player;

    @BeforeEach
    public void setUp() {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSongs(songs);
    }

    @AfterEach
    public void tearDown() {
        playlist.clearPlaylist();
    }

    @Test
    public void testAudioDetailsStopped() {
        player.stop();
        await().until(() -> null == player.getAudioDetails());
    }

    @Test
    public void testAudioDetails() {
        player.play();
        await().until(() -> player.getAudioDetails().getSampleRate() > 0);

        MPDAudioInfo info = player.getAudioDetails();

        assertEquals(info.getSampleRate(), 44100);
        assertEquals(info.getBits(), 24);
        assertEquals(info.getChannels(), 1);
    }

    @Test
    public void testGetTime() {
        player.play();
        await().until(() -> player.getElapsedTime() > 0);
    }

    @Test
    public void testGetTotalTime() {
        player.play();
        await().until(() -> 5 == player.getTotalTime());
    }

    @Test
    @Ignore
    public void testSetVolume() {
        player.setVolume(0);

        await().until(() -> 0 == player.getVolume());

        player.setVolume(5);

        await().until(() -> 5 == player.getVolume());
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
        await().until(() -> Player.Status.STATUS_PLAYING == player.getStatus());
        player.pause();
        await().until(() -> Player.Status.STATUS_PAUSED == player.getStatus());
    }

    @Test
    public void testPlaySong() {
        MPDSong testSong = new ArrayList<>(TestSongs.getSongs()).get(1);
        String testFile = "/" + testSong.getFile();
        String testTitle = testSong.getTitle();

        player.playSong(new MPDSong(testFile, testTitle));
    }
}
