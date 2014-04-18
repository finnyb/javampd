package org.bff.javampd.monitor;

import org.bff.javampd.events.PlayerBasicChangeEvent;
import org.bff.javampd.events.PlayerBasicChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDPlayerMonitorTest {
    private boolean success;
    private PlayerMonitor playerMonitor;

    private static final String RESPONSE = "state: ";

    @Before
    public void setUp() {
        playerMonitor = new MPDPlayerMonitor();
        success = false;
    }

    @Test
    public void testCheckStatusPaused() throws Exception {
        playerMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_PAUSED;
            }
        });
        playerMonitor.processResponseStatus(RESPONSE + "play");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus(RESPONSE + "paused");
        playerMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusUnPaused() throws Exception {
        playerMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_UNPAUSED;
            }
        });
        playerMonitor.processResponseStatus(RESPONSE + "paused");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus(RESPONSE + "play");
        playerMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusPlaying() throws Exception {
        playerMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_STARTED;
            }
        });
        playerMonitor.processResponseStatus(RESPONSE + "play");
        playerMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusStopped() throws Exception {
        playerMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_STOPPED;
            }
        });
        playerMonitor.processResponseStatus(RESPONSE + "play");
        playerMonitor.checkStatus();
        playerMonitor.processResponseStatus(RESPONSE + "stop");
        playerMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        playerMonitor.processResponseStatus(RESPONSE + "stop");
        playerMonitor.checkStatus();
        playerMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_STOPPED;
            }
        });
        playerMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        PlayerBasicChangeListener trackPositionChangeListener = new PlayerBasicChangeListener() {

            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                success = true;
            }
        };

        playerMonitor.addPlayerChangeListener(trackPositionChangeListener);
        playerMonitor.processResponseStatus(RESPONSE + "play");
        playerMonitor.checkStatus();

        assertTrue(success);

        success = false;

        playerMonitor.removePlayerChangeListener(trackPositionChangeListener);
        playerMonitor.processResponseStatus(RESPONSE + "stop");
        playerMonitor.checkStatus();
        assertFalse(success);
    }
}
