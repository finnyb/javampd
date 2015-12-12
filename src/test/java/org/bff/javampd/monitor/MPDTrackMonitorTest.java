package org.bff.javampd.monitor;

import org.bff.javampd.player.TrackPositionChangeEvent;
import org.bff.javampd.player.TrackPositionChangeListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MPDTrackMonitorTest {

    private TrackMonitor trackMonitor;

    @Before
    public void setUp() throws Exception {
        trackMonitor = new MPDTrackMonitor();
    }

    @Test
    public void testAddTrackPositionChangeListener() throws Exception {
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
    public void testRemoveTrackPositionChangeListener() throws Exception {
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
    public void testCheckTrackPosition() throws Exception {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("time: 1");
        trackMonitor.checkStatus();
        assertEquals(1, changeEvent[0].getElapsedTime());
    }

    @Test
    public void testCheckTrackPositionNoChange() throws Exception {
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
    public void testProcessInvalidResponse() throws Exception {
        final TrackPositionChangeEvent[] changeEvent = new TrackPositionChangeEvent[1];

        trackMonitor.addTrackPositionChangeListener(event -> changeEvent[0] = event);
        trackMonitor.processResponseStatus("bogus: 1");
        trackMonitor.checkStatus();

        assertNull(changeEvent[0]);
    }

    @Test
    public void testResetElapsedTime() throws Exception {
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
}