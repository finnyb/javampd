package org.bff.javampd.album;

import org.bff.javampd.artist.MPDArtist;
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
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s by the
     * {@link MPDArtist}
     */
    Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist);

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums by an album artist.  An album artist denotes the artist for a musical release, as distinct from artists
     * for the tracks that constitute a release
     *
     * @param albumArtist the album artist to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s by the
     * {@link MPDArtist}
     */
    Collection<MPDAlbum> listAlbumsByAlbumArtist(MPDArtist albumArtist);


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
     * be very slow for large collections.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s of all
     * albums
     */
    Collection<MPDAlbum> listAllAlbums();

    /**
     * Returns a list of {@link MPDAlbum}s for the album name.
     *
     * @param albumName the album's name
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.album.MPDAlbum}s
     */
    Collection<MPDAlbum> findAlbum(String albumName);
}
