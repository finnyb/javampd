package org.bff.javampd.playlist;

import java.util.EventObject;

/**
 * Represents a change in the status of a MPD music playlist.
 *
 * @author Bill
 */
public class PlaylistBasicChangeEvent extends EventObject {
  private Event event;

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

  /**
   * Returns the {@link PlaylistBasicChangeEvent.Event} that occurred.
   *
   * @return the specific {@link PlaylistBasicChangeEvent.Event}
   */
  public Event getEvent() {
    return event;
  }
}
