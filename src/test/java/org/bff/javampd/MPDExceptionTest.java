package org.bff.javampd;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MPDExceptionTest {
    @Test
    public void testDefaultConstructorCommand() throws Exception {
        MPDException exception = new MPDException();
        assertNull(exception.getCommand());
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void testMessageConstructorCommand() throws Exception {
        String message = "message";
        MPDException exception = new MPDException(message);
        assertNull(exception.getCommand());
        assertEquals(exception.getMessage(), message);
        assertNull(exception.getCause());
    }

    @Test
    public void testCauseConstructorCommand() throws Exception {
        String message = "message";
        Exception cause = new Exception(message);
        MPDException exception = new MPDException(cause);
        assertNull(exception.getCommand());
        assertEquals("java.lang.Exception: message", exception.getMessage());
        assertEquals(exception.getCause(), cause);
    }

    @Test
    public void testMessageCauseConstructorCommand() throws Exception {
        Exception cause = new Exception();
        String message = "message";
        MPDException exception = new MPDException(message, cause);
        assertNull(exception.getCommand());
        assertEquals(exception.getMessage(), message);
        assertEquals(exception.getCause(), cause);
    }

    @Test
    public void testMessageCauseCommandConstructorCommand() throws Exception {
        Exception cause = new Exception();
        String message = "message";
        String command = "command";
        MPDException exception = new MPDException(message, command, cause);
        assertEquals(exception.getCommand(), command);
        assertEquals(exception.getMessage(), message);
        assertEquals(exception.getCause(), cause);
    }
}