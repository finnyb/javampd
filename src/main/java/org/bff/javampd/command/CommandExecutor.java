package org.bff.javampd.command;

import java.util.Collection;
import java.util.List;
import org.bff.javampd.server.MPD;

/** @author bill */
public interface CommandExecutor {
  static String COMMAND_TERMINATION = "OK";
  /**
   * Sends a command with no parameters to the {@link org.bff.javampd.server.MPD} server returning
   * the response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
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
   * @param params the parameters for the command
   * @return the response as a <CODE>Collection</CODE> of <CODE>Strings</CODE>
   */
  List<String> sendCommand(String command, String... params);

  /**
   * Sends a command and parameters to the {@link org.bff.javampd.server.MPD} server returning the
   * response as a <CODE>List</CODE> of <CODE>Strings</CODE>.
   *
   * @param command the command to send
   * @param params the parameters for the command
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
   * Sends a list of {@link MPDCommand}s all at once to the MPD server and returns true if all
   * commands were sent successfully. If any of the commands received as error in the response false
   * will be returned.
   *
   * @param commandList the list of {@link MPDCommand}s
   */
  void sendCommands(List<MPDCommand> commandList);

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

  /** Authenticates to the server using the password provided by #usePassword */
  void authenticate();

  /**
   * Password for password protected mpd
   *
   * @param password the mpd password
   */
  void usePassword(String password);

  /** Close the connection executor socket */
  void close();
}
