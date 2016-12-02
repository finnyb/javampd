package org.bff.javampd.monitor;

import org.bff.javampd.player.PlayerBasicChangeEvent;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MPDPlayerMonitorTest {

    private PlayerMonitor playerMonitor;

    @Before
    public void setUp() throws Exception {
        playerMonitor = new MPDPlayerMonitor();
    }

    @Test
    public void testAddPlayerChangeListener() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STARTED, changeEvent[0].getStatus());
    }

    @Test
    public void testRemovePlayerChangeListener() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        PlayerBasicChangeListener playerBasicChangeListener = event -> changeEvent[0] = event;

        playerMonitor.addPlayerChangeListener(playerBasicChangeListener);
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STARTED, changeEvent[0].getStatus());

        changeEvent[0] = null;
        playerMonitor.removePlayerChangeListener(playerBasicChangeListener);
        playerMonitor.processResponseStatus("state: stop");
        playerMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testPlayerStarted() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: stop");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STARTED, changeEvent[0].getStatus());
    }

    @Test
    public void testPlayerStopped() throws Exception {
        processStoppedTest("state: play", "state: stop");
    }

    @Test
    public void testPlayerInvalidStatus() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: bogus");
        playerMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testPlayerInvalidStatusAfterValidStatus() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        assertNotNull(changeEvent[0]);

        changeEvent[0] = null;
        playerMonitor.processResponseStatus("state: bogus");
        playerMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testPlayerPaused() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus("state: pause");
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_PAUSED, changeEvent[0].getStatus());
    }

    @Test
    public void testPlayerPausedtoStopped() throws Exception {
        processStoppedTest("state: pause", "state: stop");
    }

    private void processStoppedTest(String from, String to) {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus(from);
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus(to);
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STOPPED, changeEvent[0].getStatus());
    }

    @Test
    public void testPlayerUnPaused() throws Exception {
        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus("state: pause");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();

        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_UNPAUSED, changeEvent[0].getStatus());
    }

    @Test
    public void testGetStatus() throws Exception {
        playerMonitor.processResponseStatus("state: play");
        playerMonitor.checkStatus();
        assertEquals(PlayerStatus.STATUS_PLAYING, playerMonitor.getStatus());
    }

    @Test
    public void testResetState() throws Exception {
        String line = "state: play";

        final PlayerBasicChangeEvent[] changeEvent = new PlayerBasicChangeEvent[1];

        playerMonitor.addPlayerChangeListener(event -> changeEvent[0] = event);

        playerMonitor.processResponseStatus(line);
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STARTED, changeEvent[0].getStatus());

        playerMonitor.reset();
        changeEvent[0] = null;

        playerMonitor.processResponseStatus(line);
        playerMonitor.checkStatus();
        assertEquals(PlayerBasicChangeEvent.Status.PLAYER_STARTED, changeEvent[0].getStatus());
    }
}