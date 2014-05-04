package org.bff.javampd;

import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.objects.*;

import java.util.Collection;

/**
 * @author bill
 */
public interface Database {
    public enum ListType {

        ALBUM("album"),
        ARTIST("artist"),
        GENRE("genre"),
        DATE("date");
        private String type;

        ListType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Defines the scope of items such as find, search.
     */
    public enum ScopeType {

        ALBUM("album"),
        ARTIST("artist"),
        TITLE("title"),
        TRACK("track"),
        NAME("name"),
        GENRE("genre"),
        DATE("date"),
        COMPOSER("composer"),
        PERFORMER("performer"),
        COMMENT("comment"),
        DISC("disc"),
        FILENAME("filename"),
        ANY("any");
        private String type;

        ScopeType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    public enum ListInfoType {

        PLAYLIST("playlist:"),
        DIRECTORY("directory:"),
        FILE("file:");
        private String prefix;

        ListInfoType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(org.bff.javampd.objects.MPDArtist artist)}.
     *
     * @param artist the artist to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findArtist(MPDArtist artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(String)}.
     *
     * @param artist the artist to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findArtist(String artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findGenre(MPDGenre genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findGenre(String genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a year.
     *
     * @param year the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findYear(String year) throws MPDDatabaseException;

    /**
     * Returns a <code>Collection</code> of {@link org.bff.javampd.objects.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(org.bff.javampd.objects.MPDAlbum album)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(String)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbum(String album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artist the artist album belongs to
     * @param album  the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artistName the artist album belongs to
     * @param albumName  the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByArtist(String artistName, String albumName) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param year  the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a title.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchTitle(String title)}.
     *
     * @param title the title to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findTitle(String title) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for any criteria.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchAny(String criteria)}.
     *
     * @param criteria the criteria to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> findAny(String criteria) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs and directories from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllFiles() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs and directories from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllFiles(String path) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllSongFiles() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllSongFiles(String path) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s of all
     * songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> listAllSongs() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> listAllSongs(String path) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(org.bff.javampd.objects.MPDArtist)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(String)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchArtist(String artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(org.bff.javampd.objects.MPDAlbum album)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(String)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAlbum(String album) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findTitle(String title)}.
     *
     * @param title the title to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchTitle(String title) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any criteria.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findAny(String)}
     *
     * @param criteria the criteria to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchAny(String criteria) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #searchTitle(String title)}.
     *
     * @param title     the title to match
     * @param startYear the starting year
     * @param endYear   the ending year
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchTitle(String title, int startYear, int endYear) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * file name containing the parameter filename.
     *
     * @param fileName the file name to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> searchFileName(String fileName) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s containing the album names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAllAlbums() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     * artists in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s containing the album names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDArtist> listAllArtists() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDGenre}s of all
     * genres in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDGenre}s containing the genre names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDGenre> listAllGenres() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums by a particular genre.
     *
     * @param genre the genre to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     * artists
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums for a particular year.
     *
     * @param year the year to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * years
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDDatabaseException;

    /**
     * Lists all {@link org.bff.javampd.MPDFile}s for the root directory of the file system.
     *
     * @return a {@code Collection} of {@link org.bff.javampd.MPDFile}
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDFile> listRootDirectory() throws MPDDatabaseException;

    /**
     * Lists all {@link org.bff.javampd.MPDFile}s for the given directory of the file system.
     *
     * @param directory the directory to list
     * @return a {@code Collection} of {@link org.bff.javampd.MPDFile}
     * @throws MPDDatabaseException if the MPD responded with an error or the {@link org.bff.javampd.MPDFile}
     *                              is not a directory.
     */
    Collection<MPDFile> listDirectory(MPDFile directory) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a searches matching the scope type any.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #find(org.bff.javampd.Database.ScopeType, String)}.
     *
     * @param searchType the {@link ScopeType}
     * @param param      the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the database throws an exception during the search
     */
    Collection<MPDSong> search(ScopeType searchType, String param) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a searches matching the scope type any.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #search(org.bff.javampd.Database.ScopeType, String)}.
     *
     * @param scopeType the {@link ScopeType}
     * @param param     the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException if the database throws an exception during the search
     */
    Collection<MPDSong> find(ScopeType scopeType, String param) throws MPDDatabaseException;

    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    int getArtistCount() throws MPDDatabaseException;

    /**
     * Returns the total number of albums in the database.
     *
     * @return the total number of albums
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    int getAlbumCount() throws MPDDatabaseException;

    /**
     * Returns the total number of songs in the database.
     *
     * @return the total number of songs
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    int getSongCount() throws MPDDatabaseException;

    /**
     * Returns the sum of all song times in database.
     *
     * @return the sum of all song times
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    long getDbPlayTime() throws MPDDatabaseException;

    /**
     * Returns the last database update in UNIX time.
     *
     * @return the last database update in UNIX time
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    long getLastUpdateTime() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSavedPlaylist}s of all saved playlists.  This is an expensive
     * call so use it cautiously.
     *
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSavedPlaylist}s
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of all available playlist names on the server.
     *
     * @return a list of playlist names
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listPlaylists() throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSong}s for a {@link org.bff.javampd.objects.MPDSavedPlaylist}
     *
     * @param playlistName the name of the {@link org.bff.javampd.objects.MPDSavedPlaylist}
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSong}s for the playlist
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDDatabaseException;

    /**
     * Returns a {@code Collection} of years for songs in the database.  The years are sorted from least to
     * greatest.
     *
     * @return a {@link java.util.Collection} of years
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<String> listAllYears() throws MPDDatabaseException;
}
