package org.bff.javampd.monitor;

import org.bff.javampd.player.TrackPositionChangeEvent;
import org.bff.javampd.player.TrackPositionChangeListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class MPDTrackMonitorTest {

    private TrackMonitor trackMonitor;

    @BeforeEach
    void setUp() {
        trackMonitor = new MPDTrackMonitor();
    }

    @Test
    void testAddTrackPositionChangeListener() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];
        final TrackPositionChangeEvent[] changeEvent2 = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.addTrackPositionChangeListener(event -> changeEvent2[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();

        assertEquals(1, changeEvent[0].getElapsedTime());
        assertEquals(1, changeEvent2[0].getElapsedTime());
    }

    @Test
    void testRemoveTrackPositionChangeListener() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        TrackPositionChangeListener trackPositionChangeListener = event -> changeEvent[0] = event;

        trackMonitor.addTrackPositionChangeListener(trackPositionChangeListener);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());

        changeEvent[0] = null;
        trackMonitor.removeTrackPositionChangeListener(trackPositionChangeListener);
        trackMonitor.processResponseStatus("time: 2");
        trackMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    void testCheckTrackPosition() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());
    }

    @Test
    void testCheckTrackPositionNoChange() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());

        changeEvent[0] = null;
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    void testProcessInvalidResponse() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("bogus: 1");
        trackMonitor.checkStatus();

        assertNull(changeEvent[0]);
    }

    @Test
    void testResetElapsedTime() {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());

        changeEvent[0] = null;
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertNull(changeEvent[0]);

        trackMonitor.resetElapsedTime();

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());
    }

    @Test
    void testResetTrackPositionChange() {
        String line = "time: 1";

        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus(line);
        trackMonitor.checkStatus();

        assertEquals(1, changeEvent[0].getElapsedTime());

        trackMonitor.reset();
        changeEvent[0] = null;

        trackMonitor.processResponseStatus(line);
        trackMonitor.checkStatus();

        assertEquals(1, changeEvent[0].getElapsedTime());
    }

    @Test
    void testMonitorResetElapsedTime() {
        String line = "time: 1";

        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);

        trackMonitor.processResponseStatus(line);
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());

        changeEvent[0] = null;
        trackMonitor.processResponseStatus(line);
        trackMonitor.checkStatus();
        assertNull(changeEvent[0]);

        trackMonitor.reset();

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus(line);
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());
    }
}
