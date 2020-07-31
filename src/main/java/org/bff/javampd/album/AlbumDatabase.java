package org.bff.javampd.album;

import java.util.Collection;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;

/**
 * Database for artist related items
 *
 * @author bill
 */
public interface AlbumDatabase {
  /**
   * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums by a particular genre.
   *
   * @param genre the genre to find albums
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums
   */
  Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre);

  /**
   * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums for a particular year.
   *
   * @param year the year to find albums
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * years
   */
  Collection<MPDAlbum> listAlbumsByYear(String year);

  /**
   * Returns a {@link java.util.Collection} of album names of all
   * albums for a particular year.
   *
   * @param year the year to find albums
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * years
   */
  Collection<String> listAlbumNamesByYear(String year);

  /**
   * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums by a particular artist.
   *
   * @param artist the artist to find albums
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s by the
   * {@link MPDArtist}
   */
  Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist);

  /**
   * Returns a list of album names.  To hydrate the {@link org.bff.javampd.album.MPDAlbum} call
   * #findAlbum(String).  This method will return an empty string album name if one exists
   * in your collection.
   *
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums
   */
  Collection<String> listAllAlbumNames();

  /**
   * Returns a list of all {@link org.bff.javampd.album.MPDAlbum}s.  This could
   * be very slow for large collections.  A better approach is to use the windowed
   * method {@link #listAllAlbums(int, int)}
   *
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums
   */
  Collection<MPDAlbum> listAllAlbums();

  /**
   * Returns a windowed list of {@link org.bff.javampd.album.MPDAlbum}s between the specified
   * <tt>fromIndex</tt>, inclusive, and <tt>toIndex</tt>, exclusive.  (If
   * <tt>fromIndex</tt> and <tt>toIndex</tt> are equal, the returned list is
   * empty.)
   *
   * @param start starting number
   * @param end   ending number
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
   * albums
   */
  Collection<MPDAlbum> listAllAlbums(int start, int end);

  /**
   * Returns a list of {@link MPDAlbum}s for the album name.
   *
   * @param albumName the album's name
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s
   */
  Collection<MPDAlbum> findAlbum(String albumName);
}
