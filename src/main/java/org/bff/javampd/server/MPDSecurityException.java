package org.bff.javampd.server;

import org.bff.javampd.MPDException;

/**
 * Represents a security or permission issue when trying to execute commands
 *
 * @author bill
 */
public class MPDSecurityException extends MPDException {

  /**
   * Class constructor specifying the message.
   *
   * @param message the exception message
   */
  public MPDSecurityException(String message) {
    super(message);
  }

  /**
   * Class constructor specifying the message and command generating the
   * error.
   *
   * @param message the exception message
   * @param command the command generating the exception
   */
  public MPDSecurityException(String message, String command) {
    super(message, command);
  }
}
