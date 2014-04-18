package org.bff.javampd.monitor;

import org.bff.javampd.events.MPDErrorEvent;
import org.bff.javampd.events.MPDErrorListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MPDErrorMonitorTest {
    private ErrorMonitor errorMonitor;
    private boolean success;

    @Before
    public void setUp() {
        errorMonitor = new MPDErrorMonitor();
        success = false;
    }

    @Test
    public void testCheckStatus() throws Exception {
        errorMonitor.addMPDErrorListener(new MPDErrorListener() {
            @Override
            public void errorEventReceived(MPDErrorEvent event) {
                success = true;
            }
        });
        errorMonitor.processResponseStatus("error: 1");
        errorMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        errorMonitor.processResponseStatus("error: 1");
        errorMonitor.checkStatus();
        errorMonitor.addMPDErrorListener(new MPDErrorListener() {
            @Override
            public void errorEventReceived(MPDErrorEvent event) {
                success = true;
            }
        });
        errorMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        MPDErrorListener errorListener = new MPDErrorListener() {
            @Override
            public void errorEventReceived(MPDErrorEvent event) {
                success = true;
            }
        };

        errorMonitor.addMPDErrorListener(errorListener);
        errorMonitor.processResponseStatus("error: 1");
        errorMonitor.checkStatus();

        assertTrue(success);

        success = false;

        errorMonitor.removeMPDErrorListener(errorListener);
        errorMonitor.processResponseStatus("error: 1");
        errorMonitor.checkStatus();
        assertFalse(success);
    }
}
