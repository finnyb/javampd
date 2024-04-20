package org.bff.javampd.output;

import lombok.Getter;

/**
 * Represents a change in the outputs of a {@link MPDOutput}.
 *
 * @author Bill
 * @version 1.0
 */
@Getter
public class OutputChangeEvent extends java.util.EventObject {

  /**
   * -- GETTER -- Returns the for this event.
   *
   * @return the event
   */
  private final OUTPUT_EVENT event;

  public enum OUTPUT_EVENT {
    OUTPUT_ADDED,
    OUTPUT_DELETED,
    OUTPUT_CHANGED
  }

  /**
   * Creates a new instance of OutputChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the output event
   */
  public OutputChangeEvent(Object source, OUTPUT_EVENT event) {
    super(source);
    this.event = event;
  }
}
