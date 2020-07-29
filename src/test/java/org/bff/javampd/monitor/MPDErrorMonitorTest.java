package org.bff.javampd.monitor;

import org.bff.javampd.server.ErrorEvent;
import org.bff.javampd.server.ErrorListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MPDErrorMonitorTest {
    private ErrorMonitor errorMonitor;

    @BeforeEach
    public void setUp() {
        errorMonitor = new MPDErrorMonitor();
    }

    @Test
    public void testAddErrorListener() {
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        errorMonitor.addErrorListener(event -> errorEvent[0] = event);
        errorMonitor.processResponseStatus("error: message");
        errorMonitor.checkStatus();
        assertEquals("message", errorEvent[0].getMessage());
    }

    @Test
    public void testRemoveErrorListener() {
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
    public void testInvalidStatus() {
        final ErrorEvent[] errorEvent = new ErrorEvent[1];

        errorMonitor.addErrorListener(event -> errorEvent[0] = event);
        errorMonitor.processResponseStatus("bogus: message");
        errorMonitor.checkStatus();

        assertNull(errorEvent[0]);
    }

    @Test
    public void testResetError() {
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