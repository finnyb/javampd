package org.bff.javampd.song;

import com.google.inject.Inject;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.properties.DatabaseProperties;
import org.bff.javampd.server.MPDResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDSongDatabase represents a song database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the {@link org.bff.javampd.server.MPD#getArtistDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDSongDatabase implements SongDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDSongDatabase.class);

    private DatabaseProperties databaseProperties;
    private CommandExecutor commandExecutor;
    private SongSearcher songSearcher;
    private SongConverter songConverter;

    @Inject
    public MPDSongDatabase(DatabaseProperties databaseProperties,
                           CommandExecutor commandExecutor,
                           SongSearcher songSearcher,
                           SongConverter songConverter) {
        this.databaseProperties = databaseProperties;
        this.commandExecutor = commandExecutor;
        this.songSearcher = songSearcher;
        this.songConverter = songConverter;
    }

    @Override
    public Collection<MPDSong> findAlbum(MPDAlbum album) throws MPDDatabaseException {
        return findAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> findAlbum(String album) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) throws MPDDatabaseException {
        return findAlbumByArtist(artist.getName(), album.getName());
    }

    @Override
    public Collection<MPDSong> findAlbumByArtist(String artistName, String albumName) throws MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<>();

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, albumName));

        for (MPDSong song : songList) {
            if (song.getArtistName() != null && song.getArtistName().equals(artistName)) {
                retList.add(song);
            }
        }

        return retList;
    }

    @Override
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<>();

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getGenre() != null && song.getGenre().equals(genre.getName())) {
                retList.add(song);
            }
        }

        return retList;
    }

    @Override
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<>();

        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        for (MPDSong song : songList) {
            if (song.getYear() != null && song.getYear().equals(year)) {
                retList.add(song);
            }
        }

        return retList;
    }

    @Override
    public Collection<MPDSong> searchAlbum(MPDAlbum album) throws MPDDatabaseException {
        return searchAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> searchAlbum(String album) throws MPDDatabaseException {
        return songSearcher.search(SongSearcher.ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> findArtist(MPDArtist artist) throws MPDDatabaseException {
        return findArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> findArtist(String artist) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> searchArtist(MPDArtist artist) throws MPDDatabaseException {
        return searchArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> searchArtist(String artist) throws MPDDatabaseException {
        return songSearcher.search(SongSearcher.ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> findYear(String year) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.DATE, year);
    }

    @Override
    public Collection<MPDSong> findTitle(String title) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> findAny(String criteria) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.ANY, criteria);
    }

    @Override
    public Collection<MPDSong> listAllSongs() throws MPDDatabaseException {
        List<String> songList;

        try {
            songList = commandExecutor.sendCommand(databaseProperties.getListAllInfo());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return new ArrayList<>(songConverter.convertResponseToSong(songList));
    }

    @Override
    public Collection<MPDSong> listAllSongs(String path) throws MPDDatabaseException {
        List<String> songList;

        try {
            songList = commandExecutor.sendCommand(databaseProperties.getListAllInfo(), path);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return new ArrayList<>(songConverter.convertResponseToSong(songList));
    }


    @Override
    public Collection<MPDSong> searchTitle(String title) throws MPDDatabaseException {
        return songSearcher.search(SongSearcher.ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> searchAny(String criteria) throws MPDDatabaseException {
        return songSearcher.search(SongSearcher.ScopeType.ANY, criteria);
    }

    @Override
    public Collection<MPDSong> searchTitle(String title, int startYear, int endYear) throws MPDDatabaseException {
        List<MPDSong> retList = new ArrayList<>();

        for (MPDSong song : songSearcher.search(SongSearcher.ScopeType.TITLE, title)) {
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
                LOGGER.error("Problem searching for title", e);
            }
        }

        return retList;
    }

    @Override
    public Collection<MPDSong> searchFileName(String fileName) throws MPDDatabaseException {
        return songSearcher.search(SongSearcher.ScopeType.FILENAME, removeSlashes(fileName));
    }

    @Override
    public Collection<MPDSong> findGenre(MPDGenre genre) throws MPDDatabaseException {
        return findGenre(genre.getName());
    }

    @Override
    public Collection<MPDSong> findGenre(String genre) throws MPDDatabaseException {
        return songSearcher.find(SongSearcher.ScopeType.GENRE, genre);
    }

    /**
     * Removes leading or trailing slashes from a string.
     *
     * @param path the string to remove leading or trailing slashes
     * @return the string without leading or trailing slashes
     */
    private static String removeSlashes(String path) {
        String retString = path;
        String slash = System.getProperty("file.separator");

        //if non-Unix slashes replace
        retString = retString.replace(slash, "/");

        retString = retString.replaceFirst("^/", "");
        retString = retString.replaceFirst("/$", "");

        return retString;
    }
}
