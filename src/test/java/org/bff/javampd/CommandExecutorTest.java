package org.bff.javampd;

import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorTest {
    @Mock
    private MPDSocket mpdSocket;

    @Mock
    private MPD mpd;

    @InjectMocks
    private TestCommandExecutor testCommandExecutor;

    @Test
    public void testGetVersion() throws MPDResponseException, UnknownHostException {
        when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
        when(mpd.getPort()).thenReturn(8080);
        when(mpd.getTimeout()).thenReturn(0);
        when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", testCommandExecutor.getMPDVersion());
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandNoMPDSet() throws MPDException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandObjectNoMPDSet() throws MPDException {
        testCommandExecutor = new TestCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        testCommandExecutor.sendCommand(command);
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringParamsNoMPDSet() throws MPDException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command", "param1", "param2");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringIntegerParamsNoMPDSet() throws MPDException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command", 1, 2, 3);
    }

    @Test(expected = MPDResponseException.class)
    public void testGetVersionNoMPDSet() throws MPDResponseException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.getMPDVersion();
    }

    @Test(expected = MPDResponseException.class)
    public void testSendCommandsNoMPDSet() throws MPDException {
        testCommandExecutor = new TestCommandExecutor();
        List<MPDCommand> commands = new ArrayList<MPDCommand>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        testCommandExecutor.sendCommands(commands);
    }
}
