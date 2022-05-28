package org.bff.javampd.genre;

import java.util.Collection;

/**
 * Database for genre related items
 *
 * @author bill
 */
public interface GenreDatabase {

  /**
   * Returns a {@link java.util.Collection} of {@link org.bff.javampd.genre.MPDGenre}s of all genres
   * in the database.
   *
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.genre.MPDGenre}s containing
   *     the genre names
   */
  Collection<MPDGenre> listAllGenres();

  /**
   * Returns a {@link org.bff.javampd.genre.MPDGenre} with the passed name.
   *
   * @param name the name of the genre
   * @return a {@link org.bff.javampd.genre.MPDGenre}
   */
  MPDGenre listGenreByName(String name);
}
