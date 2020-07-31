package org.bff.javampd.monitor;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDSongs;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.output.MPDOutput;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.AfterEach;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.awaitility.Awaitility.await;

public class MPDStandAloneMonitorIT extends BaseTest {
    private Player player;
    private Playlist playlist;
    private Admin admin;
    private StandAloneMonitor monitor;

    @BeforeEach
    public void setUp() {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        this.admin = getMpd().getAdmin();
        this.monitor = getMpd().getMonitor();

        playlist.clearPlaylist();
        await().until(() -> 0 == playlist.getSongList().size());

        player.stop();
        await().until(() -> Player.Status.STATUS_STOPPED == player.getStatus());

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.enableOutput(output);
        monitor.start();
        await().until(() -> monitor.isLoaded());
    }

    @AfterEach
    public void tearDown() {
        monitor.stop();
        await().until(() -> monitor.isDone());
    }

    @Test
    public void testAddSong() {
        final boolean[] success = {false};

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_ADDED:
                    success[0] = true;
                    break;
            }
        });

        playlist.addSong(MPDSongs.getSongs().get(0));

        await().until(() -> success[0]);

        Assert.assertTrue(success[0]);
    }

    @Test
    public void testPlaylistChanged() {
        final boolean[] success = {false};

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case PLAYLIST_CHANGED:
                    success[0] = true;
                    break;
            }
        });

        playlist.addSong(MPDSongs.getSongs().get(0));

        await().until(() -> success[0]);
    }

    @Test
    public void testRemoveSong() {
        final boolean[] success = {false};
        final boolean[] successAdded = {false};

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_DELETED:
                    success[0] = true;
                    break;
                case SONG_ADDED:
                    successAdded[0] = true;
                    break;
            }
        });

        MPDSong song = MPDSongs.getSongs().get(0);

        playlist.addSong(song);
        await().until(() -> successAdded[0]);
        playlist.removeSong(playlist.getSongList().get(0));

        await().until(() -> success[0]);
    }

    @Test
    public void testSongChanged() {
        final boolean[] success = {false};

        monitor.addPlaylistChangeListener(event -> {
            switch (event.getEvent()) {
                case SONG_CHANGED:
                    success[0] = true;
                    break;
            }
        });
        playlist.addSong(MPDSongs.getSongs().get(0));
        playlist.addSong(MPDSongs.getSongs().get(1));

        player.play();

        await().until(() -> success[0]);
    }

    @Test
    public void testPlayerStarted() {
        final boolean[] success = {false};

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_STARTED:
                    success[0] = true;
                    break;
            }
        });

        player.stop();
        await().until(() -> Player.Status.STATUS_STOPPED == player.getStatus());

        success[0] = false;
        loadSeveralSongs();
        player.play();

        await().until(() -> success[0]);
    }

    @Test
    public void testPlayerStopped() {
        final boolean[] success = {false};

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_STOPPED:
                    success[0] = true;
                    break;
            }
        });

        success[0] = false;
        loadSeveralSongs();
        player.play();
        await().until(() -> Player.Status.STATUS_PLAYING == player.getStatus());
        player.stop();

        await().until(() -> success[0]);
    }

    @Test
    public void testPlayerPaused() {
        final boolean[] success = {false};
        final boolean[] successPlaying = {false};

        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_PAUSED:
                    success[0] = true;
                    break;
                case PLAYER_STARTED:
                    successPlaying[0] = true;
                    break;
            }
        });

        success[0] = false;
        loadSeveralSongs();
        player.play();
        await().until(() -> successPlaying[0]);
        player.pause();

        await().until(() -> success[0]);
    }

    @Test
    public void testVolumeChanged() {
        final boolean[] success = {false};

        player.setVolume(0);

        await().until(() -> 0 == player.getVolume());

        monitor.addVolumeChangeListener(event -> success[0] = true);

        loadSeveralSongs();
        player.play();

        player.setVolume(5);
        await().until(() -> success[0]);
    }

    @Test
    public void testPlayerUnPaused() {
        final boolean[] success = {false};
        final boolean[] successStarted = {false};
        final boolean[] successPaused = {false};
        monitor.addPlayerChangeListener(event -> {
            switch (event.getStatus()) {
                case PLAYER_STARTED:
                    successStarted[0] = true;
                    break;
                case PLAYER_UNPAUSED:
                    success[0] = true;
                    break;
                case PLAYER_PAUSED:
                    successPaused[0] = true;
                    break;
            }
        });
        success[0] = false;
        loadSeveralSongs();
        player.play();
        await().until(() -> successStarted[0]);
        player.pause();
        await().until(() -> successPaused[0]);
        player.pause();

        await().until(() -> success[0]);
    }

    @Test
    public void testOutputChanged() {
        final boolean[] success = {false};

        monitor.addOutputChangeListener(event -> success[0] = true);

        MPDOutput output = new ArrayList<>(admin.getOutputs()).get(0);
        admin.disableOutput(output);
        await().until(() -> success[0]);
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
