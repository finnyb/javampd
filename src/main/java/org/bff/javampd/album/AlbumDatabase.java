package org.bff.javampd.album;

import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;

import java.util.Collection;

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
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums for a particular year.
     *
     * @param year the year to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * years
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDDatabaseException;


    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDDatabaseException;
}
