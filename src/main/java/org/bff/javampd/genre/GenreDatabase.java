package org.bff.javampd.genre;

import org.bff.javampd.database.MPDDatabaseException;

import java.util.Collection;

/**
 * Database for genre related items
 *
 * @author bill
 */
public interface GenreDatabase {

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.genre.MPDGenre}s of all
     * genres in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.genre.MPDGenre}s containing the genre names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDGenre> listAllGenres() throws MPDDatabaseException;
}
