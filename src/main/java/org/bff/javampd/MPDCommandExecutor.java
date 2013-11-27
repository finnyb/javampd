package org.bff.javampd;

import com.google.inject.Singleton;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Executes commands to the {@link MPD}.
 *
 * @author bill
 */
@Singleton
public class MPDCommandExecutor implements CommandExecutor {
    private final Logger logger = LoggerFactory.getLogger(MPDCommandExecutor.class);

    private MPDSocket mpdSocket;

    /**
     * You <b>MUST</b> call {@link #setMpd} before
     * making any calls to the server
     */
    public MPDCommandExecutor() {
    }

    @Override
    public synchronized List<String> sendCommand(String command) throws MPDResponseException, MPDConnectionException {
        return new ArrayList<String>(sendCommand(new MPDCommand(command)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, String... params) throws MPDResponseException, MPDConnectionException {
        return new ArrayList<String>(sendCommand(new MPDCommand(command, params)));
    }

    @Override
    public synchronized List<String> sendCommand(String command, Integer... params) throws MPDResponseException, MPDConnectionException {
        String[] intParms = new String[params.length];
        for (int i = 0; i < params.length; ++i) {
            intParms[i] = Integer.toString(params[i]);
        }
        return new ArrayList<String>(sendCommand(new MPDCommand(command, intParms)));
    }

    @Override
    public synchronized Collection<String> sendCommand(MPDCommand command) throws MPDConnectionException, MPDResponseException {
        checkSocket();
        return mpdSocket.sendCommand(command);
    }

    @Override
    public synchronized boolean sendCommands(List<MPDCommand> commandList) throws MPDConnectionException, MPDResponseException {
        checkSocket();
        return mpdSocket.sendCommands(commandList);
    }

    private void checkSocket() throws MPDConnectionException {
        if (mpdSocket == null) {
            throw new MPDConnectionException("Socket not established.  Was mpd set?");
        }
    }

    @Override
    public String getMPDVersion() throws MPDConnectionException {
        checkSocket();
        return mpdSocket.getVersion();
    }

    @Override
    public void setMpd(MPD mpd) throws IOException, MPDConnectionException {
        mpdSocket = new MPDSocket(mpd.getAddress(), mpd.getPort(), mpd.getTimeout());
    }
}
