package org.bff.javampd.monitor;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDOutput;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.integrationdata.Songs;
import org.bff.javampd.objects.MPDSong;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MPDStandAloneMonitorIT extends BaseTest {

    /**
     * Delay for various monitor actions
     */
    private static final int MONITOR_DELAY = 3000;

    @Before
    public void setUp() throws MPDException {
        getPlaylist().clearPlaylist();
        getPlayer().stop();
        MPDOutput output = new ArrayList<>(getAdmin().getOutputs()).get(0);
        getAdmin().enableOutput(output);
        getMonitor().start();
        delay();
    }

    @After
    public void tearDown() {
        getMonitor().stop();
        getMonitor().clearListeners();
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
    public void testAddSong() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getEvent()) {
                    case SONG_ADDED:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().addSong(Songs.databaseSongs.get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistChanged() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getEvent()) {
                    case PLAYLIST_CHANGED:
                        success = true;
                        break;
                }
            }
        });

        getPlaylist().addSong(Songs.databaseSongs.get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testRemoveSong() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getEvent()) {
                    case SONG_DELETED:
                        success = true;
                        break;
                }
            }
        });

        MPDSong song = Songs.databaseSongs.get(0);

        getPlaylist().addSong(song);
        delay();
        getPlaylist().removeSong(getPlaylist().getSongList().get(0));

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testSongChanged() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getEvent()) {
                    case SONG_CHANGED:
                        success = true;
                        break;
                }
            }
        });
        getPlaylist().addSong(Songs.databaseSongs.get(0));
        getPlaylist().addSong(Songs.databaseSongs.get(1));

        getPlayer().play();

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
    public void testPlayerStarted() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_STARTED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().stop();
        delay();

        success = false;
        loadSeveralSongs();
        getPlayer().play();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerStopped() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_STOPPED:
                        success = true;
                        break;
                }
            }
        });

        success = false;
        loadSeveralSongs();
        getPlayer().play();
        delay();
        getPlayer().stop();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerPaused() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_PAUSED:
                        success = true;
                        break;
                }
            }
        });


        success = false;
        loadSeveralSongs();
        getPlayer().play();
        delay();
        getPlayer().pause();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testVolumeChanged() throws MPDException, IOException {
        success = false;

        getPlayer().setVolume(0);

        delay(2);

        getMonitor().addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });

        loadSeveralSongs();
        getPlayer().play();

        getPlayer().setVolume(5);
        waitForSuccess();
        Assert.assertTrue(success);
    }

    @Test
    public void testPlayerUnPaused() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_UNPAUSED:
                        success = true;
                        break;
                }
            }
        });


        success = false;
        loadSeveralSongs();
        getPlayer().play();
        getPlayer().pause();
        delay();
        getPlayer().pause();

        waitForSuccess();

        Assert.assertTrue(success);
    }

    @Test
    public void testOutputChanged() throws MPDException, IOException {
        success = false;

        getMonitor().addOutputChangeListener(new OutputChangeListener() {

            @Override
            public void outputChanged(OutputChangeEvent event) {
                success = true;
            }
        });

        MPDOutput output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        getAdmin().disableOutput(output);
        waitForSuccess();
        Assert.assertTrue(success);
    }

    private void loadSeveralSongs() throws MPDException, IOException {
        getPlaylist().addSong(Songs.databaseSongs.get(0));
        getPlaylist().addSong(Songs.databaseSongs.get(1));
        getPlaylist().addSong(Songs.databaseSongs.get(2));
        getPlaylist().addSong(Songs.databaseSongs.get(3));
        getPlaylist().addSong(Songs.databaseSongs.get(4));
        getPlaylist().addSong(Songs.databaseSongs.get(5));
        getPlaylist().addSong(Songs.databaseSongs.get(6));
        getPlaylist().addSong(Songs.databaseSongs.get(7));
        getPlaylist().addSong(Songs.databaseSongs.get(8));
        getPlaylist().addSong(Songs.databaseSongs.get(9));
    }
}
