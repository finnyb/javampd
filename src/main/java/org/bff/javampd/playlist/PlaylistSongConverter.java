package org.bff.javampd.playlist;

import java.util.List;
import org.bff.javampd.song.MPDSong;

/** @author bill */
public interface PlaylistSongConverter {
  /**
   * Converts the response from the MPD server into {@link MPDSong}s.
   *
   * @param list the response from the MPD server
   * @return a MPDSong object
   */
  List<MPDPlaylistSong> convertResponseToSongs(List<String> list);
}
