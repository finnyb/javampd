/*
 * MPDDatabase.java
 *
 * Created on September 28, 2005, 8:23 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.*;

import java.util.*;

/**
 * MPDDatabase represents a database controller to a MPD server.  To obtain
 * an instance of the class you must use the {@link MPD#getMPDDatabase()} method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDDatabase {

    private MPD mpd;
    private Properties prop;
    //database properties contants
    private static final String MPDPROPFIND = "MPD_DB_FIND";
    private static final String MPDPROPLIST = "MPD_DB_LIST_TAG";
    private static final String MPDPROPLISTALL = "MPD_DB_LIST_ALL";
    private static final String MPDPROPLISTALLINFO = "MPD_DB_LIST_ALL_INFO";
    private static final String MPDPROPLISTINFO = "MPD_DB_LIST_INFO";
    private static final String MPDPROPSEARCH = "MPD_DB_SEARCH";
    private static final String MPDPROPLISTSONGS = "MPD_DP_LIST_SONGS";

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
            return (type);
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
            return (type);
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
            return (prefix);
        }
    }

    /**
     * Class constructor
     *
     * @param mpd a connection to a MPD
     */
    MPDDatabase(MPD mpd) {
        this.mpd = mpd;
        this.prop = mpd.getMPDProperties();
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(MPDArtist artist)}.
     *
     * @param artist the artist to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return (findArtist(artist.getName()));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an artist.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #searchArtist(java.lang.String)}.
     *
     * @param artist the artist to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.ARTIST, artist));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        return (findGenre(genre.getName()));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a genre.
     *
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findGenre(String genre) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.GENRE, genre));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a year.
     *
     * @param year the year to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findYear(String year) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.DATE, year));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(MPDAlbum album)}.
     *
     * @param album the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return (findAlbum(album.getName()));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album.
     * Please note this only returns an exact match of album.  To find a partial
     * match use {@link #searchAlbum(String)}.
     *
     * @param album the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.ALBUM, album));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param artist the artist album belongs to
     * @param album  the album to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getArtist() != null && song.getArtist().equals(artist)) {
                retList.add(song);
            }
        }

        return (retList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param genre the genre to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getGenre() != null && song.getGenre().equals(genre.getName())) {
                retList.add(song);
            }
        }

        return (retList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an album by
     * a particular artist. Please note this only returns an exact match of album
     * and artist.
     *
     * @param album the album to find
     * @param year  the year to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getYear() != null && song.getYear().equals(year)) {
                retList.add(song);
            }
        }

        return (retList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for a title.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchTitle(String title)}.
     *
     * @param title the title to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public Collection<MPDSong> findTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.TITLE, title));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for any criteria.
     * Please note this only returns an exact match of title.  To find a partial
     * match use {@link #searchAny(String criteria)}.
     *
     * @param criteria the criteria to find
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public Collection<MPDSong> findAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return (find(ScopeType.ANY, criteria));
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs and directories from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<String> listAllFiles() throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALL));

        try {
            return (mpd.sendMPDCommand(command));
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
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<String> listAllFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALL), path);

        try {
            return (mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<String> listAllSongFiles() throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALL));
        List<String> fileList;

        try {
            fileList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        List<String> retList = new ArrayList<String>();

        for (String s : fileList) {
            if (s.startsWith(MPD.SONGPREFIXFILE)) {
                retList.add((s.substring(MPD.SONGPREFIXFILE.length())).trim());
            }
        }

        return (retList);
    }

    /**
     * Returns a {@link Collection} of {@link String}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<String> listAllSongFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALL), path);
        List<String> fileList;

        try {
            fileList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        List<String> retList = new ArrayList<String>();

        for (String s : fileList) {
            if (s.startsWith(MPD.SONGPREFIXFILE)) {
                retList.add((s.substring(MPD.SONGPREFIXFILE.length())).trim());
            }
        }
        return (retList);
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s of all
     * songs from the mpd root.
     *
     * @return a collection of Strings containing all files and directories
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> listAllSongs() throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALLINFO));
        List<String> songList;

        try {
            songList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return (new ArrayList<MPDSong>(mpd.convertResponseToSong(songList)));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s of all
     * songs from the given path.
     *
     * @param path the root of the list
     * @return a collection of Strings containing all files and directories
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> listAllSongs(String path) throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTALLINFO), path);
        List<String> songList;

        try {
            songList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return (new ArrayList<MPDSong>(mpd.convertResponseToSong(songList)));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(org.bff.javampd.objects.MPDArtist)}.
     *
     * @param artist the artist to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return (searchArtist(artist.getName()));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * artist containing the parameter artist.
     * Please note this returns a partial match of an artist.  To find an
     * exact match use {@link #findArtist(java.lang.String)}.
     *
     * @param artist the artist to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return (search(ScopeType.ARTIST, artist));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(MPDAlbum album)}.
     *
     * @param album the album to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return (searchAlbum(album.getName()));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * album containing the parameter album.
     * Please note this returns a partial match of an album.  To find an
     * exact match use {@link #findAlbum(java.lang.String)}.
     *
     * @param album the album to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return (search(ScopeType.ALBUM, album));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any
     * title containing the parameter title.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findTitle(String title)}.
     *
     * @param title the title to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return (search(ScopeType.TITLE, title));
    }

    /**
     * Returns a {@link Collection} of {@link MPDSong}s for an any criteria.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #findAny(java.lang.String)}
     *
     * @param criteria the criteria to match
     * @return a {@link Collection} of {@link MPDSong}s
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return (search(ScopeType.ANY, criteria));
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
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
                e.printStackTrace();
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
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDSong> searchFileName(String fileName) throws MPDConnectionException, MPDDatabaseException {
        fileName = Utils.removeSlashes(fileName);
        return (search(ScopeType.FILENAME, fileName));
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums in the database.
     *
     * @return a {@link Collection} of {@link MPDAlbum}s containing the album names
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAllAlbums() throws MPDConnectionException, MPDDatabaseException {
        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM)) {
            albums.add(new MPDAlbum(str));
        }
        return (albums);
    }

    /**
     * Returns a {@link Collection} of {@link MPDArtist}s of all
     * artists in the database.
     *
     * @return a {@link Collection} of {@link MPDArtist}s containing the album names
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDArtist> listAllArtists() throws MPDConnectionException, MPDDatabaseException {
        List<MPDArtist> artists = new ArrayList<MPDArtist>();
        for (String str : list(ListType.ARTIST)) {
            artists.add(new MPDArtist(str));
        }
        return (artists);
    }

    /**
     * Returns a {@link Collection} of {@link MPDGenre}s of all
     * genres in the database.
     *
     * @return a {@link Collection} of {@link MPDGenre}s containing the genre names
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDGenre> listAllGenres() throws MPDConnectionException, MPDDatabaseException {
        List<MPDGenre> genres = new ArrayList<MPDGenre>();
        for (String str : list(ListType.GENRE)) {
            genres.add(new MPDGenre(str));
        }
        return (genres);
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums by a particular artist.
     *
     * @param artist the artist to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         albums
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is aproblem sending the command
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
        return (albums);
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums by a particular genre.
     *
     * @param genre the genre to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         albums
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is aproblem sending the command
     */
    public Collection<MPDAlbum> listAlbumsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            albums.add(new MPDAlbum(str));
        }
        return (albums);
    }

    /**
     * Returns a {@link Collection} of {@link MPDArtist}s of all
     * artists by a particular genre.
     *
     * @param genre the genre to find artists
     * @return a {@link Collection} of {@link MPDArtist}s of all
     *         artists
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is aproblem sending the command
     */
    public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDArtist> artists = new ArrayList<MPDArtist>();
        for (String str : list(ListType.ARTIST, list)) {
            artists.add(new MPDArtist(str));
        }
        return (artists);
    }

    /**
     * Returns a {@link Collection} of {@link MPDAlbum}s of all
     * albums for a particular year.
     *
     * @param year the year to find albums
     * @return a {@link Collection} of {@link MPDAlbum}s of all
     *         years
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<MPDAlbum> listAlbumsByYear(String year) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(ListType.DATE.getType());
        list.add(year);

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            albums.add(new MPDAlbum(str));
        }
        return (albums);
    }

    private Collection<String> listInfo(ListInfoType... types) throws MPDConnectionException, MPDDatabaseException {
        List<String> returnList = new ArrayList<String>();
        List<String> list;
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTINFO));

        try {
            list = new ArrayList<String>(mpd.sendMPDCommand(command));
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

        return (returnList);
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
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTINFO), directory);

        try {
            list = new ArrayList<String>(mpd.sendMPDCommand(command));
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
        return (returnList);
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

        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLIST), paramList);
        List<String> responseList;

        try {
            responseList = new ArrayList<String>(mpd.sendMPDCommand(command));
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
                System.out.println("String with array problem:" + s);
                retList.add("");
            }
        }
        return (retList);
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
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPSEARCH), paramList);
        List<String> titleList;

        try {
            titleList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return (mpd.convertResponseToSong(titleList));
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
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPFIND), paramList);
        List<String> titleList;

        try {
            titleList = new ArrayList<String>(mpd.sendMPDCommand(command));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return (mpd.convertResponseToSong(titleList));
    }

    /**
     * Returns the total number of artists in the database.
     *
     * @return the total number of artists
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public int getArtistCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (Integer.parseInt(mpd.getServerStat(MPD.StatList.ARTISTS)));
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public int getAlbumCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (Integer.parseInt(mpd.getServerStat(MPD.StatList.ALBUMS)));
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public int getSongCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (Integer.parseInt(mpd.getServerStat(MPD.StatList.SONGS)));
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public long getDbPlayTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (Long.parseLong(mpd.getServerStat(MPD.StatList.DBPLAYTIME)));
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public long getLastUpdateTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (Long.parseLong(mpd.getServerStat(MPD.StatList.DBUPDATE)));
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    /**
     * Returns the mpd this database class is using.
     *
     * @return the mpd connection
     */
    public MPD getMpd() {
        return mpd;
    }

    /**
     * Set the mpd connection this class is using.
     *
     * @param mpd the mpd to use
     */
    public void setMpd(MPD mpd) {
        this.mpd = mpd;
    }

    /**
     * Returns a {@link Collection} of {@link MPDSavedPlaylist}s of all saved playlists.  This is an expensive
     * call so use it cautiously.
     *
     * @return a {@link Collection} of all {@link MPDSavedPlaylist}s
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
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
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     */
    public Collection<String> listPlaylists() throws MPDConnectionException, MPDDatabaseException {
        try {
            return (listInfo(ListInfoType.PLAYLIST));
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
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     */
    public Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDConnectionException, MPDDatabaseException {
        MPDCommand command = new MPDCommand(prop.getProperty(MPDPROPLISTSONGS), playlistName);
        List<String> response;
        List<MPDSong> songList;
        try {
            response = new ArrayList<String>(mpd.sendMPDCommand(command));
            songList = new ArrayList<MPDSong>();
            for (String s : response) {
                songList.add(new ArrayList<MPDSong>(searchFileName(s.substring(MPD.SONGPREFIXFILE.length()).trim())).get(0));
            }
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return (songList);
    }

    /**
     * Returns a {@code Collection} of years for songs in the database.  The years are sorted from least to
     * greatest.
     *
     * @return a {@link Collection} of years
     * @throws org.bff.javampd.exception.MPDDatabaseException
     *          if the MPD responded with an error
     * @throws org.bff.javampd.exception.MPDConnectionException
     *          if there is a problem sending the command
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
        return (retList);
    }
}
