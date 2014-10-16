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
    private MPDCommandExecutor testCommandExecutor;

    @Test
    public void testGetVersion() throws MPDResponseException, UnknownHostException {
        Mockito.when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
        Mockito.when(mpd.getPort()).thenReturn(8080);
        Mockito.when(mpd.getTimeout()).thenReturn(0);
        Mockito.when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", testCommandExecutor.getMPDVersion());
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandNoMPDSet() throws MPDException {
        testCommandExecutor = new MPDCommandExecutor();
        testCommandExecutor.sendCommand("command");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandObjectNoMPDSet() throws MPDException {
        testCommandExecutor = new MPDCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        testCommandExecutor.sendCommand(command);
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringParamsNoMPDSet() throws MPDException {
        testCommandExecutor = new MPDCommandExecutor();
        testCommandExecutor.sendCommand("command", "param1", "param2");
    }

    @Test(expected = MPDResponseException.class)
    public void testCommandStringIntegerParamsNoMPDSet() throws MPDException {
        testCommandExecutor = new MPDCommandExecutor();
        testCommandExecutor.sendCommand("command", 1, 2, 3);
    }

    @Test(expected = MPDResponseException.class)
    public void testGetVersionNoMPDSet() throws MPDResponseException {
        testCommandExecutor = new MPDCommandExecutor();
        testCommandExecutor.getMPDVersion();
    }

    @Test(expected = MPDResponseException.class)
    public void testSendCommandsNoMPDSet() throws MPDException {
        testCommandExecutor = new MPDCommandExecutor();
        List<MPDCommand> commands = new ArrayList<MPDCommand>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        testCommandExecutor.sendCommands(commands);
    }
}
