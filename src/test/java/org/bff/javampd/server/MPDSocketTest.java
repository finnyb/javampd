package org.bff.javampd.server;

import org.bff.javampd.command.MPDCommand;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MPDSocketTest {

    private MPDSocket socket;

    private Socket mockSocket;
    private InputStream mockedInputStream;
    private OutputStream mockedOutputStream;
    private BufferedReader mockedBufferedReader;
    private static final String VERSION_RESPONSE = "OK MPD 0.18.0";

    private ArgumentCaptor<byte[]> byteArgumentCaptor;

    @Test
    void testSocketCreationWithError() throws IOException {
        mockSocket = mock(Socket.class);
        mockedInputStream = mock(InputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        InetAddress inetAddress = InetAddress.getByName("localhost");
        assertThrows(MPDConnectionException.class, () -> new TestSocket(inetAddress, 9999, 10));
    }

    @Test
    void testSocketCreationWithConnectError() throws IOException {
        mockSocket = mock(Socket.class);
        mockedInputStream = mock(InputStream.class);

        InetAddress inetAddress = InetAddress.getByName("localhost");
        int timeout = 10;
        int port = 9999;

        doThrow(new RuntimeException())
                .when(mockSocket)
                .connect(new InetSocketAddress(inetAddress, port), timeout);

        assertThrows(MPDConnectionException.class, () -> new TestSocket(inetAddress, port, timeout));
    }

    @Test
    void testSocketCreationWithBadResponse() throws IOException {
        mockSocket = mock(Socket.class);
        mockedBufferedReader = mock(BufferedReader.class);

        mockedInputStream = new ByteArrayInputStream("NOTOK MPD 0.18.0".getBytes());
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine()).thenReturn("Bad");
        InetAddress inetAddress = InetAddress.getByName("localhost");
        assertThrows(MPDConnectionException.class, () -> new TestSocket(inetAddress, 9999, 10));
    }

    @Test
    void testSendCommand() throws IOException {
        String testResponse = "testResponse";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(testResponse, response.get(0));
    }

    @Test
    void testSendCommandOKButNoResponse() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn(null)
                .thenReturn("OK")
                .thenReturn(null)
                .thenReturn("OK")
                .thenReturn(null);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(0, response.size());
    }

    @Test
    void testSendCommandSocketException() throws IOException {
        String testResponse = "testResponse";
        createValidSocket();

        mockedInputStream = new ByteArrayInputStream(VERSION_RESPONSE.getBytes());
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine())
                .thenReturn(VERSION_RESPONSE)
                .thenThrow(new SocketException())
                .thenReturn(VERSION_RESPONSE)
                .thenReturn(testResponse)
                .thenReturn(null);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(testResponse, response.get(0));
    }

    @Test
    void testSendCommandNoPermissionResponse() throws IOException {
        String testResponse = "ACK: you don't have permission";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        assertThrows(MPDSecurityException.class,
                () -> socket.sendCommand(new MPDCommand("command", "params")));
    }

    @Test
    void testSendCommandsSecurityException() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new MPDSecurityException("security exception"));

        List<MPDCommand> commands = new ArrayList<>();
        commands.add(new MPDCommand("command", "params"));

        assertThrows(MPDSecurityException.class, () -> socket.sendCommands(commands));
    }

    @Test
    void testSendCommandsException() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new RuntimeException("exception"));

        List<MPDCommand> commands = new ArrayList<>();
        commands.add(new MPDCommand("command", "params"));

        assertThrows(MPDConnectionException.class, () -> socket.sendCommands(commands));
    }

    @Test
    void testSendCommandError() throws IOException {
        String testResponse = "ACK: error";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        assertThrows(MPDConnectionException.class,
                () -> socket.sendCommand(new MPDCommand("command", "params")));
    }

    @Test
    void testSendCommandAfterClose() throws IOException {
        String testResponse = "test response";
        createValidSocket();

        socket.close();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        assertThrows(MPDConnectionException.class,
                () -> socket.sendCommand(new MPDCommand("command", "params")));
    }

    @Test
    void testSendCommandClosedAfterConnected() throws IOException {
        String testResponse = "testResponse";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        when(mockSocket.isClosed()).thenReturn(true);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(testResponse, response.get(0));
    }

    @Test
    void testSendCommandNeverConnected() throws IOException {
        String testResponse = "test response";
        createValidSocket(false);
        when(mockSocket.isConnected()).thenReturn(false).thenReturn(true);

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        socket.sendCommand(new MPDCommand("command", "params"));
    }

    @Test
    void testSendCommandEmptyError() throws IOException {
        String testResponse = "ACK";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        assertThrows(MPDConnectionException.class,
                () -> socket.sendCommand(new MPDCommand("command", "params")));
    }

    @Test
    void testSendCommandGeneralException() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenThrow(new RuntimeException());

        assertThrows(MPDConnectionException.class,
                () -> socket.sendCommand(new MPDCommand("command", "params")));
    }

    @Test
    void testSendCommandException() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new RuntimeException());

        assertThrows(Exception.class,
                () -> socket.sendCommand(new MPDCommand("command", "param")));
    }

    @Test
    void testSendCommandExceptionWithConnectException() throws IOException {
        createValidSocket();

        mockedInputStream = new ByteArrayInputStream(VERSION_RESPONSE.getBytes());
        when(mockSocket.getInputStream())
                .thenThrow(new SocketException())
                .thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new SocketException())
                .thenReturn("OK");

        socket.sendCommand(new MPDCommand("command"));
    }

    @Test
    void testSendCommandExceptionWithMaxConnectExceptions() throws IOException {
        createValidSocket();
        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenReturn("OK");

        assertThrows(MPDConnectionException.class,
                () -> socket.sendCommand(new MPDCommand("command")));
    }

    @Test
    void testSendCommands() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn(null)
                .thenReturn("OK")
                .thenReturn(null);

        List<MPDCommand> commands = new ArrayList<>();

        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2", "param2");
        MPDCommand command3 = new MPDCommand("command3");

        commands.add(command1);
        commands.add(command2);
        commands.add(command3);

        mockedOutputStream = mock(OutputStream.class);
        when(mockSocket.getOutputStream()).thenReturn(mockedOutputStream);
        socket.sendCommands(commands);

        ServerProperties serverProperties = new ServerProperties();
        StringBuilder sb = new StringBuilder();
        sb.append(convertCommand(new MPDCommand(serverProperties.getStartBulk())));
        commands.forEach(command -> sb.append(convertCommand(command)));
        sb.append(convertCommand(new MPDCommand(serverProperties.getEndBulk())));

        verify(mockedOutputStream, times(2)).write(byteArgumentCaptor.capture());

        assertArrayEquals(sb.toString().getBytes(), byteArgumentCaptor.getAllValues().get(1));
    }

    @Test
    void testSendCommandsExtraResponses() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn("OK")
                .thenReturn("unexpected");

        when(mockedBufferedReader.ready())
                .thenReturn(true)
                .thenReturn(false);

        List<MPDCommand> commands = new ArrayList<>();

        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2", "param2");
        MPDCommand command3 = new MPDCommand("command3");

        commands.add(command1);
        commands.add(command2);
        commands.add(command3);

        mockedOutputStream = mock(OutputStream.class);
        when(mockSocket.getOutputStream()).thenReturn(mockedOutputStream);
        socket.sendCommands(commands);

        ServerProperties serverProperties = new ServerProperties();
        StringBuilder sb = new StringBuilder();
        sb.append(convertCommand(new MPDCommand(serverProperties.getStartBulk())));
        commands.forEach(command -> sb.append(convertCommand(command)));
        sb.append(convertCommand(new MPDCommand(serverProperties.getEndBulk())));

        verify(mockedOutputStream, times(2)).write(byteArgumentCaptor.capture());

        assertTrue(Arrays.equals(sb.toString().getBytes(), byteArgumentCaptor.getAllValues().get(1)));
    }

    @Test
    void testSendCommandsWithError() throws IOException {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn(null)
                .thenReturn("OK")
                .thenReturn(null)
                .thenReturn("Error")
                .thenReturn(null);

        List<MPDCommand> commands = new ArrayList<>();

        MPDCommand command1 = new MPDCommand("command1");
        MPDCommand command2 = new MPDCommand("command2", "param2");
        MPDCommand command3 = new MPDCommand("command3");

        commands.add(command1);
        commands.add(command2);
        commands.add(command3);

        mockedOutputStream = mock(OutputStream.class);
        when(mockSocket.getOutputStream()).thenReturn(mockedOutputStream);
        socket.sendCommands(commands);

        ServerProperties serverProperties = new ServerProperties();
        StringBuilder sb = new StringBuilder();
        sb.append(convertCommand(new MPDCommand(serverProperties.getStartBulk())));
        commands.forEach(command -> sb.append(convertCommand(command)));
        sb.append(convertCommand(new MPDCommand(serverProperties.getEndBulk())));

        verify(mockedOutputStream, times(2)).write(byteArgumentCaptor.capture());

        assertTrue(Arrays.equals(sb.toString().getBytes(), byteArgumentCaptor.getAllValues().get(1)));
    }

    @Test
    void testCreateSocket() throws IOException {
        createValidSocket();
        assertNotNull(((TestSocket) socket).createParentSocket());
    }

    @Test
    void testGetVersion() throws IOException {
        createValidSocket();
        assertEquals("MPD 0.18.0", socket.getVersion());
    }

    @Test
    void testCloseException() throws IOException {
        createValidSocket();

        doThrow(new IOException()).when(mockSocket).close();
        assertThrows(MPDConnectionException.class, () -> socket.close());
    }

    @Test
    void testCloseReaderException() throws IOException {
        createValidSocket();

        doThrow(new IOException()).when(mockedBufferedReader).close();
        assertThrows(MPDConnectionException.class, () -> socket.close());
    }

    @Test
    void testClose() throws IOException {
        createValidSocket();
        socket.close();
    }

    private String convertCommand(MPDCommand command) {
        StringBuilder sb = new StringBuilder(command.getCommand());

        for (String param : command.getParams()) {
            param = param.replaceAll("\"", "\\\\\"");
            sb.append(" \"").append(param).append("\"");
        }

        return sb.append("\n").toString();
    }

    private void createValidSocket() throws IOException {
        createValidSocket(true);
    }

    private void createValidSocket(boolean connected) throws IOException {
        mockSocket = mock(Socket.class);
        mockedInputStream = new ByteArrayInputStream(VERSION_RESPONSE.getBytes());
        mockedOutputStream = new ByteArrayOutputStream();
        mockedBufferedReader = mock(BufferedReader.class);
        byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);

        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockedOutputStream);

        InetAddress inetAddress = InetAddress.getByName("localhost");

        try {
            when(mockedBufferedReader.readLine()).thenReturn(VERSION_RESPONSE);
        } catch (IOException e) {
            e.printStackTrace();
        }

        socket = new TestSocket(inetAddress, 9999, 10);
        socket.setReader(mockedBufferedReader);
        if (connected) {
            when(mockSocket.isConnected()).thenReturn(true);
        }
    }

    private class TestSocket extends MPDSocket {
        TestSocket(InetAddress server, int port, int timeout) {
            super(server, port, timeout);
        }

        @Override
        protected Socket createSocket() {
            return mockSocket;
        }

        public void setReader(BufferedReader reader) {
            super.setReader(mockedBufferedReader);
        }

        Socket createParentSocket() {
            return super.createSocket();
        }
    }
}
