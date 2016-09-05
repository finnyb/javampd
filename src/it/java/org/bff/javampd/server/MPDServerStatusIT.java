package org.bff.javampd.server;

import org.bff.javampd.BaseTest;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MPDServerStatusIT extends BaseTest {
    private Admin admin;
    private ServerStatus serverStatus;
    private Player player;
    private Playlist playlist;
    private static long DELAY = 2000;
    
    @Before
    public void setUp() {
        this.admin = getMpd().getAdmin();
        this.serverStatus = getMpd().getServerStatus();
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
    }

    @After
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

        delay(DELAY * 2);

        assertEquals("44100:24:1", serverStatus.getAudio());
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
        delay(DELAY * 3);
        assertEquals("All audio outputs are disabled", serverStatus.getError());
        admin.enableOutput(output);
    }

    @Test
    public void testClearError() {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);

        player.play();
        serverStatus.forceUpdate();
        assertTrue(serverStatus.isError());

        getMpd().clearerror();
        serverStatus.forceUpdate();
        assertFalse(serverStatus.isError());
        admin.enableOutput(output);
    }

    @Test
    public void testGetTime() {
        player.play();
        delay(DELAY * 2);

        assertTrue(serverStatus.getElapsedTime() > 0);
    }

    @Test
    public void testGetBitrate() {
        player.play();
        delay(DELAY * 3);
        assertEquals(48, serverStatus.getBitrate());
    }

    @Test
    public void testSetExpiryInterval() {
        long time = serverStatus.getElapsedTime();
        player.play();
        delay(DELAY);
        assertEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testExpiryInterval() {
        long time = serverStatus.getElapsedTime();
        player.play();
        delay(DELAY);
        assertEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testForceUpdate() {
        long time = serverStatus.getElapsedTime();
        player.play();
        serverStatus.forceUpdate();
        delay(DELAY);

        assertNotEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testGetVolume() {
        player.setVolume(5);
        delay(DELAY * 3);
        assertEquals(5, serverStatus.getVolume());
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