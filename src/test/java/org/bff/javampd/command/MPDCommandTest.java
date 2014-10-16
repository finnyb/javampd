package org.bff.javampd.command;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

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
}
