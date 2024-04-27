package org.bff.javampd.admin;

import lombok.Getter;

/**
 * An event used to identify an administrative action.
 *
 * @author Bill
 * @version 1.0
 */
@Getter
public class MPDChangeEvent extends java.util.EventObject {
  /**
   * -- GETTER -- Returns the specific that occurred.
   *
   * @return the specific {@link Event}
   */
  private final Event event;

  public enum Event {
    KILLED,
    REFRESHED
  }

  /**
   * Creates a new instance of MusicPlayerStatusChangedEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the specific {@link Event} that occurred
   */
  public MPDChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }
}
