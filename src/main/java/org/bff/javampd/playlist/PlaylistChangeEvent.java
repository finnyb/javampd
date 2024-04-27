package org.bff.javampd.playlist;

import java.util.EventObject;
import lombok.Getter;

/**
 * Represents a change in the status of a MPD playlist.
 *
 * @author Bill
 */
@Getter
public class PlaylistChangeEvent extends EventObject {
  /**
   * -- GETTER -- Returns the that occurred.
   *
   * @return the specific id
   */
  private final Event event;

  /**
   * -- GETTER -- the name of the added entity
   *
   * @return name of the artist, album, song, etc
   */
  private String name;

  public enum Event {
    SONG_ADDED,
    SONG_DELETED,
    SONG_SELECTED,
    PLAYLIST_ADDED,
    PLAYLIST_CHANGED,
    PLAYLIST_DELETED,
    PLAYLIST_LOADED,
    PLAYLIST_SAVED,
    PLAYLIST_CLEARED,
    ARTIST_ADDED,
    ALBUM_ADDED,
    GENRE_ADDED,
    YEAR_ADDED,
    FILE_ADDED,
    MULTIPLE_SONGS_ADDED
  }

  /**
   * Creates a new instance of PlayListChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the specific {@link Event} that occurred
   */
  public PlaylistChangeEvent(Object source, Event event) {
    super(source);
    this.event = event;
  }

  /**
   * Creates a new instance of PlayListChangeEvent
   *
   * @param source the object on which the Event initially occurred
   * @param event the specific {@link Event} that occurred
   * @param name name of the added entity
   */
  public PlaylistChangeEvent(Object source, Event event, String name) {
    super(source);
    this.event = event;
    this.name = name;
  }
}
