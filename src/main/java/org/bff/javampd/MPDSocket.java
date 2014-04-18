package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.exception.MPDTimeoutException;
import org.bff.javampd.properties.ResponseProperties;
import org.bff.javampd.properties.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bill
 * @since: 11/22/13 1:37 PM
 */
public class MPDSocket {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDSocket.class);

    private Socket socket;
    private ResponseProperties responseProperties;
    private ServerProperties commandProperties;
    private String encoding;
    private String lastError;
    private String version;

    private String server;
    private int port;

    private static final int TRIES = 3;

    MPDSocket(InetAddress server, int port, int timeout) throws MPDConnectionException {
        this.server = server.getHostAddress();
        this.port = port;
        this.responseProperties = new ResponseProperties();
        this.commandProperties = new ServerProperties();
        this.encoding = commandProperties.getEncoding();
        this.version = connect(timeout);
    }

    /**
     * If MPD is already connected no attempt will be made to connect and the
     * mpdVersion is returned.
     * <p/>
     * A timeout of 0 means an infinite wait.
     *
     * @param timeout socket timeout, 0 for infinite wait
     * @return the version of MPD
     * @throws java.io.IOException    if there is a socked io problem
     * @throws MPDConnectionException if there are connection issues
     */
    private synchronized String connect(int timeout) throws MPDConnectionException {
        connectSocket(timeout);
        return readVersion();
    }

    private String readVersion() throws MPDConnectionException {
        String line;
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), encoding));
            line = in.readLine();
        } catch (IOException e) {
            throw new MPDConnectionException(e);
        }

        if (isResponseOK(line)) {
            return stripResponse(responseProperties.getOk(), line).trim();
        } else {
            throw new MPDConnectionException("Command from server: " +
                    ((line == null) ? "null" : stripResponse(responseProperties.getError(), line)));
        }
    }

    private void connectSocket(int timeout) throws MPDTimeoutException {
        this.socket = new Socket();

        SocketAddress socketAddress = new InetSocketAddress(server, port);
        try {
            this.socket.connect(socketAddress, timeout);
        } catch (IOException ioe) {
            throw new MPDTimeoutException(ioe);
        }
    }

    public synchronized Collection<String> sendCommand(MPDCommand command) throws MPDResponseException {
        checkConnection();

        int count = 0;
        while (count < TRIES) {
            try {
                return sendBytes(convertCommand(command.getCommand(), command.getParams()));
            } catch (SocketException se) {
                try {
                    connect();
                } catch (Exception ex) {
                    LOGGER.error("Unable to connect to {} on port {}", server, port, ex);
                }
                ++count;
                LOGGER.error("Retrying command {} for the {} time", command.getCommand(), count, se);
            } catch (Exception e) {
                LOGGER.error("Got Error from: {}", command.getCommand(), e);
                for (String str : command.getParams()) {
                    LOGGER.error("\tparam: {}", str);
                }

                throw new MPDResponseException(e);
            }
        }
        return new ArrayList<>();
    }

    /**
     * Attempts to connect to MPD with an infinite timeout value.
     * If MPD is already connected no attempt will be made to connect and the
     * mpdVersion is returned.
     *
     * @return return the version of MPD
     * @throws IOException            if there is a socked io problem
     * @throws MPDConnectionException if there are connection issues
     */
    private synchronized String connect() throws MPDConnectionException {
        return connect(0);
    }

    private boolean isResponseOK(final String line) {
        try {
            if (line.startsWith(responseProperties.getOk())) {
                return true;
            }

            if (line.startsWith(responseProperties.getListOk())) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Could not determine if response is ok", e);
        }
        return false;
    }

    private boolean isResponseError(final String line) {
        try {
            if (line.startsWith(responseProperties.getError())) {
                this.lastError = line.substring(responseProperties.getError().length()).trim();
                return true;
            }
        } catch (Exception e) {
            LOGGER.error("Could not determine if response is error", e);
        }
        return false;
    }

    private String stripResponse(String response, String line) {
        return line.substring(response.length());
    }

    private String convertCommand(String command) {
        return convertCommand(command, new ArrayList<String>());
    }

    private String convertCommand(String command, List<String> params) {
        StringBuilder sb = new StringBuilder(command);
        for (String param : params) {
            param = param.replaceAll("\"", "\\\\\"");
            sb.append(" \"").append(param).append("\"");
        }

        return sb.append("\n").toString();
    }

    public synchronized boolean sendCommands(List<MPDCommand> commandList) throws MPDResponseException {
        StringBuilder sb = new StringBuilder(convertCommand(commandProperties.getStartBulk()));

        for (MPDCommand command : commandList) {
            sb.append(convertCommand(command.getCommand(), command.getParams()));
        }
        sb.append(convertCommand(commandProperties.getEndBulk()));

        checkConnection();

        try {
            sendBytes(sb.toString());
        } catch (Exception e) {
            throw new MPDResponseException(e.getMessage(), e);
        }

        return true;
    }

    private List<String> sendBytes(String command) throws IOException, MPDResponseException {
        LOGGER.debug("start command: " + command.trim());
        byte[] bytesToSend = command.getBytes(commandProperties.getEncoding());

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), encoding));

        OutputStream outStream = socket.getOutputStream();
        outStream.write(bytesToSend);

        List<String> response = new ArrayList<>();

        String inLine = in.readLine();
        while (inLine != null) {
            if (isResponseOK(inLine)) {
                break;
            }

            if (isResponseError(inLine)) {
                throw new MPDResponseException(lastError, command);
            }
            response.add(inLine);
            inLine = in.readLine();
        }
        for (String s : response) {
            LOGGER.debug(s);
        }
        LOGGER.debug("end command: " + command.trim());

        return response;
    }

    private void checkConnection() throws MPDResponseException {
        if (!socket.isConnected()) {
            try {
                connect();
            } catch (Exception e) {
                throw new MPDResponseException("Connection to server lost: " + e.getMessage(), e);
            }
        }
    }

    public String getVersion() {
        return this.version;
    }
}
