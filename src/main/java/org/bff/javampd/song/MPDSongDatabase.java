package org.bff.javampd.song;

import com.google.inject.Inject;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MPDSongDatabase represents a song database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MPDDatabaseManager#getSongDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDSongDatabase implements SongDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDSongDatabase.class);

    private SongSearcher songSearcher;

    @Inject
    public MPDSongDatabase(SongSearcher songSearcher) {
        this.songSearcher = songSearcher;
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
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, albumName));

        return songList.stream()
                .filter(song -> song.getArtistName() != null && song.getArtistName().equals(artistName))
                .collect(Collectors.toList());

    }

    @Override
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) throws MPDDatabaseException {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        return songList.stream()
                .filter(song -> song.getGenre() != null && song.getGenre().equals(genre.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) throws MPDDatabaseException {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        return songList.stream()
                .filter(song -> song.getYear() != null && song.getYear().equals(year))
                .collect(Collectors.toList());
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

    @Override
    public MPDSong findSong(String name, String album, String artist) throws MPDDatabaseException {
        List<MPDSong> songs = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album));

        for (MPDSong song : songs) {
            if (artist.equals(song.getArtistName())) {
                return song;
            }
        }
        LOGGER.info("Song not found title --> {}, artist --> {}, album --> {}", name, artist, album);
        return null;
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
