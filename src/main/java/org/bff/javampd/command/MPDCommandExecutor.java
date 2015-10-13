package org.bff.javampd.command;

import com.google.inject.Singleton;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDSocket;

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

    private MPDSocket mpdSocket;
    private MPD mpd;
    private String password;

    /**
     * You <b>MUST</b> call {@link #setMpd} before
     * making any calls to the server
     */
    public MPDCommandExecutor() {
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
        checkSocket();
        return mpdSocket.sendCommand(command);
    }

    @Override
    public synchronized boolean sendCommands(List<MPDCommand> commandList) {
        checkSocket();
        return mpdSocket.sendCommands(commandList);
    }

    private void checkSocket() {
        if (mpd == null) {
            throw new MPDConnectionException("Socket could not be established.  Was mpd set?");
        }

        if (mpdSocket == null) {
            mpdSocket = new MPDSocket(mpd.getAddress(),
                    mpd.getPort(),
                    mpd.getTimeout(),
                    password);
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

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void authenticate() {
        mpdSocket.authenticate();
    }
}
