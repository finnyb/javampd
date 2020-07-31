package org.bff.javampd.playlist;

import java.util.List;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.file.MPDFile;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.song.MPDSong;

/**
 * @author bill
 */
public interface Playlist {
  /**
   * Adds a {@link PlaylistChangeListener} to this object to receive
   * {@link PlaylistChangeEvent}s.
   *
   * @param pcl the PlaylistChangeListener to add
   */
  void addPlaylistChangeListener(PlaylistChangeListener pcl);

  /**
   * Removes a {@link PlaylistChangeListener} from this object.
   *
   * @param pcl the PlaylistChangeListener to remove
   */
  void removePlaylistStatusChangedListener(PlaylistChangeListener pcl);

  /**
   * Loads the songs in the given playlist to the current playlist.  The playlist
   * name can be givin with or without the .m3u extension.
   *
   * @param playlistName the playlist name
   */
  void loadPlaylist(String playlistName);

  /**
   * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist and fires a {@link PlaylistChangeEvent} for event listeners
   *
   * @param song the song to add
   */
  void addSong(MPDSong song);

  /**
   * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist and fires a {@link PlaylistChangeEvent} for event listeners
   *
   * @param file the file to add
   */
  void addSong(String file);

  /**
   * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist.
   *
   * @param file      the song to add
   * @param fireEvent whether to fire song added event for the event listeners
   */
  void addSong(String file, boolean fireEvent);

  /**
   * Adds a {@link org.bff.javampd.song.MPDSong} to the playlist.
   *
   * @param song      the song to add
   * @param fireEvent whether to fire song added event for the event listeners
   */
  void addSong(MPDSong song, boolean fireEvent);

  /**
   * Adds a <CODE>List</CODE> of {@link org.bff.javampd.song.MPDSong}s to the playlist.
   *
   * @param songList the list of songs to add
   * @return true if the songs are added successfully; false otherwise
   */
  boolean addSongs(List<MPDSong> songList);

  /**
   * Adds a <CODE>List</CODE> of {@link org.bff.javampd.song.MPDSong}s to the playlist.
   *
   * @param songList  the list of songs to add
   * @param fireEvent true if a playlist event should be fired after adding
   * @return true if the songs are added successfully; false otherwise
   */
  boolean addSongs(List<MPDSong> songList, boolean fireEvent);

  /**
   * Adds a directory of songs to the playlist.
   *
   * @param file the directory to add
   */
  void addFileOrDirectory(MPDFile file);

  /**
   * Removes a {@link org.bff.javampd.song.MPDSong} from the playlist.
   *
   * @param song the song to remove
   */
  void removeSong(MPDSong song);

  /**
   * Removes a {@link org.bff.javampd.song.MPDSong} from the playlist.
   *
   * @param position the playlist position to remove
   */
  void removeSong(int position);

  /**
   * Returns the current song.
   *
   * @return the current song
   */
  MPDSong getCurrentSong();

  /**
   * Removes all songs from the playlist.
   */
  void clearPlaylist();

  /**
   * Deletes a {@link org.bff.javampd.playlist.MPDSavedPlaylist}
   *
   * @param playlist the {@link org.bff.javampd.playlist.MPDSavedPlaylist}
   */
  void deletePlaylist(MPDSavedPlaylist playlist);

  /**
   * Deletes the playlist from the MPD server.
   *
   * @param playlistName the playlist to delete
   */
  void deletePlaylist(String playlistName);

  /**
   * Moves the desired song to the given position in the playlist.
   *
   * @param song the song to move
   * @param to   the position to move the song to
   */
  void move(MPDSong song, int to);

  /**
   * Shuffles the songs in the playlist.
   */
  void shuffle();

  /**
   * Swaps the given two songs in the playlist.
   *
   * @param song1 first song to swap
   * @param song2 second song to swap
   */
  void swap(MPDSong song1, MPDSong song2);

