package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

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
     * @throws MPDResponseException if the MPD response generates an error
     */
    List<String> sendCommand(String command) throws MPDResponseException;

    /**
     * Sends a command and parameters to the {@link MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException if the MPD response generates an error
     */
    List<String> sendCommand(String command, String... params) throws MPDResponseException;

    /**
     * Sends a command and parameters to the {@link MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException if the MPD response generates an error
     */
    List<String> sendCommand(String command, Integer... params) throws MPDResponseException;

    /**
     * Sends a {@link org.bff.javampd.MPDCommand} to the {@link MPD} server returning the
     * response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     * @throws MPDResponseException if the MPD response generates an error
     */
    Collection<String> sendCommand(MPDCommand command) throws MPDResponseException;

    /**
     * Sends a list of {@link org.bff.javampd.MPDCommand}s all at once to the MPD server and returns
     * true if all commands were sent successfully.  If any of the commands received
     * as error in the response false will be returned.
     *
     * @param commandList the list of {@link org.bff.javampd.MPDCommand}s
     * @return true if successful, false otherwise
     * @throws MPDResponseException if the MPD response generates an error
     */
    boolean sendCommands(List<MPDCommand> commandList) throws MPDResponseException;

    /**
     * Returns the {@link MPD} version
     *
     * @return the version
     */
    String getMPDVersion() throws MPDResponseException;

    /**
     * Sets the {@link MPD} to run commands against
     *
     * @param mpd the {@link MPD}
     * @throws MPDResponseException if there is a problem connecting to the server
     */
    void setMpd(MPD mpd) throws MPDConnectionException;
}
