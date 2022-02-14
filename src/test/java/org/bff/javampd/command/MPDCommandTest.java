package org.bff.javampd.command;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MPDCommandTest {

    @Test
    void testCommand() {
        MPDCommand mpdCommand = new MPDCommand("command");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    void testCommandWithOneParm() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    void testCommandWithParms() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
        assertEquals("command", mpdCommand.getCommand());
    }

    @Test
    void testParmsWithNoParam() {
        MPDCommand mpdCommand = new MPDCommand("command");
        assertEquals(0, mpdCommand.getParams().size());
    }

    @Test
    void testParmsWithOneParm() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm");
        assertEquals(1, mpdCommand.getParams().size());
        assertEquals("parm", mpdCommand.getParams().get(0));
    }

    @Test
    void testParmsWithMultipleParms() {
        MPDCommand mpdCommand = new MPDCommand("command", "parm1", "parm2");
        assertEquals(2, mpdCommand.getParams().size());
        assertEquals("parm1", mpdCommand.getParams().get(0));
        assertEquals("parm2", mpdCommand.getParams().get(1));
    }

    @Test
    void testEqualsNoParams() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1, command2);
    }

    @Test
    void testEqualsSingleParam() {
        MPDCommand command1 = new MPDCommand("command1", "param1");
        MPDCommand command2 = new MPDCommand("command1", "param1");

        assertEquals(command1, command2);
    }

    @Test
    void testEqualsMultipleParams() {
        MPDCommand command1 = new MPDCommand("command1", "param1", "param2");
        MPDCommand command2 = new MPDCommand("command1", "param1", "param2");

        assertEquals(command1, command2);
    }

    @Test
    void testNotEquals() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2");

        assertNotEquals(command1, command2);
    }

    @Test
    void testEqualsNull() {
        MPDCommand command = new MPDCommand("command");

        assertNotEquals(null, command);
    }

    @Test
    void testEqualsSameObject() {
        MPDCommand item = new MPDCommand("command");

        assertEquals(item, item);
    }

    @Test
    void testEqualsDifferentClass() {
        MPDCommand command = new MPDCommand("command");

        assertNotEquals("", command);
    }

    @Test
    void testHashCode() {
        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command1");

        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    void testNullException() {
        assertThrows(IllegalArgumentException.class, () -> new MPDCommand(null));
    }
}
