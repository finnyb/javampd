package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author bill
 */
public interface CommandExecutor {
    /**
     * Sends a command with no parameters to the {@link MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    List<String> sendCommand(String command) throws MPDResponseException, MPDConnectionException;

    /**
     * Sends a command and parameters to the {@link MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    List<String> sendCommand(String command, String... params) throws MPDResponseException, MPDConnectionException;

    /**
     * Sends a command and parameters to the {@link MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    List<String> sendCommand(String command, Integer... params) throws MPDResponseException, MPDConnectionException;

    /**
     * Sends a {@link org.bff.javampd.MPDCommand} to the {@link MPD} server returning the
     * response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    Collection<String> sendCommand(MPDCommand command) throws MPDConnectionException, MPDResponseException;

    /**
     * Sends a list of {@link org.bff.javampd.MPDCommand}s all at once to the MPD server and returns
     * true if all commands were sent successfully.  If any of the commands received
     * as error in the response false will be returned.
     *
     * @param commandList the list of {@link org.bff.javampd.MPDCommand}s
     * @return true if successful, false otherwise
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    boolean sendCommands(List<MPDCommand> commandList) throws MPDConnectionException, MPDResponseException;

    /**
     * Returns the {@link MPD} version
     *
     * @return the version
     */
    String getMPDVersion() throws MPDConnectionException;

    /**
     * Sets the {@link MPD} to run commands against
     *
     * @param mpd the {@link MPD}
     * @throws java.io.IOException    if there is a problem connecting to the server
     * @throws MPDConnectionException if there is a problem connecting to the server
     */
    void setMpd(MPD mpd) throws IOException, MPDConnectionException;
}
