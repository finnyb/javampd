package org.bff.javampd.server;

import org.bff.javampd.BaseTest;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.AfterEach;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

public class MPDServerStatusIT extends BaseTest {
    private Admin admin;
    private ServerStatus serverStatus;
    private Player player;
    private Playlist playlist;

    @BeforeEach
    public void setUp() {
        this.admin = getMpd().getAdmin();
        this.serverStatus = getMpd().getServerStatus();
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
    }

    @AfterEach
    public void tearDown() {
        MPDOutput output = new ArrayList<>(getMpd().getAdmin().getOutputs()).get(0);
        getMpd().getAdmin().enableOutput(output);
    }

    @Test
    public void testGetStatus() {
    }

    @Test
    public void testGetPlaylistVersion() {
        assertTrue(serverStatus.getPlaylistVersion() > 0);
    }

    @Test
    public void testGetState() {
        player.play();
        serverStatus.forceUpdate();
        assertEquals("play", serverStatus.getState());
    }

    @Test
    public void testGetXFade() {
        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testGetAudio() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSongs(songs);
        player.play();
        await().until(() -> "44100:24:1".equals(serverStatus.getAudio()));
    }

    @Test
    public void testIsError() {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        player.play();
        serverStatus.forceUpdate();
        assertTrue(serverStatus.isError());
    }

    @Test
    public void testGetError() {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        player.play();
        await().until(() -> "All audio outputs are disabled".equals(serverStatus.getError()));
        admin.enableOutput(output);
    }

    @Test
    public void testClearError() {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);

        player.play();
        serverStatus.forceUpdate();
        assertTrue(serverStatus.isError());

        getMpd().clearError();
        serverStatus.forceUpdate();
        assertFalse(serverStatus.isError());
        admin.enableOutput(output);
    }

    @Test
    public void testGetTime() {
        player.play();
        await().until(() -> serverStatus.getElapsedTime() > 0);
    }

    @Test
    public void testGetBitrate() {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSongs(songs);
        player.play();
        await().atMost(20, TimeUnit.SECONDS).until(() -> 48 == serverStatus.getBitrate());
    }

    @Test
    public void testSetExpiryInterval() {
        long time = serverStatus.getElapsedTime();
        player.play();
        await().until(() -> time == serverStatus.getElapsedTime());
    }

    @Test
    public void testExpiryInterval() {
        long time = serverStatus.getElapsedTime();
        player.play();
        await().until(() -> time == serverStatus.getElapsedTime());
    }

    @Test
    public void testForceUpdate() {
        long time = serverStatus.getElapsedTime();
        player.play();
        serverStatus.forceUpdate();
        await().until(() -> time != serverStatus.getElapsedTime());
    }

    @Test
    public void testGetVolume() {
        player.setVolume(5);
        await().until(() -> 5 == serverStatus.getVolume());
    }

    @Test
    public void testIsRepeat() {
        assertFalse(serverStatus.isRepeat());
    }

    @Test
    public void testIsRandom() {
        assertFalse(serverStatus.isRandom());
    }
}