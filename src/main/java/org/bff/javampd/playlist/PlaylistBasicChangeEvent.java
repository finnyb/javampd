package org.bff.javampd.playlist;

import java.util.EventObject;
import lombok.Getter;

/**
 * Represents a change in the status of a MPD music playlist.
 *
 * @author Bill
 */
@Getter
public class PlaylistBasicChangeEvent extends EventObject {
  /**
   * -- GETTER -- Returns the that occurred.
   *
   * @return the specific {@link Event}
   */
  private final Event event;

  public enum Event {
    SONG_ADDED,
    SONG_DELETED,
    SONG_CHANGED,
    PLAYLIST_CHANGED,
    PLAYLIST_ENDED
  }

  /**
   * Creates a new instance of PlayListBasicChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the specific {@link PlaylistBasicChangeEvent.Event} that occurred
   */
  public PlaylistBasicChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }
}
