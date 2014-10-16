package org.bff.javampd.artist;

import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;

import java.util.Collection;

/**
 * Database for artist related items
 *
 * @author bill
 */
public interface ArtistDatabase {
    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s of all
     * artists in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s containing the album names
     * @throws org.bff.javampd.database.MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDArtist> listAllArtists() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s of all
     * artists
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDDatabaseException;
}
