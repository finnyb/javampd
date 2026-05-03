package org.bff.javampd.playlist;

import java.util.Collection;
import org.bff.javampd.song.MPDSong;

/**
 * Database for playlist related items
 *
 * @author bill
 */
public interface PlaylistDatabase {

  /**
   * Returns a {@link java.util.Collection} of {@link org.bff.javampd.playlist.MPDSavedPlaylist}s of
   * all saved playlists. This is an expensive call so use it cautiously.
   *
   * @return a {@link java.util.Collection} of all {@link
   *     org.bff.javampd.playlist.MPDSavedPlaylist}s
   */
  Collection<MPDSavedPlaylist> listSavedPlaylists();

  /**
   * Returns a {@link java.util.Collection} of all available playlist names on the server.
   *
   * @return a list of playlist names
   */
  Collection<String> listPlaylists();

  /**
   * Returns a {@link java.util.Collection} of all songs in this playlist on the server.
   *
   * @param playlistName the name of the playlist
   * @return a collection of songs
   */
  Collection<MPDSong> listPlaylistSongs(String playlistName);

  /**
   * Returns a range of songs in this playlist on the server. The range is specified by the
   * parameters {@code start} and {@code end}.
   *
   * @param playlistName the name of the playlist
   * @param start the start of the range
   * @param count how many items will be returned at most
   * @return a collection of songs
   */
  Collection<MPDSong> listPlaylistSongs(String playlistName, long start, long count);

  /**
   * Returns a {@link java.util.Collection} of the file names of all songs in this playlist on the
   * server.
   *
   * @param playlistName the name of the playlist
   * @return a collection of file names of songs
   */
  Collection<String> listRawPlaylistSongs(String playlistName);

  int countPlaylistSongs(String playlistName);
}
