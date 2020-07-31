package org.bff.javampd.server;

import org.bff.javampd.MPDException;

/**
 * Represents an error with the MPD connection.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDConnectionException extends MPDException {

  /**
   * Constructor.
   */
  public MPDConnectionException() {
    super();
  }

  /**
   * Class constructor specifying the message.
   *
   * @param message the exception message
   */
  public MPDConnectionException(String message) {
    super(message);
  }

  /**
   * Class constructor specifying the cause.
   *
   * @param cause the cause of this exception
   */
  public MPDConnectionException(Throwable cause) {
    super(cause);
  }

  /**
   * Class constructor specifying the message and cause.
   *
   * @param message the exception message
   * @param cause   the cause of this exception
   */
  public MPDConnectionException(String message, Throwable cause) {
    super(message, cause);
  }
}
