package org.bff.javampd.song;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;

import java.util.Collection;

/**
 * Database for song related items
 *
 * @author bill
 */
public interface SongDatabase {

    /**
     * Returns a <code>Collection</code> of {@link org.bff.javampd.song.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(org.bff.javampd.album.MPDAlbum album)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws org.bff.javampd.database.MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(String)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbum(String album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artist the artist album belongs to
     * @param album  the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artistName the artist album belongs to
     * @param albumName  the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByArtist(String artistName, String albumName) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param year  the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(org.bff.javampd.album.MPDAlbum album)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(String)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAlbum(String album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(org.bff.javampd.artist.MPDArtist artist)}.
     *
     * @param artist the artist to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findArtist(MPDArtist artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(String)}.
     *
     * @param artist the artist to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findArtist(String artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(org.bff.javampd.artist.MPDArtist)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(String)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchArtist(String artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a year.
     *
     * @param year the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findYear(String year) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a title.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchTitle(String title)}.
     *
     * @param title the title to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findTitle(String title) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for any criteria.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchAny(String criteria)}.
     *
     * @param criteria the criteria to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAny(String criteria) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findTitle(String title)}.
     *
     * @param title the title to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchTitle(String title) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #searchTitle(String title)}.
     *
     * @param title     the title to match
     * @param startYear the starting year
     * @param endYear   the ending year
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchTitle(String title, int startYear, int endYear) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any criteria.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findAny(String)}
     *
     * @param criteria the criteria to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAny(String criteria) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for an any
     * file name containing the parameter filename.
     *
     * @param fileName the file name to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchFileName(String fileName) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findGenre(MPDGenre genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findGenre(String genre) throws MPDDatabaseException;


    /**
     * Returns a {@link org.bff.javampd.song.MPDSong} for the given album and artist
     *
     * @param name   name of the {@link MPDSong}
     * @param album  name of the album
     * @param artist name of the artist
     * @return the {@link MPDSong or null if none found}
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    MPDSong findSong(String name, String album, String artist) throws MPDDatabaseException;
}
