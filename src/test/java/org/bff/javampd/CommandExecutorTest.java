package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.net.InetAddress;
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

    @Before
    public void before() {
    }

    @Test
    public void testGetVersion() throws MPDConnectionException, IOException {
        when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
        when(mpd.getPort()).thenReturn(8080);
        when(mpd.getTimeout()).thenReturn(0);
        when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", testCommandExecutor.getMPDVersion());
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandNoMPDSet() throws MPDResponseException, MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command");
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandObjectNoMPDSet() throws MPDResponseException, MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        testCommandExecutor.sendCommand(command);
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandStringParamsNoMPDSet() throws MPDResponseException, MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command", "param1", "param2");
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandStringIntegerParamsNoMPDSet() throws MPDResponseException, MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.sendCommand("command", 1, 2, 3);
    }

    @Test(expected = MPDConnectionException.class)
    public void testGetVersionNoMPDSet() throws MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        testCommandExecutor.getMPDVersion();
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandsNoMPDSet() throws MPDResponseException, MPDConnectionException {
        testCommandExecutor = new TestCommandExecutor();
        List<MPDCommand> commands = new ArrayList<MPDCommand>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        testCommandExecutor.sendCommands(commands);
    }
}
