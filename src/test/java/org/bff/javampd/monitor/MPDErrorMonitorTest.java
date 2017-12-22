package org.bff.javampd.monitor;

import org.bff.javampd.server.ErrorEvent;
import org.bff.javampd.server.ErrorListener;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MPDErrorMonitorTest {
    private ErrorMonitor errorMonitor;

    @Before
    public void setUp() throws Exception {
        errorMonitor = new MPDErrorMonitor();
    }

    @Test
    public void testAddErrorListener() throws Exception {
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        errorMonitor.addErrorListener(event -> errorEvent[0] = event);
        errorMonitor.processResponseStatus("error: message");
        errorMonitor.checkStatus();
        assertEquals("message", errorEvent[0].getMessage());
    }

    @Test
    public void testRemoveErrorListener() throws Exception {
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        ErrorListener errorListener = event -> errorEvent[0] = event;

        errorMonitor.addErrorListener(errorListener);
        errorMonitor.processResponseStatus("error: message");
        errorMonitor.checkStatus();
        assertEquals("message", errorEvent[0].getMessage());

        errorEvent[0] = null;
        errorMonitor.removeErrorListener(errorListener);
        errorMonitor.processResponseStatus("error: message2");
        errorMonitor.checkStatus();
        assertNull(errorEvent[0]);
    }

    @Test
    public void testInvalidStatus() throws Exception {
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        errorMonitor.addErrorListener(event -> errorEvent[0] = event);
        errorMonitor.processResponseStatus("bogus: message");
        errorMonitor.checkStatus();

        assertNull(errorEvent[0]);
    }

    @Test
    public void testResetError() throws Exception {
        String line = "error: message";
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        errorMonitor.addErrorListener(event -> errorEvent[0] = event);
        errorMonitor.processResponseStatus(line);
        errorMonitor.checkStatus();
        assertEquals("message", errorEvent[0].getMessage());

        errorMonitor.reset();
        errorEvent[0] = null;

        errorMonitor.processResponseStatus(line);
        errorMonitor.checkStatus();
        assertEquals("message", errorEvent[0].getMessage());

    }
}