package org.bff.javampd.monitor;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDSongs;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPDStandAloneMonitorIT extends BaseTest {

    /**
     * Delay for various monitor actions
     */
    private static final int MONITOR_DELAY = 1500;

    private Player player;
    private Playlist playlist;
    private Admin admin;
    private StandAloneMonitor monitor;

    @Before
    public void setUp() {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        this.admin = getMpd().getAdmin();
        this.monitor = getMpd().getMonitor();

        playlist.clearPlaylist();
        player.stop();
        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.enableOutput(output);
        monitor.start();
        delay();
    }

    @After
    public void tearDown() {
        monitor.stop();
        delay();
    }

    private void delay() {
        delay(1);
    }

    private void delay(int multiplier) {
        try {
            Thread.sleep(MONITOR_DELAY * multiplier);
        } catch (InterruptedException e) {
            //don't care
            e.printStackTrace();
        }
    }

    private boolean success;

    @Test
    public void testAddSong() {
        success = false;

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_ADDED:
                    success = true;
                    break;
            }
        });

        playlist.addSong(MPDSongs.getSongs().get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistChanged() {
        success = false;

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case PLAYLIST_CHANGED:
                    success = true;
                    break;
            }
        });

        playlist.addSong(MPDSongs.getSongs().get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testRemoveSong() {
        success = false;

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_DELETED:
                    success = true;
                    break;
            }
        });

        MPDSong song = MPDSongs.getSongs().get(0);

        playlist.addSong(song);
        delay(2);
        playlist.removeSong(playlist.getSongList().get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testSongChanged() {
        success = false;

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_CHANGED:
                    success = true;
                    break;
            }
        });
        playlist.addSong(MPDSongs.getSongs().get(0));
        playlist.addSong(MPDSongs.getSongs().get(1));

        player.play();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    private void waitForSuccess() {
        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorIT.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Test
    public void testPlayerStarted() {
        success = false;

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_STARTED:
                    success = true;
                    break;
            }
        });

        player.stop();
        delay();

        success = false;
        loadSeveralSongs();
        player.play();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerStopped() {
        success = false;

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_STOPPED:
                    success = true;
                    break;
            }
        });

        success = false;
        loadSeveralSongs();
        player.play();
        delay(5);
        player.stop();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerPaused() {
        success = false;

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_PAUSED:
                    success = true;
                    break;
            }
        });


        success = false;
        loadSeveralSongs();
        player.play();
        delay(2);
        player.pause();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testVolumeChanged() {
        success = false;

        player.setVolume(0);

        delay(2);

        monitor.addVolumeChangeListener(event -> success = true);

        loadSeveralSongs();
        player.play();

        player.setVolume(5);
        waitForSuccess();
        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerUnPaused() {
        success = false;
        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_UNPAUSED:
                    success = true;
                    break;
            }
        });
        success = false;
        loadSeveralSongs();
        player.play();
        player.pause();
        delay();
        player.pause();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testOutputChanged() {
        success = false;

        monitor.addOutputChangeListener(event -> success = true);

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        waitForSuccess();
        Assert.assertTrue(success);
    }

    private void loadSeveralSongs() {
        playlist.addSong(MPDSongs.getSongs().get(0));
        playlist.addSong(MPDSongs.getSongs().get(1));
        playlist.addSong(MPDSongs.getSongs().get(2));
        playlist.addSong(MPDSongs.getSongs().get(3));
        playlist.addSong(MPDSongs.getSongs().get(4));
        playlist.addSong(MPDSongs.getSongs().get(5));
        playlist.addSong(MPDSongs.getSongs().get(6));
        playlist.addSong(MPDSongs.getSongs().get(7));
        playlist.addSong(MPDSongs.getSongs().get(8));
        playlist.addSong(MPDSongs.getSongs().get(9));
    }
}
