package org.bff.javampd.playlist;

import java.util.Collection;
import java.util.Objects;
import org.bff.javampd.MPDItem;
import org.bff.javampd.song.MPDSong;

/**
 * MPDSavedPlaylist represents a saved playlist.
 *
 * @author Bill
 */
public class MPDSavedPlaylist extends MPDItem {
  private Collection<MPDSong> songs;

  /**
   * Creates a MPDSavedPlaylist object
   *
   * @param name the name of the saved playlist
   */
  public MPDSavedPlaylist(String name) {
    super(name);
  }

  /**
   * Returns the list of {@link MPDSong}s for the playlist
   *
   * @return a {@link Collection} of {@link MPDSong}s
   */
  public Collection<MPDSong> getSongs() {
    return songs;
  }

  /**
   * Sets the {@link MPDSong}s for the playlist
   *
   * @param songs the {@link Collection} of {@link MPDSong}s
   */
  public void setSongs(Collection<MPDSong> songs) {
    this.songs = songs;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    if (!super.equals(o)) {
      return false;
    }

    MPDSavedPlaylist that = (MPDSavedPlaylist) o;

    return Objects.equals(songs, that.songs);
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (songs != null ? songs.hashCode() : 0);
    return result;
  }
}
