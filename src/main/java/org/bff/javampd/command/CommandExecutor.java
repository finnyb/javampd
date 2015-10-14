package org.bff.javampd.command;

import org.bff.javampd.server.MPD;

import java.util.Collection;
import java.util.List;

/**
 * @author bill
 */
public interface CommandExecutor {
    /**
     * Sends a command with no parameters to the {@link org.bff.javampd.server.MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     */
    List<String> sendCommand(String command);

    /**
     * Sends a command and parameters to the {@link org.bff.javampd.server.MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     */
    List<String> sendCommand(String command, String... params);

    /**
     * Sends a command and parameters to the {@link org.bff.javampd.server.MPD} server returning the
     * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @param params  the parameters for the command
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     */
    List<String> sendCommand(String command, Integer... params);

    /**
     * Sends a {@link MPDCommand} to the {@link org.bff.javampd.server.MPD} server returning the
     * response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>.
     *
     * @param command the command to send
     * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
     */
    Collection<String> sendCommand(MPDCommand command);

    /**
     * Sends a list of {@link MPDCommand}s all at once to the MPD server and returns
     * true if all commands were sent successfully.  If any of the commands received
     * as error in the response false will be returned.
     *
     * @param commandList the list of {@link MPDCommand}s
     * @return true if successful, false otherwise
     */
    boolean sendCommands(List<MPDCommand> commandList);

    /**
     * Returns the {@link org.bff.javampd.server.MPD} version
     *
     * @return the version
     */
    String getMPDVersion();

    /**
     * Sets the {@link org.bff.javampd.server.MPD} to run commands against
     *
     * @param mpd the {@link org.bff.javampd.server.MPD}
     */
    void setMpd(MPD mpd);

    /**
     * Authenticates to the server using the password
     */
    void authenticate(String password);
}
