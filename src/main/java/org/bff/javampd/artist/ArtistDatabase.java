package org.bff.javampd.artist;

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
     */
    Collection<MPDArtist> listAllArtists();

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.artist.MPDArtist}s of all
     * artists
     */
    Collection<MPDArtist> listArtistsByGenre(MPDGenre genre);

    /**
     * Returns a {@link org.bff.javampd.artist.MPDArtist} with the passed name.
     *
     * @param name the name of the artist
     * @return a {@link org.bff.javampd.artist.MPDArtist}
     */
    MPDArtist listArtistByName(String name);
}
