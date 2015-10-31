package org.bff.javampd.command;

import org.bff.javampd.server.*;
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
public class MPDCommandExecutorTest {
    @Mock
    private MPDSocket mpdSocket;

    @Mock
    private MPD mpd;

    @InjectMocks
    private MPDCommandExecutor commandExecutor;

    @Test
    public void testGetVersion() throws UnknownHostException {
        when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
        when(mpd.getPort()).thenReturn(8080);
        when(mpd.getTimeout()).thenReturn(0);
        when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", commandExecutor.getMPDVersion());
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command");
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandObjectNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        commandExecutor.sendCommand(command);
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandStringParamsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command", "param1", "param2");
    }

    @Test(expected = MPDConnectionException.class)
    public void testCommandStringIntegerParamsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command", 1, 2, 3);
    }

    @Test(expected = MPDConnectionException.class)
    public void testGetVersionNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.getMPDVersion();
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        List<MPDCommand> commands = new ArrayList<MPDCommand>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        commandExecutor.sendCommands(commands);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAuthenticateIllegalArgument() throws Exception {
        commandExecutor.usePassword(null);
    }

    @Test(expected = MPDSecurityException.class)
    public void testAuthenticateSecurityException() {
        String password = "password";
        ServerProperties serverProperties = new ServerProperties();
        MPDCommand command = new MPDCommand(serverProperties.getPassword(), password);
        when(mpdSocket.sendCommand(command)).thenThrow(new RuntimeException("incorrect password"));
        commandExecutor.usePassword(password);
        commandExecutor.authenticate();
    }

    @Test(expected = MPDConnectionException.class)
    public void testAuthenticateGeneralException() {
        String password = "password";
        ServerProperties serverProperties = new ServerProperties();
        MPDCommand command = new MPDCommand(serverProperties.getPassword(), password);
        when(mpdSocket.sendCommand(command)).thenThrow(new RuntimeException());
        commandExecutor.usePassword(password);
        commandExecutor.authenticate();
    }
}
