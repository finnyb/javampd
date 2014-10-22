package org.bff.javampd.admin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MPDAdminExceptionTest {

    @Test
    public void testDefaultConstructor() {
        MPDAdminException adminException = new MPDAdminException();
        assertEquals(null, adminException.getCause());
        assertEquals(null, adminException.getCommand());
        assertEquals(null, adminException.getMessage());
    }

    @Test
    public void testConstructor1Parm() {
        Exception exception = new Exception();
        MPDAdminException adminException = new MPDAdminException(exception);
        assertEquals(exception, adminException.getCause());
        assertEquals(null, adminException.getCommand());
        assertEquals("java.lang.Exception", adminException.getMessage());
    }

    @Test
    public void testConstructor2Parm() {
        Exception exception = new Exception();
        String testMessage = "testMessage";
        MPDAdminException adminException = new MPDAdminException(testMessage, exception);
        assertEquals(exception, adminException.getCause());
        assertEquals(null, adminException.getCommand());
        assertEquals(testMessage, adminException.getMessage());
    }

    @Test
    public void testConstructor3Parm() {
        Exception exception = new Exception();
        String testMessage = "testMessage";
        String testCommand = "testCommand";
        MPDAdminException adminException = new MPDAdminException(testMessage, testCommand, exception);
        assertEquals(exception, adminException.getCause());
        assertEquals(testCommand, adminException.getCommand());
        assertEquals(testMessage, adminException.getMessage());
    }
}