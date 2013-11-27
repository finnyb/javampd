package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
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
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(String)}.
     *
     * @param artist the artist to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findArtist(String artist) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findGenre(String genre) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a year.
     *
     * @param year the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findYear(String year) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a <code>Collection</code> of {@link org.bff.javampd.objects.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(org.bff.javampd.objects.MPDAlbum album)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(String)}.
     *
     * @param album the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findAlbum(String album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artist the artist album belongs to
     * @param album  the album to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param genre the genre to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param year  the year to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a title.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchTitle(String title)}.
     *
     * @param title the title to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    Collection<MPDSong> findTitle(String title) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for any criteria.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchAny(String criteria)}.
     *
     * @param criteria the criteria to find
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    Collection<MPDSong> findAny(String criteria) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs and directories from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listAllFiles() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs and directories from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listAllFiles(String path) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listAllSongFiles() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link String}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listAllSongFiles(String path) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s of all
     * songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> listAllSongs() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> listAllSongs(String path) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(org.bff.javampd.objects.MPDArtist)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(String)}.
     *
     * @param artist the artist to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchArtist(String artist) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(org.bff.javampd.objects.MPDAlbum album)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(String)}.
     *
     * @param album the album to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchAlbum(String album) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findTitle(String title)}.
     *
     * @param title the title to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchTitle(String title) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any criteria.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findAny(String)}
     *
     * @param criteria the criteria to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchAny(String criteria) throws MPDConnectionException, MPDDatabaseException;

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
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchTitle(String title, int startYear, int endYear) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for an any
     * file name containing the parameter filename.
     *
     * @param fileName the file name to match
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDSong> searchFileName(String fileName) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s containing the album names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDAlbum> listAllAlbums() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     * artists in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s containing the album names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDArtist> listAllArtists() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDGenre}s of all
     * genres in the database.
     *
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDGenre}s containing the genre names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDGenre> listAllGenres() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     *         albums
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums by a particular genre.
     *
     * @param genre the genre to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     *         albums
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDArtist}s of all
     *         artists
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     * albums for a particular year.
     *
     * @param year the year to find albums
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDAlbum}s of all
     *         years
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Lists all {@link org.bff.javampd.MPDFile}s for the root directory of the file system.
     *
     * @return a {@code Collection} of {@link org.bff.javampd.MPDFile}
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    Collection<MPDFile> listRootDirectory() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Lists all {@link org.bff.javampd.MPDFile}s for the given directory of the file system.
     *
     * @param directory the directory to list
     * @return a {@code Collection} of {@link org.bff.javampd.MPDFile}
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the MPD responded with an error or the {@link org.bff.javampd.MPDFile}
     *                                is not a directory.
     */
    Collection<MPDFile> listDirectory(MPDFile directory) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a searches matching the scope type any.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #find(org.bff.javampd.Database.ScopeType, String)}.
     *
     * @param searchType the {@link ScopeType}
     * @param param      the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the database throws an exception during the search
     */
    Collection<MPDSong> search(ScopeType searchType, String param) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s for a searches matching the scope type any.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #search(org.bff.javampd.Database.ScopeType, String)}.
     *
     * @param scopeType the {@link ScopeType}
     * @param param     the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSong}s
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the database throws an exception during the search
     */
    Collection<MPDSong> find(ScopeType scopeType, String param) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    int getArtistCount() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns the total number of albums in the database.
     *
     * @return the total number of albums
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    int getAlbumCount() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns the total number of songs in the database.
     *
     * @return the total number of songs
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    int getSongCount() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns the sum of all song times in database.
     *
     * @return the sum of all song times
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    long getDbPlayTime() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns the last database update in UNIX time.
     *
     * @return the last database update in UNIX time
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    long getLastUpdateTime() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.objects.MPDSavedPlaylist}s of all saved playlists.  This is an expensive
     * call so use it cautiously.
     *
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSavedPlaylist}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of all available playlist names on the server.
     *
     * @return a list of playlist names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listPlaylists() throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSong}s for a {@link org.bff.javampd.objects.MPDSavedPlaylist}
     *
     * @param playlistName the name of the {@link org.bff.javampd.objects.MPDSavedPlaylist}
     * @return a {@link java.util.Collection} of all {@link org.bff.javampd.objects.MPDSong}s for the playlist
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDConnectionException, MPDDatabaseException;

    /**
     * Returns a {@code Collection} of years for songs in the database.  The years are sorted from least to
     * greatest.
     *
     * @return a {@link java.util.Collection} of years
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    Collection<String> listAllYears() throws MPDConnectionException, MPDDatabaseException;
}
