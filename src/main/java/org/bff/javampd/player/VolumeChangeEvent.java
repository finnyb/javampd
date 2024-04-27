package org.bff.javampd.player;

import lombok.Getter;

/**
 * Represents a change in the volume of a MPD player.
 *
 * @author Bill
 * @version 1.0
 */
@Getter
public class VolumeChangeEvent extends java.util.EventObject {
  /**
   * -- GETTER -- Returns the new volume level.
   *
   * @return the new volume
   */
  private final int volume;

  /**
   * Creates a new instance of MusicPlayerStatusChangedEvent
   *
   * @param source the object on which the Event initially occurred
   * @param volume the new volume
   */
  public VolumeChangeEvent(Object source, int volume) {
    super(source);
    this.volume = volume;
  }
}