  /**
   * Saves the current playlist as the passed playlist name.
   *
   * @param playlistName the playlist name for the playlist
   * @return true if the playlist is saved; otherwise false
   */
  boolean savePlaylist(String playlistName);

  /**
   * Adds a {@link org.bff.javampd.album.MPDAlbum} by a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
   *
   * @param artist the {@link org.bff.javampd.artist.MPDArtist} for the album to add
   * @param album  the {@link org.bff.javampd.album.MPDAlbum} to add
   */
  void insertAlbum(MPDArtist artist, MPDAlbum album);

  /**
   * Adds a album by a artist to the playlist.
   *
   * @param artistName the album's artist
   * @param albumName  the album name
   */
  void insertAlbum(String artistName, String albumName);

  /**
   * Adds a {@link org.bff.javampd.album.MPDAlbum} to the playlist.
   *
   * @param album the {@link org.bff.javampd.album.MPDAlbum} to add
   */
  void insertAlbum(MPDAlbum album);

  /**
   * Adds a album to the playlist.
   *
   * @param albumName the album to add
   */
  void insertAlbum(String albumName);

  /**
   * Removes a {@link org.bff.javampd.album.MPDAlbum} by a {@link org.bff.javampd.artist.MPDArtist} from the playlist.
   *
   * @param artist the {@link org.bff.javampd.artist.MPDArtist} for the album to remove
   * @param album  the {@link org.bff.javampd.album.MPDAlbum} to remove
   */
  void removeAlbum(MPDArtist artist, MPDAlbum album);

  /**
   * Removes a album by a artist from the playlist.
   *
   * @param artistName the artist for the album to remove
   * @param albumName  the album to remove
   */
  void removeAlbum(String artistName, String albumName);

  /**
   * Adds a {@link org.bff.javampd.artist.MPDArtist} to the playlist.
   *
   * @param artist the {@link org.bff.javampd.artist.MPDArtist} to add
   */
  void insertArtist(MPDArtist artist);

  /**
   * Adds a artist to the playlist.
   *
   * @param artistName the artist to add
   */
  void insertArtist(String artistName);

  /**
   * Adds a {@link org.bff.javampd.genre.MPDGenre} to the playlist.
   *
   * @param genre the {@link org.bff.javampd.genre.MPDGenre} to add
   */
  void insertGenre(MPDGenre genre);

  /**
   * Adds a genre to the playlist.
   *
   * @param genreName the genre to add
   */
  void insertGenre(String genreName);

  /**
   * Removes a {@link org.bff.javampd.genre.MPDGenre} from the playlist.
   *
   * @param genre the {@link org.bff.javampd.genre.MPDGenre} to remove
   */
  void removeGenre(MPDGenre genre);

  /**
   * Removes a genre from the playlist.
   *
   * @param genreName the artist to remove
   */
  void removeGenre(String genreName);

  /**
   * Adds a year to the playlist.
   *
   * @param year the {@link org.bff.javampd.genre.MPDGenre} to add
   */
  void insertYear(String year);

  /**
   * Removes a year from the playlist.
   *
   * @param year the artist to remove
   */
  void removeYear(String year);

  /**
   * Removes a {@link org.bff.javampd.artist.MPDArtist} from the playlist.
   *
   * @param artist the {@link org.bff.javampd.artist.MPDArtist} to remove
   */
  void removeArtist(MPDArtist artist);

  /**
   * Removes a artist from the playlist.
   *
   * @param artistName the artist to remove
   */
  void removeArtist(String artistName);

  /**
   * Returns the playlist version.
   *
   * @return the playlist version
   */
  int getVersion();

  /**
   * Returns the list of songs in the playlist.  This does query the MPD server for the list so
   * care should be taken not to call it excessively.
   *
   * @return the song list
   */
  List<MPDSong> getSongList();

  /**
   * Returns the string representation of this playlist.
   *
   * @return the string representation
   */
  @Override
  String toString();

  void swap(MPDSong song, int i);
}
