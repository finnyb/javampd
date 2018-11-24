package org.bff.javampd.server;

import org.bff.javampd.MPDException;
import org.bff.javampd.command.MPDCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bill
 */
public class MPDSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDSocket.class);

    private Socket socket;
    private BufferedReader reader;

    private final ResponseProperties responseProperties;
    private final ServerProperties serverProperties;
    private final String encoding;
    private String lastError;
    private String version;

    private final String server;
    private final int port;
    private boolean closed;

    private static final int TRIES = 3;

    public MPDSocket(InetAddress server,
                     int port,
                     int timeout) {
        this.server = server.getHostAddress();
        this.port = port;
        this.responseProperties = new ResponseProperties();
        this.serverProperties = new ServerProperties();
        this.encoding = serverProperties.getEncoding();
        connect(timeout);
    }

    /**
     * If MPD is already connected no attempt will be made to connect and the
     * mpdVersion is returned.
     * <p>
     * A timeout of 0 means an infinite wait.
     *
     * @param timeout socket timeout, 0 for infinite wait
     * @throws MPDConnectionException if there is a socked io problem
     */
    private synchronized void connect(int timeout) {
        connectSocket(timeout);
    }

    private void readVersion() {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            throw new MPDConnectionException(e);
        }

        if (line != null && isResponseOK(line)) {
            this.version = stripResponse(responseProperties.getOk(), line).trim();
        } else {
            throw new MPDConnectionException("Command from server: " +
                    ((line == null) ? "null" : stripResponse(responseProperties.getError(), line)));
        }
    }

    private void connectSocket(int timeout) {
        LOGGER.debug("attempting to connect socket to {} with timeout of {}", server, timeout);
        this.socket = createSocket();
        SocketAddress socketAddress = new InetSocketAddress(server, port);
        try {
            this.socket.connect(socketAddress, timeout);
            setReader(new BufferedReader(new InputStreamReader(socket.getInputStream(), encoding)));
            readVersion();
        } catch (Exception ioe) {
            LOGGER.error("failed to connect socket to {}", server);
            throw new MPDConnectionException(ioe);
        }
    }

    protected void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    protected Socket createSocket() {
        return new Socket();
    }

    public synchronized Collection<String> sendCommand(MPDCommand command) {
        checkConnection();

        int count = 0;
        while (count < TRIES) {
            try {
                return sendBytes(convertCommand(command.getCommand(), command.getParams()));
            } catch (MPDException mpdException) {
                logCommandError(command, mpdException);
                throw mpdException;
            } catch (Exception ex) {
                logCommandError(command, ex);
                try {
                    connect();
                } catch (Exception exc) {
                    LOGGER.error("Unable to connect to {} on port {}", server, port, exc);
                }
                ++count;
                LOGGER.warn("Retrying command {} for the {} time", command.getCommand(), count);
            }
        }

        LOGGER.error("Unable to send command {} after {} tries", command, TRIES);
        throw new MPDConnectionException("Unable to send command " + command);
    }

    private static void logCommandError(MPDCommand command, Exception se) {
        LOGGER.error("Error from: {}", command.getCommand(), se);
        for (String str : command.getParams()) {
            LOGGER.error("\tparam: {}", str);
        }
    }

    /**
     * Attempts to connect to MPD with an infinite timeout value.
     * If MPD is already connected no attempt will be made to connect and the
     * mpdVersion is returned.
     */
    private synchronized void connect() {
        connect(0);
    }

    private boolean isResponseOK(final String line) {
        try {
            if (line.startsWith(responseProperties.getOk())
                    || line.startsWith(responseProperties.getListOk())) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Could not determine if response is ok", e);
        }

        return false;
    }

    private boolean isResponseError(final String line) {
        if (line.startsWith(responseProperties.getError())) {
            this.lastError = line.substring(responseProperties.getError().length()).trim();
            return true;
        } else {
            return false;
        }
    }

    private static String stripResponse(String response, String line) {
        return line.substring(response.length());
    }

    private static String convertCommand(String command) {
        return convertCommand(command, new ArrayList<String>());
    }

    private static String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        for (String param : params) {
            param = param.replaceAll("\"", "\\\\\"");
            sb.append(" \"").append(param).append("\"");
        }

        return sb.append("\n").toString();
    }

    public synchronized void sendCommands(List<MPDCommand> commandList) {
        StringBuilder sb = new StringBuilder(convertCommand(serverProperties.getStartBulk()));

        for (MPDCommand command : commandList) {
            sb.append(convertCommand(command.getCommand(), command.getParams()));
        }

        sb.append(convertCommand(serverProperties.getEndBulk()));

        checkConnection();

        try {
            sendBytes(sb.toString());

            String line = reader.readLine();
            while (line != null) {
                if (!isResponseOK(line)) {
                    LOGGER.warn("some command from a command list failed: {}", line);
                }

                if (reader.ready()) {
                    line = reader.readLine();
                    LOGGER.warn("unexpected response line {}", line);
                } else {
                    line = null;
                }
            }
        } catch (MPDSecurityException se) {
            LOGGER.error("Response Error from command list", se);
            throw se;
        } catch (Exception e) {
            LOGGER.error("Response Error from command list", e);
            for (MPDCommand s : commandList) {
                LOGGER.error(s.getCommand());
            }
            throw new MPDConnectionException(e.getMessage(), e);
        }
    }

    private List<String> sendBytes(String command) throws IOException {
        LOGGER.debug("start command: {}", command);

        List<String> response = new ArrayList<>();

        writeToStream(command);

        String inLine = reader.readLine();
        LOGGER.debug("first response line is: {}", inLine);
        while (inLine != null) {
            if (isResponseOK(inLine)) {
                LOGGER.debug("the response was ok");
                break;
            }

            if (isResponseError(inLine)) {
                if (lastError.contains("you don't have permission")) {
                    throw new MPDSecurityException(lastError, command);
                } else {
                    LOGGER.error("Got error from command {}", command);
                    throw new MPDConnectionException(lastError);
                }
            }
            response.add(inLine);
            inLine = reader.readLine();
        }

//        response.forEach(LOGGER::debug);

        return response;
    }

    private void checkConnection() {
        boolean connected;

        if (this.closed) {
            throw new MPDConnectionException("Close has been called on MPD.  Create a new MPD.");
        }

        if (!socket.isConnected()) {
            LOGGER.warn("socket hasn't been connected yet");
            connected = false;
        } else {
            if (!socket.isClosed()) {
                connected = checkPing();
            } else {
                LOGGER.warn("socket is closed");
                connected = false;
            }
        }

        if (!connected) {
            LOGGER.warn("we've lost connectivity, attempting to connect");
            try {
                connect();
            } catch (Exception e) {
                throw new MPDConnectionException("Connection to server lost: " + e.getMessage(), e);
            }
        }
    }

    public void close() {
        this.closed = true;
        if (!this.socket.isClosed()) {
            try {
                this.reader.close();
            } catch (IOException e) {
                throw new MPDConnectionException("Unable to close socket", e);
            }

            try {
                this.socket.close();
            } catch (IOException e) {
                throw new MPDConnectionException("Unable to close socket", e);
            }
        }
    }

    private void writeToStream(String command) throws IOException {
        socket.getOutputStream().write(command.getBytes(serverProperties.getEncoding()));
    }

    public String getVersion() {
        return this.version;
    }

    private boolean checkPing() {
        boolean connected = true;
        try {
            writeToStream(convertCommand(serverProperties.getPing()));
            String inLine = reader.readLine();
            if (!isResponseOK(inLine)) {
                connected = false;
            }
        } catch (Exception e) {
            connected = false;
            LOGGER.error("lost socket connection", e);
        }

        return connected;
    }
}
