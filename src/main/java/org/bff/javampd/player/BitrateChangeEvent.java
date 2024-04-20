package org.bff.javampd.player;

import lombok.Getter;

/**
 * Represents a bitrate change when playing a song.
 *
 * @author Bill
 */
@Getter
public class BitrateChangeEvent extends java.util.EventObject {
  /**
   * -- GETTER -- the old bitrate
   *
   * @return the from bitrate
   */
  private final int oldBitrate;

  /**
   * -- GETTER -- the new bitrate
   *
   * @return the new bitrate
   */
  private final int newBitrate;

  /**
   * Creates a new instance of BitrateChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param oldBitrate the from bitrate
   * @param newBitrate the new bitrate
   */
  public BitrateChangeEvent(Object source, int oldBitrate, int newBitrate) {
    super(source);
    this.oldBitrate = oldBitrate;
    this.newBitrate = newBitrate;
  }
}
