package org.bff.javampd.command;

import org.bff.javampd.server.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

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
    public void testSendCommandNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        commandExecutor.sendCommand("command");
    }

    @Test
    public void testSendCommandSecurityException() {
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);

        MPDCommand command = new MPDCommand("command");
        MPDCommand passwordCommand = new MPDCommand("password", "password");

        when(mpdSocket.sendCommand(passwordCommand))
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>());

        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");

        when(mpdSocket.sendCommand(command))
                .thenThrow(new MPDSecurityException("exception"))
                .thenReturn(testResponse);
        List<String> response = new ArrayList<>(commandExecutor.sendCommand(command));
        assertEquals(response.get(0), testResponse.get(0));
    }

    @Test
    public void testSendCommandsSecurityException() {
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);

        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2");

        List<MPDCommand> commands = new ArrayList<>();
        commands.add(command1);
        commands.add(command2);

        MPDCommand passwordCommand = new MPDCommand("password", "password");
        when(mpdSocket.sendCommand(passwordCommand))
                .thenReturn(new ArrayList<>())
                .thenReturn(new ArrayList<>());

        doThrow(new MPDSecurityException("exception"))
                .doNothing()
                .when(mpdSocket).sendCommands(commands);

        commandExecutor.sendCommands(commands);
    }

    @Test
    public void testCreateSocket() throws Exception {
        ServerSocket socket = null;
        try {
            socket = new ServerSocket(0);
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
            when(mpd.getPort()).thenReturn(port);
            when(mpd.getTimeout()).thenReturn(5000);

            ServerSocket finalSocket = socket;
            new Thread(() -> {
                Socket clientSocket = null;
                try {
                    clientSocket = finalSocket.accept();
                    PrintWriter pw = new PrintWriter(clientSocket.getOutputStream(), true);
                    pw.write("OK MPD Version\r\n");
                    pw.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            commandExecutor.setMpd(mpd);
            assertNotNull(commandExecutor.createSocket());
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
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

    @Test
    public void testClose() {
        commandExecutor.close();
        verify(mpdSocket).close();
    }

    private class TestMPDCommandExecutor extends MPDCommandExecutor {
        @Override
        protected MPDSocket createSocket() {
            return mpdSocket;
        }
    }
}
