package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.*;
import org.bff.javampd.properties.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * MPDDatabase represents a database controller to a MPD server.  To obtain
 * an instance of the class you must use the {@link MPD#getMPDDatabase()} method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDDatabase extends CommandExecutor {

    private final Logger logger = LoggerFactory.getLogger(MPDDatabase.class);
    private DatabaseProperties databaseProperties;
    private MPDServerStatistics serverStatistics;

    private enum ListType {

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

    private enum ListInfoType {

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
     * Class constructor
     *
     * @param mpd a connection to a MPD
     */
    MPDDatabase(MPD mpd) {
        super(mpd);
        this.databaseProperties = new DatabaseProperties();
        this.serverStatistics = mpd.getMPDServerStatistics();
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(MPDArtist artist)}.
     *
     * @param artist the artist to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return findArtist(artist.getName());
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(java.lang.String)}.
     *
     * @param artist the artist to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ARTIST, artist);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        return findGenre(genre.getName());
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findGenre(String genre) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.GENRE, genre);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a year.
     *
     * @param year the year to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findYear(String year) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.DATE, year);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(MPDAlbum album)}.
     *
     * @param album the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return findAlbum(album.getName());
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(String)}.
     *
     * @param album the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ALBUM, album);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artist the artist album belongs to
     * @param album  the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getArtist() != null && song.getArtist().equals(artist)) {
                retList.add(song);
            }
        }

        return retList;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getGenre() != null && song.getGenre().equals(genre.getName())) {
                retList.add(song);
            }
        }

        return retList;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param year  the year to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getYear() != null && song.getYear().equals(year)) {
                retList.add(song);
            }
        }

        return retList;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a title.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchTitle(String title)}.
     *
     * @param title the title to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public Collection<MPDSong> findTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.TITLE, title);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for any criteria.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchAny(String criteria)}.
     *
     * @param criteria the criteria to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public Collection<MPDSong> findAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ANY, criteria);
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs and directories from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listAllFiles() throws MPDConnectionException, MPDDatabaseException {
        try {
            return sendMPDCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs and directories from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listAllFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        try {
            return sendMPDCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listAllSongFiles() throws MPDConnectionException, MPDDatabaseException {
        List<String> fileList;

        try {
            fileList = sendMPDCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return MPDSongConverter.getSongNameList(fileList);
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listAllSongFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        List<String> fileList;
        try {
            fileList = sendMPDCommand(databaseProperties.getListAll(), path);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return MPDSongConverter.getSongNameList(fileList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s of all
     * songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> listAllSongs() throws MPDConnectionException, MPDDatabaseException {
        List<String> songList;

        try {
            songList = sendMPDCommand(databaseProperties.getListAllInfo());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return new ArrayList<MPDSong>(convertResponseToSong(songList));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> listAllSongs(String path) throws MPDConnectionException, MPDDatabaseException {
        List<String> songList;

        try {
            songList = sendMPDCommand(databaseProperties.getListAllInfo(), path);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return new ArrayList<MPDSong>(convertResponseToSong(songList));
    }

    private List<MPDSong> convertResponseToSong(List<String> songList) {
        return MPDSongConverter.convertResponseToSong(songList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(org.bff.javampd.objects.MPDArtist)}.
     *
     * @param artist the artist to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return searchArtist(artist.getName());
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(java.lang.String)}.
     *
     * @param artist the artist to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ARTIST, artist);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(MPDAlbum album)}.
     *
     * @param album the album to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return searchAlbum(album.getName());
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(java.lang.String)}.
     *
     * @param album the album to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ALBUM, album);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findTitle(String title)}.
     *
     * @param title the title to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.TITLE, title);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any criteria.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findAny(java.lang.String)}
     *
     * @param criteria the criteria to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ANY, criteria);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #searchTitle(String title)}.
     *
     * @param title     the title to match
     * @param startYear the starting year
     * @param endYear   the ending year
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchTitle(String title, int startYear, int endYear) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        for (MPDSong song : search(ScopeType.TITLE, title)) {
            int year;

            //Ignore songs that miss the year tag.
            if (song.getYear() == null) {
                continue;
            }

            try {
                if (song.getYear().contains("-")) {
                    year = Integer.parseInt(song.getYear().split("-")[0]);
                } else {
                    year = Integer.parseInt(song.getYear());
                }

                if (year >= startYear && year <= endYear) {
                    retList.add(song);
                }
            } catch (Exception e) {
                logger.error("Problem searching for title", e);
            }
        }

        return retList;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * file name containing the parameter filename.
     *
     * @param fileName the file name to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDSong> searchFileName(String fileName) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.FILENAME, Utils.removeSlashes(fileName));
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums in the database.
     *
     * @return a {@link Collection} of {@link MPDAlbum}s containing the album names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAllAlbums() throws MPDConnectionException, MPDDatabaseException {
        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM)) {
            albums.add(new MPDAlbum(str));
        }
        return albums;
    }

    /**
     * Returns a {@link Collection} of {@link MPDArtist}s of all
     * artists in the database.
     *
     * @return a {@link Collection} of {@link MPDArtist}s containing the album names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDArtist> listAllArtists() throws MPDConnectionException, MPDDatabaseException {
        List<MPDArtist> artists = new ArrayList<MPDArtist>();
        for (String str : list(ListType.ARTIST)) {
            artists.add(new MPDArtist(str));
        }
        return artists;
    }

    /**
     * Returns a {@link Collection} of {@link MPDGenre}s of all
     * genres in the database.
     *
     * @return a {@link Collection} of {@link MPDGenre}s containing the genre names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDGenre> listAllGenres() throws MPDConnectionException, MPDDatabaseException {
        List<MPDGenre> genres = new ArrayList<MPDGenre>();
        for (String str : list(ListType.GENRE)) {
            genres.add(new MPDGenre(str));
        }
        return genres;
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         albums
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(artist.getName());

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            MPDAlbum album = new MPDAlbum(str);
            album.setArtist(artist);
            albums.add(album);
        }
        return albums;
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums by a particular genre.
     *
     * @param genre the genre to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         albums
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            albums.add(new MPDAlbum(str));
        }
        return albums;
    }

    /**
     * Returns a {@link Collection} of {@link MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link Collection} of {@link MPDArtist}s of all
     *         artists
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDArtist> artists = new ArrayList<MPDArtist>();
        for (String str : list(ListType.ARTIST, list)) {
            artists.add(new MPDArtist(str));
        }
        return artists;
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums for a particular year.
     *
     * @param year the year to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         years
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.DATE.getType());
        list.add(year);

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            albums.add(new MPDAlbum(str));
        }
        return albums;
    }

    private Collection<String> listInfo(ListInfoType... types) throws MPDConnectionException, MPDDatabaseException {
        List<String> returnList = new ArrayList<String>();
        List<String> list;

        try {
            list = sendMPDCommand(databaseProperties.getListInfo());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        for (String s : list) {
            for (ListInfoType type : types) {
                if (s.startsWith(type.getPrefix())) {
                    returnList.add(s.substring(type.getPrefix().length()).trim());
                }
            }
        }

        return returnList;
    }

    /**
     * Lists all {@link MPDFile}s for the root directory of the file system.
     *
     * @return a {@code Collection} of {@link MPDFile}
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public Collection<MPDFile> listRootDirectory() throws MPDConnectionException, MPDDatabaseException {
        return listDirectory("");
    }

    /**
     * Lists all {@link MPDFile}s for the given directory of the file system.
     *
     * @param directory the directory to list
     * @return a {@code Collection} of {@link MPDFile}
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the MPD responded with an error or the {@link MPDFile}
     *                                is not a directory.
     */
    public Collection<MPDFile> listDirectory(MPDFile directory) throws MPDConnectionException, MPDDatabaseException {
        if (directory.isDirectory()) {
            return listDirectory(directory.getPath());
        } else {
            throw new MPDDatabaseException(directory.getName() + " is not a directory.");
        }
    }

    private Collection<MPDFile> listDirectory(String directory) throws MPDConnectionException, MPDDatabaseException {
        return listDirectoryInfo(directory);
    }

    private Collection<MPDFile> listDirectoryInfo(String directory) throws MPDConnectionException, MPDDatabaseException {
        List<MPDFile> returnList = new ArrayList<MPDFile>();
        List<String> list;

        try {
            list = sendMPDCommand(databaseProperties.getListInfo(), directory);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        for (String s : list) {

            if (s.startsWith(ListInfoType.FILE.getPrefix())
                    || s.startsWith(ListInfoType.DIRECTORY.getPrefix())) {
                MPDFile f = new MPDFile();

                String name = s;
                if (s.startsWith(ListInfoType.FILE.getPrefix())) {
                    f.setDirectory(false);
                    name = name.substring(ListInfoType.FILE.getPrefix().length()).trim();
                } else {
                    f.setDirectory(true);
                    name = name.substring(ListInfoType.DIRECTORY.getPrefix().length()).trim();
                }

                f.setName(name);
                f.setPath(name);
                returnList.add(f);
            }
        }
        return returnList;
    }

    private Collection<String> list(ListType listType) throws MPDConnectionException, MPDDatabaseException {
        return list(listType, null);
    }

    private Collection<String> list(ListType listType, List<String> params) throws MPDConnectionException, MPDDatabaseException {
        String[] paramList;
        int i = 0;

        if (params != null) {
            paramList = new String[params.size() + 1];
        } else {
            paramList = new String[1];
        }

        paramList[i++] = listType.getType();

        if (params != null) {
            for (String s : params) {
                paramList[i++] = s;
            }
        }

        List<String> responseList;

        try {
            responseList = sendMPDCommand(databaseProperties.getList(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        List<String> retList = new ArrayList<String>();
        for (String s : responseList) {
            try {
                retList.add(s.substring(s.split(":")[0].length() + 1).trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                logger.error("Problem with response array {}", s, e);
                retList.add("");
            }
        }
        return retList;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a searches matching the scope type any.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #find(org.bff.javampd.MPDDatabase.ScopeType, java.lang.String)}.
     *
     * @param searchType the {@link ScopeType}
     * @param param      the search criteria
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the database throws an exception during the search
     */
    public Collection<MPDSong> search(ScopeType searchType, String param) throws MPDConnectionException, MPDDatabaseException {
        String[] paramList;

        if (param != null) {
            paramList = new String[2];
            paramList[1] = param;
        } else {
            paramList = new String[1];
        }

        paramList[0] = searchType.getType();
        List<String> titleList;

        try {
            titleList = sendMPDCommand(databaseProperties.getSearch(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return convertResponseToSong(titleList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a searches matching the scope type any.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #search(org.bff.javampd.MPDDatabase.ScopeType, java.lang.String)}.
     *
     * @param scopeType the {@link ScopeType}
     * @param param     the search criteria
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDDatabaseException   if the database throws an exception during the search
     */
    public Collection<MPDSong> find(ScopeType scopeType, String param) throws MPDConnectionException, MPDDatabaseException {
        String[] paramList;

        if (param != null) {
            paramList = new String[2];
            paramList[1] = param;
        } else {
            paramList = new String[1];
        }
        paramList[0] = scopeType.getType();
        List<String> titleList;

        try {
            titleList = sendMPDCommand(databaseProperties.getFind(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return convertResponseToSong(titleList);
    }

    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public int getArtistCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getArtists();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns the total number of albums in the database.
     *
     * @return the total number of albums
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public int getAlbumCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getAlbums();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns the total number of songs in the database.
     *
     * @return the total number of songs
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public int getSongCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getSongs();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns the sum of all song times in database.
     *
     * @return the sum of all song times
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public long getDbPlayTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getDatabasePlaytime();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns the last database update in UNIX time.
     *
     * @return the last database update in UNIX time
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public long getLastUpdateTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getDatabaseUpdateTime();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns a {@link Collection} of {@link MPDSavedPlaylist}s of all saved playlists.  This is an expensive
     * call so use it cautiously.
     *
     * @return a {@link Collection} of all {@link MPDSavedPlaylist}s
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDConnectionException, MPDDatabaseException {
        List<MPDSavedPlaylist> playlists = new ArrayList<MPDSavedPlaylist>();

        for (String s : listPlaylists()) {
            MPDSavedPlaylist playlist = new MPDSavedPlaylist(s);
            playlist.setSongs(listPlaylistSongs(s));
            playlists.add(playlist);
        }
        return playlists;
    }

    /**
     * Returns a {@link Collection} of all available playlist names on the server.
     *
     * @return a list of playlist names
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listPlaylists() throws MPDConnectionException, MPDDatabaseException {
        try {
            return listInfo(ListInfoType.PLAYLIST);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns a {@link Collection} of all {@link MPDSong}s for a {@link MPDSavedPlaylist}
     *
     * @param playlistName the name of the {@link MPDSavedPlaylist}
     * @return a {@link Collection} of all {@link MPDSong}s for the playlist
     * @throws MPDConnectionException if there is a problem sending the command
     * @throws MPDDatabaseException   if the MPD responded with an error
     */
    public Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> songList = new ArrayList<MPDSong>();
        try {
            List<String> response = sendMPDCommand(databaseProperties.getListSongs(), playlistName);
            for (String song : MPDSongConverter.getSongNameList(response)) {
                songList.add(new ArrayList<MPDSong>(searchFileName(song)).get(0));
            }
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songList;
    }

    /**
     * Returns a {@code Collection} of years for songs in the database.  The years are sorted from least to
     * greatest.
     *
     * @return a {@link Collection} of years
     * @throws MPDDatabaseException   if the MPD responded with an error
     * @throws MPDConnectionException if there is a problem sending the command
     */
    public Collection<String> listAllYears() throws MPDConnectionException, MPDDatabaseException {
        List<String> retList = new ArrayList<String>();
        for (String str : list(ListType.DATE)) {
            if (str.contains("-")) {
                str = str.split("-")[0];
            }

            if (!retList.contains(str)) {
                retList.add(str);
            }
        }
        Collections.sort(retList);
        return retList;
    }
}
