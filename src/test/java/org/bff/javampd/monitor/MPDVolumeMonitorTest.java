package org.bff.javampd.monitor;

import org.bff.javampd.player.VolumeChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MPDVolumeMonitorTest {

    private VolumeMonitor volumeMonitor;

    @Before
    public void setUp() throws Exception {
        volumeMonitor = new MPDVolumeMonitor();
    }

    @Test
    public void testProcessResponseStatus() throws Exception {
        final int[] volume = {0};
        volumeMonitor.addVolumeChangeListener(event -> {
            volume[0] = event.getVolume();
        });
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertEquals(1, volume[0]);
    }

    @Test
    public void testProcessResponseStatusSameVolume() throws Exception {
        final boolean[] eventFired = {false};

        volumeMonitor.addVolumeChangeListener(event -> {
            eventFired[0] = true;
        });
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertTrue(eventFired[0]);

        eventFired[0] = false;
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertFalse(eventFired[0]);
    }

    @Test
    public void testProcessResponseStatusNotVolume() throws Exception {
        final boolean[] eventFired = {false};

        volumeMonitor.addVolumeChangeListener(event -> {
            eventFired[0] = true;
        });

        volumeMonitor.processResponseStatus("bogus: 1");
        volumeMonitor.checkStatus();
        assertFalse(eventFired[0]);
    }

    @Test
    public void testRemoveVolumeChangeListener() throws Exception {
        final int[] volume = {0};

        VolumeChangeListener volumeChangeListener = event -> volume[0] = event.getVolume();

        volumeMonitor.addVolumeChangeListener(volumeChangeListener);
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertEquals(1, volume[0]);

        volumeMonitor.removeVolumeChangeListener(volumeChangeListener);

        volumeMonitor.processResponseStatus("volume: 2");
        assertEquals(1, volume[0]);

    }
}