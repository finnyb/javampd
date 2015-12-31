package org.bff.javampd.server;

import org.bff.javampd.command.MPDCommand;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MPDSocketTest {

    private MPDSocket socket;

    private Socket mockSocket;
    private InputStream mockedInputStream;
    private OutputStream mockedOutputStream;
    private BufferedReader mockedBufferedReader;

    private ArgumentCaptor<byte[]> byteArgumentCaptor;

    @Test(expected = MPDConnectionException.class)
    public void testSocketCreationWithError() throws Exception {
        mockSocket = mock(Socket.class);
        mockedInputStream = mock(InputStream.class);
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        InetAddress inetAddress = InetAddress.getByName("localhost");
        socket = new TestSocket(inetAddress, 9999, 10);
    }

    @Test(expected = MPDConnectionException.class)
    public void testSocketCreationWithConnectError() throws Exception {
        mockSocket = mock(Socket.class);
        mockedInputStream = mock(InputStream.class);

        InetAddress inetAddress = InetAddress.getByName("localhost");
        int timeout = 10;
        int port = 9999;

        doThrow(new RuntimeException())
                .when(mockSocket)
                .connect(new InetSocketAddress(inetAddress, port), timeout);

        socket = new TestSocket(inetAddress, port, timeout);
    }

    @Test(expected = MPDConnectionException.class)
    public void testSocketCreationWithBadResponse() throws Exception {
        mockSocket = mock(Socket.class);
        mockedBufferedReader = mock(BufferedReader.class);

        mockedInputStream = new ByteArrayInputStream("NOTOK MPD 0.18.0".getBytes());
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine()).thenReturn("Bad");
        InetAddress inetAddress = InetAddress.getByName("localhost");
        socket = new TestSocket(inetAddress, 9999, 10);
    }

    @Test
    public void testSendCommand() throws Exception {
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
    public void testSendCommandOKButNoResponse() throws Exception {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn("OK")
                .thenReturn(null);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(0, response.size());
    }

    @Test
    public void testSendCommandSocketException() throws Exception {
        String testResponse = "testResponse";
        createValidSocket();

        mockedInputStream = new ByteArrayInputStream("OK MPD 0.18.0".getBytes());
        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new SocketException())
                .thenReturn(testResponse)
                .thenReturn(null);

        MPDCommand command = new MPDCommand("command");

        List<String> response = new ArrayList<>(socket.sendCommand(command));
        assertEquals(testResponse, response.get(0));
    }

    @Test(expected = MPDSecurityException.class)
    public void testSendCommandNoPermissionException() throws Exception {
        String testResponse = "ACK: you don't have permission";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        socket.sendCommand(new MPDCommand("command", "params"));
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandError() throws Exception {
        String testResponse = "ACK: error";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        socket.sendCommand(new MPDCommand("command", "params"));
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandEmptyError() throws Exception {
        String testResponse = "ACK";
        createValidSocket();

        List<String> responseList = new ArrayList<>();
        responseList.add(testResponse);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenReturn(testResponse)
                .thenReturn(null);

        socket.sendCommand(new MPDCommand("command", "params"));
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandGeneralException() throws Exception {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenThrow(new RuntimeException());

        socket.sendCommand(new MPDCommand("command", "params"));
    }

    @Test(expected = Exception.class)
    public void testSendCommandException() throws Exception {
        createValidSocket();

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new RuntimeException());

        socket.sendCommand(new MPDCommand("command", "param"));
    }

    @Test
    public void testSendCommandExceptionWithConnectException() throws Exception {
        createValidSocket();

        mockedInputStream = new ByteArrayInputStream("OK MPD 0.18.0".getBytes());
        when(mockSocket.getInputStream())
                .thenThrow(new SocketException())
                .thenReturn(mockedInputStream);

        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new SocketException())
                .thenReturn("OK");

        socket.sendCommand(new MPDCommand("command"));
    }

    @Test(expected = MPDConnectionException.class)
    public void testSendCommandExceptionWithMaxConnectExceptions() throws Exception {
        createValidSocket();
        when(mockedBufferedReader.readLine())
                .thenReturn("OK")
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenThrow(new SocketException())
                .thenReturn("OK");

        socket.sendCommand(new MPDCommand("command"));
    }

    @Test
    public void testSendCommandStream() {

    }

    @Test
    public void testSendCommandAndParamStream() {

    }

    @Test
    public void testSendCommands() throws Exception {
        createValidSocket();

        when(mockedBufferedReader.readLine())
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

        assertTrue(Arrays.equals(sb.toString().getBytes(), byteArgumentCaptor.getAllValues().get(1)));
    }

    @Test
    public void testCreateSocket() throws Exception {
        createValidSocket();
        assertNotNull(((TestSocket) socket).createParentSocket());
    }

    @Test
    public void testGetVersion() throws Exception {
        createValidSocket();
        assertEquals("MPD 0.18.0", socket.getVersion());
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
        mockSocket = mock(Socket.class);
        mockedInputStream = new ByteArrayInputStream("OK MPD 0.18.0".getBytes());
        mockedOutputStream = new ByteArrayOutputStream();
        mockedBufferedReader = mock(BufferedReader.class);
        byteArgumentCaptor = ArgumentCaptor.forClass(byte[].class);

        when(mockSocket.getInputStream()).thenReturn(mockedInputStream);
        when(mockSocket.getOutputStream()).thenReturn(mockedOutputStream);

        InetAddress inetAddress = InetAddress.getByName("localhost");
        socket = new TestSocket(inetAddress, 9999, 10);
        when(mockSocket.isConnected()).thenReturn(true);
    }

    private class TestSocket extends MPDSocket {
        public TestSocket(InetAddress server, int port, int timeout) {
            super(server, port, timeout);
        }

        @Override
        protected Socket createSocket() {
            return mockSocket;
        }

        @Override
        protected BufferedReader writeToStream(String command) throws IOException {
            super.writeToStream(command);
            return mockedBufferedReader;
        }

        public Socket createParentSocket() {
            return super.createSocket();
        }
    }
}