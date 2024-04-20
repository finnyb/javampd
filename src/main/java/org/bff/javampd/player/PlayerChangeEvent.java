package org.bff.javampd.player;

import lombok.Getter;

/**
 * Represents a change in the status of a MPD music player.
 *
 * @author Bill
 */
@Getter
public class PlayerChangeEvent extends java.util.EventObject {
  /**
   * -- GETTER -- Returns the that occurred.
   *
   * @return the specific {@link Event}
   */
  private final Event event;

  public enum Event {
    PLAYER_STOPPED,
    PLAYER_STARTED,
    PLAYER_PAUSED,
    PLAYER_NEXT,
    PLAYER_PREVIOUS,
    PLAYER_MUTED,
    PLAYER_UNMUTED,
    PLAYER_SONG_SET,
    PLAYER_SEEKING
  }

  /**
   * Creates a new instance of PlayerChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the {@link Event}
   */
  public PlayerChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }
}
