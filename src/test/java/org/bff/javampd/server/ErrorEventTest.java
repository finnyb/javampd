package org.bff.javampd.server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ErrorEventTest {
    @Test
    public void getMessage() throws Exception {
        String message = "message";
        ErrorEvent errorEvent = new ErrorEvent(this, message);

        assertEquals(errorEvent.getMessage(), message);
    }

    @Test
    public void getSource() throws Exception {
        Object source = new Object();
        ErrorEvent errorEvent = new ErrorEvent(source);

        assertEquals(errorEvent.getSource(), source);
    }

    @Test
    public void getSourceAndMessage() throws Exception {
        Object source = new Object();
        String message = "message";
        ErrorEvent errorEvent = new ErrorEvent(source, message);

        assertEquals(errorEvent.getSource(), source);
        assertEquals(errorEvent.getMessage(), message);
    }
}