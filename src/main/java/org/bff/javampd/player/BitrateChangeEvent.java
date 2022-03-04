package org.bff.javampd.player;

/**
 * Represents a bitrate change when playing a song.
 *
 * @author Bill
 */
public class BitrateChangeEvent extends java.util.EventObject {
  private final int oldBitrate;
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

  /**
   * the old bitrate
   *
   * @return the from bitrate
   */
  public int getOldBitrate() {
    return oldBitrate;
  }

  /**
   * the new bitrate
   *
   * @return the new bitrate
   */
  public int getNewBitrate() {
    return newBitrate;
  }
}
