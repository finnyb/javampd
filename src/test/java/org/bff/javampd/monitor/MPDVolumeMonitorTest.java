package org.bff.javampd.monitor;

import org.bff.javampd.events.VolumeChangeEvent;
import org.bff.javampd.events.VolumeChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDVolumeMonitorTest {
    private VolumeMonitor volumeMonitor;
    private boolean success;

    @Before
    public void setUp() {
        volumeMonitor = new MPDVolumeMonitor();
        success = false;
    }

    @Test
    public void testCheckStatus() throws Exception {
        volumeMonitor.addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        volumeMonitor.addVolumeChangeListener(new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        });
        volumeMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        VolumeChangeListener volumeChangeListener = new VolumeChangeListener() {

            @Override
            public void volumeChanged(VolumeChangeEvent event) {
                success = true;
            }
        };

        volumeMonitor.addVolumeChangeListener(volumeChangeListener);
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();

        assertTrue(success);

        success = false;

        volumeMonitor.removeVolumeChangedListener(volumeChangeListener);
        volumeMonitor.processResponseStatus("volume: 1");
        volumeMonitor.checkStatus();
        assertFalse(success);
    }
}
