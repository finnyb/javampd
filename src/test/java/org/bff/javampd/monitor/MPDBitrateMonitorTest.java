package org.bff.javampd.monitor;

import org.bff.javampd.events.PlayerBasicChangeEvent;
import org.bff.javampd.events.PlayerBasicChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDBitrateMonitorTest {

    private BitrateMonitor bitrateMonitor;
    private boolean success;
    private static final String RESPONSE = "bitrate: ";

    @Before
    public void setUp() {
        bitrateMonitor = new MPDBitrateMonitor();
        success = false;
    }

    @Test
    public void testCheckStatus() throws Exception {
        bitrateMonitor.processResponseStatus(RESPONSE + "1");
        bitrateMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {
            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_BITRATE_CHANGE:
                        success = true;
                }
            }
        });
        bitrateMonitor.processResponseStatus(RESPONSE + "2");
        bitrateMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        bitrateMonitor.processResponseStatus(RESPONSE + "1");
        bitrateMonitor.checkStatus();
        bitrateMonitor.addPlayerChangeListener(new PlayerBasicChangeListener() {
            @Override
            public void playerBasicChange(PlayerBasicChangeEvent event) {
                switch (event.getStatus()) {
                    case PLAYER_BITRATE_CHANGE:
                        success = true;
                }
            }
        });
        bitrateMonitor.checkStatus();
        bitrateMonitor.processResponseStatus(RESPONSE + "1");
        assertFalse(success);
    }
}
