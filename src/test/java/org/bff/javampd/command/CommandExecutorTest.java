package org.bff.javampd.command;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDResponseException;
import org.bff.javampd.server.MPDSocket;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CommandExecutorTest {
    @Mock
    private MPDSocket mpdSocket;

    @Mock
    private MPD mpd;

    @InjectMocks
    private MPDCommandExecutor commandExecutor;

    @Test
    public void testGetVersion() throws MPDResponseException, UnknownHostException {
        Mockito.when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
        Mockito.when(mpd.getPort()).thenReturn(8080);
        Mockito.when(mpd.getTimeout()).thenReturn(0);
        Mockito.when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", commandExecutor.getMPDVersion());
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandNoMPDSet() throws MPDException {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandObjectNoMPDSet() throws MPDException {
        commandExecutor = new MPDCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        commandExecutor.sendCommand(command);
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringParamsNoMPDSet() throws MPDException {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command", "param1", "param2");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringIntegerParamsNoMPDSet() throws MPDException {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command", 1, 2, 3);
    }

    @Test(expected = MPDResponseException.class)
    public void testGetVersionNoMPDSet() throws MPDResponseException {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.getMPDVersion();
    }

    @Test(expected = MPDResponseException.class)
    public void testSendCommandsNoMPDSet() throws MPDException {
        commandExecutor = new MPDCommandExecutor();
        List<MPDCommand> commands = new ArrayList<MPDCommand>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        commandExecutor.sendCommands(commands);
    }
}
