package org.bff.javampd.command;

import com.google.inject.Singleton;
import org.bff.javampd.server.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Executes commands to the {@link org.bff.javampd.server.MPD}.
 * You <b>MUST</b> call {@link #setMpd} before making any calls
 * to the server
 *
 * @author bill
 */
@Singleton
public class MPDCommandExecutor implements CommandExecutor {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDCommandExecutor.class);

    private MPDSocket mpdSocket;
    private MPD mpd;
    private ServerProperties serverProperties;
    private String password;

    /**
     * You <b>MUST</b> call {@link #setMpd} before
     * making any calls to the server
     */
    public MPDCommandExecutor() {
        serverProperties = new ServerProperties();
    }

    @Override
    public synchronized List<String> sendCommand(String command) {
        return new ArrayList<>(sendCommand(new MPDCommand(command)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, String... params) {
        return new ArrayList<>(sendCommand(new MPDCommand(command, params)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, Integer... params) {
        String[] intParms = new String[params.length];
        for (int i = 0; i < params.length; ++i) {
            intParms[i] = Integer.toString(params[i]);
        }
        return new ArrayList<>(sendCommand(new MPDCommand(command, intParms)));
    }

    @Override
    public synchronized Collection<String> sendCommand(MPDCommand command) {
        try {
            checkSocket();
            return mpdSocket.sendCommand(command);
        } catch (MPDSecurityException se) {
            LOGGER.warn("Connection exception while sending command {}, will retry", command.getCommand(), se);
            authenticate();
            return mpdSocket.sendCommand(command);
        }
    }

    @Override
    public synchronized void sendCommands(List<MPDCommand> commandList) {
        try {
            checkSocket();
            mpdSocket.sendCommands(commandList);
        } catch (MPDSecurityException se) {
            LOGGER.warn("Connection exception while sending commands, will retry", se);
            authenticate();
            mpdSocket.sendCommands(commandList);
        }
    }

    private void checkSocket() {
        if (mpd == null) {
            throw new MPDConnectionException("Socket could not be established.  Was mpd set?");
        }

        if (mpdSocket == null) {
            mpdSocket = new MPDSocket(mpd.getAddress(),
                    mpd.getPort(),
                    mpd.getTimeout());
        }
    }

    @Override
    public String getMPDVersion() {
        checkSocket();
        return mpdSocket.getVersion();
    }

    @Override
    public void setMpd(MPD mpd) {
        this.mpd = mpd;
    }

    @Override
    public void authenticate() {
        if (password == null) {
            throw new MPDSecurityException("Password cannot be null");
        }

        try {
            sendCommand(new MPDCommand(serverProperties.getPassword(), password));
        } catch (Exception e) {
            LOGGER.error("Error authenticating to mpd", e);
            if (e.getMessage() != null && e.getMessage().contains("incorrect password")) {
                throw new MPDSecurityException("Incorrect password");
            }

            throw new MPDConnectionException("Could not authenticate", e);
        }
    }

    @Override
    public void usePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password cannot be null");
        }

        this.password = password;
    }
}
