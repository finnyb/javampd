package org.bff.javampd.server;

import lombok.Getter;

/**
 * An event used to identify a MPD error.
 *
 * @author Bill
 * @version 1.0
 */
@Getter
public class ErrorEvent extends java.util.EventObject {
  /**
   * -- GETTER -- Returns the message attached to this event. If there is no message null is
   * returned.
   *
   * @return the optional message
   */
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
}
