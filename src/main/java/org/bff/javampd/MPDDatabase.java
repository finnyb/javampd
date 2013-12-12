package org.bff.javampd;

import com.google.inject.Inject;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDDatabaseException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.objects.*;
import org.bff.javampd.properties.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * MPDDatabase represents a database controller to a {@link MPD}.  To obtain
 * an instance of the class you must use the {@link MPD#getDatabase()} method from
 * the {@link MPD} connection class.  This class does not have a public constructor
 * (singleton model) so the object must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDDatabase implements Database {

    private final Logger logger = LoggerFactory.getLogger(MPDDatabase.class);
    @Inject
    private DatabaseProperties databaseProperties;
    @Inject
    private ServerStatistics serverStatistics;
    @Inject
    private CommandExecutor commandExecutor;

    protected MPDDatabase() {
    }

    @Override
    public Collection<MPDSong> findArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return findArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> findArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> findGenre(MPDGenre genre) throws MPDConnectionException, MPDDatabaseException {
        return findGenre(genre.getName());
    }

    @Override
    public Collection<MPDSong> findGenre(String genre) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.GENRE, genre);
    }

    @Override
    public Collection<MPDSong> findYear(String year) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.DATE, year);
    }

    @Override
    public Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return findAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> findAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<MPDSong>();

        List<MPDSong> songList = new ArrayList<MPDSong>(find(ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getArtistName() != null && song.getArtistName().equals(artist.getName())) {
                retList.add(song);
            }
        }

        return retList;
    }

    @Override
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

    @Override
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

    @Override
    public Collection<MPDSong> findTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> findAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return find(ScopeType.ANY, criteria);
    }

    @Override
    public Collection<String> listAllFiles() throws MPDConnectionException, MPDDatabaseException {
        try {
            return commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<String> listAllFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        try {
            return commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<String> listAllSongFiles() throws MPDConnectionException, MPDDatabaseException {
        List<String> fileList;

        try {
            fileList = commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return MPDSongConverter.getSongNameList(fileList);
    }

    @Override
    public Collection<String> listAllSongFiles(String path) throws MPDConnectionException, MPDDatabaseException {
        List<String> fileList;
        try {
            fileList = commandExecutor.sendCommand(databaseProperties.getListAll(), path);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return MPDSongConverter.getSongNameList(fileList);
    }

    @Override
    public Collection<MPDSong> listAllSongs() throws MPDConnectionException, MPDDatabaseException {
        List<String> songList;

        try {
            songList = commandExecutor.sendCommand(databaseProperties.getListAllInfo());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return new ArrayList<MPDSong>(convertResponseToSong(songList));
    }

    @Override
    public Collection<MPDSong> listAllSongs(String path) throws MPDConnectionException, MPDDatabaseException {
        List<String> songList;

        try {
            songList = commandExecutor.sendCommand(databaseProperties.getListAllInfo(), path);
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

    @Override
    public Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        return searchArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> searchArtist(String artist) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDConnectionException, MPDDatabaseException {
        return searchAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> searchAlbum(String album) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> searchTitle(String title) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> searchAny(String criteria) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.ANY, criteria);
    }

    @Override
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

    @Override
    public Collection<MPDSong> searchFileName(String fileName) throws MPDConnectionException, MPDDatabaseException {
        return search(ScopeType.FILENAME, Utils.removeSlashes(fileName));
    }

    @Override
    public Collection<MPDAlbum> listAllAlbums() throws MPDConnectionException, MPDDatabaseException {
        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (MPDArtist artist : listAllArtists()) {
            albums.addAll(listAlbumsByArtist(artist));
        }

        Collections.sort(albums, new Comparator<MPDAlbum>() {
            public int compare(MPDAlbum one, MPDAlbum other) {
                return one.getName().compareTo(other.getName());
            }
        });

        return albums;
    }

    @Override
    public Collection<MPDArtist> listAllArtists() throws MPDConnectionException, MPDDatabaseException {
        List<MPDArtist> artists = new ArrayList<MPDArtist>();
        for (String str : list(ListType.ARTIST)) {
            artists.add(new MPDArtist(str));
        }
        return artists;
    }

    @Override
    public Collection<MPDGenre> listAllGenres() throws MPDConnectionException, MPDDatabaseException {
        List<MPDGenre> genres = new ArrayList<MPDGenre>();
        for (String str : list(ListType.GENRE)) {
            genres.add(new MPDGenre(str));
        }
        return genres;
    }

    @Override
    public Collection<MPDAlbum> listAlbumsByArtist(MPDArtist artist) throws MPDConnectionException, MPDDatabaseException {
        List<String> list = new ArrayList<String>();
        list.add(artist.getName());

        List<MPDAlbum> albums = new ArrayList<MPDAlbum>();
        for (String str : list(ListType.ALBUM, list)) {
            MPDAlbum album = new MPDAlbum(str);
            album.setArtistName(artist.getName());
            albums.add(album);
        }
        return albums;
    }

    @Override
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

    @Override
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

    @Override
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
            list = commandExecutor.sendCommand(databaseProperties.getListInfo());
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

    @Override
    public Collection<MPDFile> listRootDirectory() throws MPDConnectionException, MPDDatabaseException {
        return listDirectory("");
    }

    @Override
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
            list = commandExecutor.sendCommand(databaseProperties.getListInfo(), directory);
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
            responseList = commandExecutor.sendCommand(databaseProperties.getList(), paramList);
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

    @Override
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
            titleList = commandExecutor.sendCommand(databaseProperties.getSearch(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return convertResponseToSong(titleList);
    }

    @Override
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
            titleList = commandExecutor.sendCommand(databaseProperties.getFind(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return convertResponseToSong(titleList);
    }

    @Override
    public int getArtistCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getArtists();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public int getAlbumCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getAlbums();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public int getSongCount() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getSongs();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public long getDbPlayTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getDatabasePlaytime();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public long getLastUpdateTime() throws MPDConnectionException, MPDDatabaseException {
        try {
            return serverStatistics.getDatabaseUpdateTime();
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<MPDSavedPlaylist> listSavedPlaylists() throws MPDConnectionException, MPDDatabaseException {
        List<MPDSavedPlaylist> playlists = new ArrayList<MPDSavedPlaylist>();

        for (String s : listPlaylists()) {
            MPDSavedPlaylist playlist = new MPDSavedPlaylist(s);
            playlist.setSongs(listPlaylistSongs(s));
            playlists.add(playlist);
        }
        return playlists;
    }

    @Override
    public Collection<String> listPlaylists() throws MPDConnectionException, MPDDatabaseException {
        try {
            return listInfo(ListInfoType.PLAYLIST);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<MPDSong> listPlaylistSongs(String playlistName) throws MPDConnectionException, MPDDatabaseException {
        List<MPDSong> songList = new ArrayList<MPDSong>();
        try {
            List<String> response = commandExecutor.sendCommand(databaseProperties.getListSongs(), playlistName);
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

    @Override
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
