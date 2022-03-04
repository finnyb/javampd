package org.bff.javampd.server;

/**
 * An event used to identify a MPD error.
 *
 * @author Bill
 * @version 1.0
 */
public class ErrorEvent extends java.util.EventObject {
  private String message;

  /**
   * Creates a new instance of ErrorEvent
   *
   * @param source the object on which the Event initially occurred
   */
  public ErrorEvent(Object source) {
    super(source);
  }

  /**
   * Creates a new instance of ErrorEvent
   *
   * @param message an optional message
   * @param source the object on which the Event initially occurred
   */
  public ErrorEvent(Object source, String message) {
    super(source);
    this.message = message;
  }

  /**
   * Returns the message attached to this event. If there is no message null is returned.
   *
   * @return the optional message
   */
  public String getMessage() {
    return message;
  }
}
