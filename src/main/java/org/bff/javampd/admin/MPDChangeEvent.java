package org.bff.javampd.admin;

/**
 * An event used to identify an administrative action.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDChangeEvent extends java.util.EventObject {
  private Event event;

  public enum Event {
    KILLED,
    REFRESHED,
  }

  /**
   * Creates a new instance of MusicPlayerStatusChangedEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event  the specific {@link Event} that occurred
   */
  public MPDChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }

  /**
   * Returns the specific {@link Event} that occurred.
   *
   * @return the specific {@link Event}
   */
  public Event getEvent() {
    return this.event;
  }
}
