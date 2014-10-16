package org.bff.javampd.year;

import org.bff.javampd.database.MPDDatabaseException;

import java.util.Collection;

/**
 * Database for year related items
 *
 * @author bill
 */
public interface YearDatabase {

    /**
     * Returns a {@code Collection} of years for songs in the database.  The years are sorted from least to
     * greatest.
     *
     * @return a {@link java.util.Collection} of years
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllYears() throws MPDDatabaseException;
}
