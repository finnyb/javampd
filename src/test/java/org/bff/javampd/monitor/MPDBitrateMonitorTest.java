package org.bff.javampd.monitor;

import org.bff.javampd.player.BitrateChangeEvent;
import org.bff.javampd.player.BitrateChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MPDBitrateMonitorTest {

    private BitrateMonitor bitrateMonitor;

    @Before
    public void setup() {
        bitrateMonitor = new MPDBitrateMonitor();
    }

    @Test
    public void testAddBitrateChangeListener() throws Exception {
        final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

        bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
        bitrateMonitor.processResponseStatus("bitrate: 1");
        bitrateMonitor.checkStatus();
        assertEquals(0, changeEvent[0].getOldBitrate());
        assertEquals(1, changeEvent[0].getNewBitrate());
    }

    @Test
    public void testRemoveBitrateChangeListener() throws Exception {
        final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

        BitrateChangeListener bitrateChangeListener = event -> changeEvent[0] = event;

        bitrateMonitor.addBitrateChangeListener(bitrateChangeListener);
        bitrateMonitor.processResponseStatus("bitrate: 1");
        bitrateMonitor.checkStatus();
        assertEquals(0, changeEvent[0].getOldBitrate());
        assertEquals(1, changeEvent[0].getNewBitrate());

        changeEvent[0] = null;
        bitrateMonitor.removeBitrateChangeListener(bitrateChangeListener);
        bitrateMonitor.processResponseStatus("bitrate: 2");
        bitrateMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testBitrateNoChange() throws Exception {
        final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

        bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
        bitrateMonitor.processResponseStatus("bitrate: 1");
        bitrateMonitor.checkStatus();
        assertEquals(0, changeEvent[0].getOldBitrate());
        assertEquals(1, changeEvent[0].getNewBitrate());

        changeEvent[0] = null;
        bitrateMonitor.processResponseStatus("bitrate: 1");
        bitrateMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testResetBitrateChangeListener() throws Exception {
        String line = "bitrate: 1";

        final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

        bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
        bitrateMonitor.processResponseStatus(line);
        bitrateMonitor.checkStatus();
        assertEquals(0, changeEvent[0].getOldBitrate());
        assertEquals(1, changeEvent[0].getNewBitrate());

        bitrateMonitor.reset();
        changeEvent[0] = null;

        bitrateMonitor.processResponseStatus(line);
        bitrateMonitor.checkStatus();
        assertEquals(0, changeEvent[0].getOldBitrate());
        assertEquals(1, changeEvent[0].getNewBitrate());

    }
}