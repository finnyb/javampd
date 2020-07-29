package org.bff.javampd.command;

import org.bff.javampd.server.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MPDCommandExecutorTest {
    @Mock
    private MPDSocket mpdSocket;
    @Mock
    private MPD mpd;
    @InjectMocks
    private MPDCommandExecutor commandExecutor;

    @Test
    public void testGetVersion() {
        when(mpdSocket.getVersion()).thenReturn("version");
        assertEquals("version", commandExecutor.getMPDVersion());
    }

    @Test
    public void testSendCommandNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.sendCommand("command"));
    }

    @Test
    public void testSendCommandString() {
        String commandString = "command";
        MPDCommand command = new MPDCommand(commandString);
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);
        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");
        when(mpdSocket.sendCommand(command))
                .thenReturn(testResponse);

        List<String> response = commandExecutor.sendCommand(commandString);

        assertEquals(response.get(0), testResponse.get(0));
    }

    @Test
    public void testSendCommand() {
        MPDCommand command = new MPDCommand("command");
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);
        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");
        when(mpdSocket.sendCommand(command))
                .thenReturn(testResponse);

        List<String> response = commandExecutor.sendCommand(command);

        assertEquals(response.get(0), testResponse.get(0));
    }

    @Test
    public void testSendCommandWithStringParams() {
        String commandString = "command";
        String paramString = "param";
        MPDCommand command = new MPDCommand(commandString, paramString);
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);
        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");
        when(mpdSocket.sendCommand(command))
                .thenReturn(testResponse);

        List<String> response = commandExecutor.sendCommand(commandString, paramString);

        assertEquals(response.get(0), testResponse.get(0));
    }

    @Test
    public void testSendCommandWithIntegerParams() {
        String commandString = "command";
        int paramInteger = 1;
        MPDCommand command = new MPDCommand(commandString, Integer.toString(paramInteger));
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);
        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");
        when(mpdSocket.sendCommand(command))
                .thenReturn(testResponse);

        List<String> response = commandExecutor.sendCommand(commandString, paramInteger);

        assertEquals(response.get(0), testResponse.get(0));
    }

    @Test
    public void testSendCommandSecurityException() {
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);

        MPDCommand command = new MPDCommand("command");

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

        doThrow(new MPDSecurityException("exception"))
                .doNothing()
                .when(mpdSocket).sendCommands(commands);

        commandExecutor.sendCommands(commands);
    }

    @Test
    public void testSendCommands() {
        commandExecutor = new TestMPDCommandExecutor();
        commandExecutor.setMpd(mpd);

        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2");

        List<MPDCommand> commands = new ArrayList<>();
        commands.add(command1);
        commands.add(command2);

        doNothing().when(mpdSocket).sendCommands(commands);

        commandExecutor.sendCommands(commands);
    }

    @Test
    public void testCreateSocket() throws IOException {
        try (ServerSocket socket = new ServerSocket(0)) {
            socket.setReuseAddress(true);
            int port = socket.getLocalPort();
            when(mpd.getAddress()).thenReturn(InetAddress.getLocalHost());
            when(mpd.getPort()).thenReturn(port);
            when(mpd.getTimeout()).thenReturn(5000);

            ServerSocket finalSocket = socket;
            new Thread(() -> {
                Socket clientSocket;
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
        }
    }

    @Test
    public void testCommandObjectNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        MPDCommand command = new MPDCommand("command");
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.sendCommand(command));
    }

    @Test
    public void testCommandStringParamsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.sendCommand("command", "param1", "param2"));
    }

    @Test
    public void testCommandStringIntegerParamsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.sendCommand("command", 1, 2, 3));
    }

    @Test
    public void testGetVersionNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.getMPDVersion());
    }

    @Test
    public void testSendCommandsNoMPDSet() {
        commandExecutor = new MPDCommandExecutor();
        List<MPDCommand> commands = new ArrayList<>();
        commands.add(new MPDCommand("command1"));
        commands.add(new MPDCommand("command2"));
        commands.add(new MPDCommand("command3"));
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.sendCommands(commands));
    }

    @Test
    public void testAuthenticateIllegalArgument() {
        assertThrows(IllegalArgumentException.class,
                () -> commandExecutor.usePassword(null));
    }

    @Test
    public void testAuthentication() {
        String password = "password";
        ServerProperties serverProperties = new ServerProperties();
        MPDCommand command = new MPDCommand(serverProperties.getPassword(), password);

        List<String> testResponse = new ArrayList<>();
        testResponse.add("testResponse");

        when(mpdSocket.sendCommand(command)).thenReturn(testResponse);

        commandExecutor.usePassword(password);
        commandExecutor.authenticate();
    }

    @Test
    public void testAuthenticateSecurityException() {
        String password = "password";
        ServerProperties serverProperties = new ServerProperties();
        MPDCommand command = new MPDCommand(serverProperties.getPassword(), password);
        when(mpdSocket.sendCommand(command)).thenThrow(new RuntimeException("incorrect password"));
        commandExecutor.usePassword(password);
        assertThrows(MPDSecurityException.class,
                () -> commandExecutor.authenticate());
    }

    @Test
    public void testAuthenticateGeneralException() {
        String password = "password";
        ServerProperties serverProperties = new ServerProperties();
        MPDCommand command = new MPDCommand(serverProperties.getPassword(), password);
        when(mpdSocket.sendCommand(command)).thenThrow(new RuntimeException());
        commandExecutor.usePassword(password);
        assertThrows(MPDConnectionException.class,
                () -> commandExecutor.authenticate());
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
