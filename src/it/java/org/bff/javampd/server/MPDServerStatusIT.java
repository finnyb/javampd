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
    public void testGetStatus() throws Exception {
    }

    @Test
    public void testGetPlaylistVersion() throws Exception {
        assertTrue(serverStatus.getPlaylistVersion() > 0);
    }

    @Test
    public void testGetState() throws Exception {
        player.play();
        serverStatus.forceUpdate();
        assertEquals("play", serverStatus.getState());
    }

    @Test
    public void testGetXFade() throws Exception {
        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testGetAudio() throws Exception {
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSongs(songs);
        player.play();

        Thread.sleep(1000);

        assertEquals("44100:24:1", serverStatus.getAudio());
    }

    @Test
    public void testIsError() throws Exception {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        player.play();
        serverStatus.forceUpdate();
        assertTrue(serverStatus.isError());
    }

    @Test
    public void testGetError() throws Exception {
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        player.play();
        assertEquals("All audio outputs are disabled", serverStatus.getError());
        admin.enableOutput(output);
    }

    @Test
    public void testClearError() throws Exception {
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
    public void testGetTime() throws Exception {
        player.play();
        Thread.sleep(2000);

        assertTrue(serverStatus.getElapsedTime() > 0);
    }

    @Test
    public void testGetBitrate() throws Exception {
        player.play();
        Thread.sleep(5000);
        assertEquals(48, serverStatus.getBitrate());
    }

    @Test
    public void testSetExpiryInterval() throws Exception {
        long time = serverStatus.getElapsedTime();
        player.play();
        Thread.sleep(1000);
        assertEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testExpiryInterval() throws Exception {
        long time = serverStatus.getElapsedTime();
        player.play();
        Thread.sleep(1000);
        assertEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testForceUpdate() throws Exception {
        long time = serverStatus.getElapsedTime();
        player.play();
        serverStatus.forceUpdate();
        assertNotEquals(time, serverStatus.getElapsedTime());
    }

    @Test
    public void testGetVolume() throws Exception {
        player.setVolume(5);
        assertEquals(5, serverStatus.getVolume());
    }

    @Test
    public void testIsRepeat() throws Exception {
        assertFalse(serverStatus.isRepeat());
    }

    @Test
    public void testIsRandom() throws Exception {
        assertFalse(serverStatus.isRandom());
    }
}