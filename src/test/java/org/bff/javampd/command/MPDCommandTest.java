package org.bff.javampd.command;

import org.junit.Test;

import static org.junit.Assert.*;

public class MPDCommandTest {

    @Test
    public void testCommand() {
        MPDCommand mpdCommand = new MPDCommand("command");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    public void testCommandWithOneParm() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    public void testCommandWithParms() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    public void testParmsWithNoParam() {
        MPDCommand mpdCommand = new MPDCommand("command");
        assertEquals(0, mpdCommand.getParams().size());
    }

    @Test
    public void testParmsWithOneParm() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm");
        assertEquals(1, mpdCommand.getParams().size());
        assertEquals("parm", mpdCommand.getParams().get(0));
    }

    @Test
    public void testParmsWithMultipleParms() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
        assertEquals(2, mpdCommand.getParams().size());
        assertEquals("parm1", mpdCommand.getParams().get(0));
        assertEquals("parm2", mpdCommand.getParams().get(1));
    }

    @Test
    public void testEqualsNoParams() throws Exception {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1, command2);
    }

    @Test
    public void testEqualsSingleParam() throws Exception {
        MPDCommand command1 = new MPDCommand("command1", "param1");
        MPDCommand command2 = new MPDCommand("command1", "param1");

        assertEquals(command1, command2);
    }

    @Test
    public void testEqualsMultipleParams() throws Exception {
        MPDCommand command1 = new MPDCommand("command1", "param1", "param2");
        MPDCommand command2 = new MPDCommand("command1", "param1", "param2");

        assertEquals(command1, command2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2");

        assertNotEquals(command1, command2);
    }

    @Test
    public void testEqualsNull() throws Exception {
        MPDCommand command = new MPDCommand("command");

        assertNotEquals(command, null);
    }

    @Test
    public void testEqualsSameObject() throws Exception {
        MPDCommand item = new MPDCommand("command");

        assertTrue(item.equals(item));
    }

    @Test
    public void testEqualsDifferentClass() throws Exception {
        MPDCommand command = new MPDCommand("command");

        assertFalse(command.equals(new String()));
    }

    @Test
    public void testHashCode() throws Exception {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullException() throws Exception {
        new MPDCommand(null);
    }
}
