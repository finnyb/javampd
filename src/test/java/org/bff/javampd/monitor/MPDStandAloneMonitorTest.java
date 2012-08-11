package org.bff.javampd.monitor;

import org.bff.javampd.BaseTest;
import org.bff.javampd.Controller;
import org.bff.javampd.IntegrationTest;
import org.bff.javampd.MPDOutput;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.objects.MPDSong;
import org.junit.*;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

@Category(IntegrationTest.class)
public class MPDStandAloneMonitorTest extends BaseTest {

    private static MPDStandAloneMonitor monitor = new MPDStandAloneMonitor(getMpd());

    @BeforeClass
    public static void setUpClass() {
        getMonitor().start();
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        getMonitor().stop();
    }

    @Before
    public void setUp() throws MPDException {
        getPlaylist().clearPlaylist();
        getPlayer().stop();
        MPDOutput output = new ArrayList<MPDOutput>(getAdmin().getOutputs()).get(0);
        getAdmin().enableOutput(output);
    }

    @After
    public void tearDown() {
    }

    private boolean success;

    @Test
    public void testAddSong() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistBasicChangeEvent.SONG_ADDED:
                        success = true;
                        break;
                }
            }
        });

        MPDSong song = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().addSong(song);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testPlaylistChanged() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistBasicChangeEvent.PLAYLIST_CHANGED:
                        success = true;
                        break;
                }
            }
        });

        MPDSong song = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().addSong(song);

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testRemoveSong() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistBasicChangeEvent.SONG_DELETED:
                        success = true;
                        break;
                }
            }
        });

        MPDSong song = Controller.getInstance().getDatabaseSongs().get(0);

        getPlaylist().addSong(song);

        getPlaylist().removeSong(getPlaylist().getSongList().get(0));

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testSongChanged() throws MPDException, IOException {
        success = false;

        getMonitor().addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlaylistBasicChangeEvent.SONG_CHANGED:
                        success = true;
                        break;
                }
            }
        });
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(0));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(1));

        getPlayer().play();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testPlayerStarted() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlayerBasicChangeEvent.PLAYER_STARTED:
                        success = true;
                        break;
                }
            }
        });

        getPlayer().stop();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        success = false;
        loadSeveralSongs();
        getPlayer().play();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testPlayerStopped() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlayerBasicChangeEvent.PLAYER_STOPPED:
                        success = true;
                        break;
                }
            }
        });


        success = false;
        loadSeveralSongs();
        getPlayer().play();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        getPlayer().stop();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testPlayerPaused() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlayerBasicChangeEvent.PLAYER_PAUSED:
                        success = true;
                        break;
                }
            }
        });


        success = false;
        loadSeveralSongs();
        getPlayer().play();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        getPlayer().pause();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testVolumeChanged() throws MPDException, IOException {
        success = false;

        getPlayer().setVolume(0);

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        getMonitor().addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });

        loadSeveralSongs();
        getPlayer().play();

        getPlayer().setVolume(5);

        int count = 0;
        while (!success && count++ < 200) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    @Test
    public void testPlayerUnPaused() throws MPDException, IOException {
        success = false;

        getMonitor().addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getId()) {
                    case PlayerBasicChangeEvent.PLAYER_UNPAUSED:
                        success = true;
                        break;
                }
            }
        });


        success = false;
        loadSeveralSongs();
        getPlayer().play();
        getPlayer().pause();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        getPlayer().pause();

        int count = 0;
        while (!success && count++ < 100) {
            try {
                Thread.sleep(50);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
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

        int count = 0;
        while (!success && count++ < 1000) {
            try {
                Thread.sleep(60);
            } catch (InterruptedException ex) {
                Logger.getLogger(MPDStandAloneMonitorTest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        junit.framework.Assert.assertTrue(success);
    }

    /**
     * @return the monitor
     */
    public static MPDStandAloneMonitor getMonitor() {
        return monitor;
    }

    private void loadSeveralSongs() throws MPDException, IOException {
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(0));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(1));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(2));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(3));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(4));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(5));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(6));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(7));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(8));
        getPlaylist().addSong(Controller.getInstance().getDatabaseSongs().get(9));
    }
}
