package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author bill
 * @since: 11/21/13 7:31 PM
 */
public abstract class CommandExecutor {

    private MPDSocket mpdSocket;

    /**
     * If this constructor is used you <b>MUST</b> call {@link #setMpd} before
     * making any calls to the server
     */
    public CommandExecutor() {
    }

    public CommandExecutor(MPD mpd) {
        this();
        mpdSocket = MPDSocket.getInstance(mpd);
    }

    /**
     * Sends a {@link MPDCommand} and its parameters to the MPD server returning the
     * response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    protected synchronized Collection<String> sendMPDCommand(MPDCommand command) throws MPDConnectionException, MPDResponseException {
        return sendCommand(command);
    }

    protected synchronized List<String> sendMPDCommand(String command) throws MPDResponseException, MPDConnectionException {
        return new ArrayList<String>(sendMPDCommand(new MPDCommand(command)));
    }

    protected synchronized List<String> sendMPDCommand(String command, String... params) throws MPDResponseException, MPDConnectionException {
        return new ArrayList<String>(sendMPDCommand(new MPDCommand(command, params)));
    }

    protected synchronized List<String> sendMPDCommand(String command, Integer... params) throws MPDResponseException, MPDConnectionException {
        String[] intParms = new String[params.length];
        for (int i = 0; i < params.length; ++i) {
            intParms[i] = Integer.toString(params[i]);
        }
        return new ArrayList<String>(sendMPDCommand(new MPDCommand(command, intParms)));
    }

    private synchronized Collection<String> sendCommand(MPDCommand command) throws MPDConnectionException, MPDResponseException {
        return mpdSocket.sendCommand(command);
    }

    /**
     * Sends a list of {@link MPDCommand}s all at once to the MPD server and returns
     * true if all commands were sent successfully.  If any of the commands received
     * as error in the response false will be returned.
     *
     * @param commandList the list of {@link MPDCommand}s
     * @return true if successful, false otherwise
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    protected synchronized boolean sendMPDCommands(List<MPDCommand> commandList) throws MPDConnectionException, MPDResponseException {
        return mpdSocket.sendMPDCommands(commandList);
    }

    public String getMPDVersion() {
        return mpdSocket.getVersion();
    }

    public void setMpd(MPD mpd) throws IOException, MPDConnectionException {
        mpdSocket = MPDSocket.getInstance(mpd);
    }
}
