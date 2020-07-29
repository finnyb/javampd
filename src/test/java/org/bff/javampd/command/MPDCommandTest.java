package org.bff.javampd.command;

import org.bff.javampd.server.MPDConnectionException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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
    public void testEqualsNoParams() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1, command2);
    }

    @Test
    public void testEqualsSingleParam() {
        MPDCommand command1 = new MPDCommand("command1", "param1");
        MPDCommand command2 = new MPDCommand("command1", "param1");

        assertEquals(command1, command2);
    }

    @Test
    public void testEqualsMultipleParams() {
        MPDCommand command1 = new MPDCommand("command1", "param1", "param2");
        MPDCommand command2 = new MPDCommand("command1", "param1", "param2");

        assertEquals(command1, command2);
    }

    @Test
    public void testNotEquals() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2");

        assertNotEquals(command1, command2);
    }

    @Test
    public void testEqualsNull() {
        MPDCommand command = new MPDCommand("command");

        assertNotEquals(command, null);
    }

    @Test
    public void testEqualsSameObject() {
        MPDCommand item = new MPDCommand("command");

        assertEquals(item, item);
    }

    @Test
    public void testEqualsDifferentClass() {
        MPDCommand command = new MPDCommand("command");

        assertNotEquals("", command);
    }

    @Test
    public void testHashCode() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void testNullException() {
        assertThrows(IllegalArgumentException.class, () -> new MPDCommand(null));
    }
}
