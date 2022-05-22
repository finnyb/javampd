package org.bff.javampd.playlist;

import java.util.Collection;
import lombok.Builder;
import lombok.Data;
import org.bff.javampd.song.MPDSong;

/**
 * MPDSavedPlaylist represents a saved playlist.
 *
 * @author Bill
 */
@Builder(builderMethodName = "internalBuilder")
@Data
public class MPDSavedPlaylist {
  private String name;
  private Collection<MPDSong> songs;

  public static MPDSavedPlaylist.MPDSavedPlaylistBuilder builder(String name) {
    return internalBuilder().name(name);
  }
}
