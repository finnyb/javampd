package org.bff.javampd.command;

import com.google.inject.Singleton;
import org.bff.javampd.server.MPD;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.MPDResponseException;
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

    /**
     * You <b>MUST</b> call {@link #setMpd} before
     * making any calls to the server
     */
    public MPDCommandExecutor() {
    }

    @Override
    public synchronized List<String> sendCommand(String command) throws MPDResponseException {
        return new ArrayList<>(sendCommand(new MPDCommand(command)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, String... params) throws MPDResponseException {
        return new ArrayList<>(sendCommand(new MPDCommand(command, params)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, Integer... params) throws MPDResponseException {
        String[] intParms = new String[params.length];
        for (int i = 0; i < params.length; ++i) {
            intParms[i] = Integer.toString(params[i]);
        }
        return new ArrayList<>(sendCommand(new MPDCommand(command, intParms)));
    }

    @Override
    public synchronized Collection<String> sendCommand(MPDCommand command) throws MPDResponseException {
        try {
            checkSocket();
        } catch (MPDConnectionException e) {
            throw new MPDResponseException(e);
        }
        return mpdSocket.sendCommand(command);
    }

    @Override
    public synchronized boolean sendCommands(List<MPDCommand> commandList) throws MPDResponseException {
        try {
            checkSocket();
        } catch (MPDConnectionException e) {
            throw new MPDResponseException(e);
        }
        return mpdSocket.sendCommands(commandList);
    }

    private void checkSocket() throws MPDConnectionException {
        if (mpd == null) {
            throw new MPDConnectionException("Socket could not be established.  Was mpd set?");
        }

        if (mpdSocket == null) {
            mpdSocket = new MPDSocket(mpd.getAddress(), mpd.getPort(), mpd.getTimeout());
        }
    }

    @Override
    public String getMPDVersion() throws MPDResponseException {
        try {
            checkSocket();
        } catch (MPDConnectionException e) {
            throw new MPDResponseException(e);
        }
        return mpdSocket.getVersion();
    }

    @Override
    public void setMpd(MPD mpd) throws MPDConnectionException {
        this.mpd = mpd;
    }
}
