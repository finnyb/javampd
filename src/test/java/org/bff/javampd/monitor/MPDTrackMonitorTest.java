package org.bff.javampd.monitor;

import org.bff.javampd.events.TrackPositionChangeEvent;
import org.bff.javampd.events.TrackPositionChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDTrackMonitorTest {
    private TrackMonitor trackMonitor;
    private boolean success;

    @Before
    public void setUp() {
        trackMonitor = new MPDTrackMonitor();
        success = false;
    }

    @Test
    public void testCheckStatus() throws Exception {
        trackMonitor.addTrackPositionChangeListener(new TrackPositionChangeListener() {

            @Override
            public void trackPositionChanged(TrackPositionChangeEvent event) {
                success = true;
            }
        });
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusAfterReset() throws Exception {
        trackMonitor.addTrackPositionChangeListener(new TrackPositionChangeListener() {

            @Override
            public void trackPositionChanged(TrackPositionChangeEvent event) {
                success = true;
            }
        });

        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        trackMonitor.resetElapsedTime();
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        trackMonitor.addTrackPositionChangeListener(new TrackPositionChangeListener() {

            @Override
            public void trackPositionChanged(TrackPositionChangeEvent event) {
                success = true;
            }
        });
        trackMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        TrackPositionChangeListener trackPositionChangeListener = new TrackPositionChangeListener() {

            @Override
            public void trackPositionChanged(TrackPositionChangeEvent event) {
                success = true;
            }
        };

        trackMonitor.addTrackPositionChangeListener(trackPositionChangeListener);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();

        assertTrue(success);

        success = false;

        trackMonitor.removeTrackPositionChangeListener(trackPositionChangeListener);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertFalse(success);
    }
}
