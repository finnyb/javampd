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
     * Returns a windowed list of {@link org.bff.javampd.album.MPDAlbum}s.  If the start
     * or end indexes fall outside the range they are adjusted to either the start or end
     * of the list.
     *
     * @param start starting record number
     * @param end   ending record number
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

    /**
     * Returns the {@link org.bff.javampd.album.MPDAlbum} for the passed {@link org.bff.javampd.artist.MPDArtist}.
     * Returns null if no album found.
     *
     * @param artist    the {@link org.bff.javampd.artist.MPDArtist}
     * @param albumName the albums name
     * @return the {@link org.bff.javampd.album.MPDAlbum}, null if nothing found
     */
    MPDAlbum findAlbumByArtist(MPDArtist artist, String albumName);
}
