package org.bff.javampd.song;

import com.google.inject.Inject;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDSongDatabase represents a song database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getSongDatabase()} method from
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
    public Collection<MPDSong> findAlbum(MPDAlbum album) {
        return findAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> findAlbum(String album) {
        return songSearcher.find(SongSearcher.ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> findAlbumByArtist(MPDArtist artist, MPDAlbum album) {
        return findAlbumByArtist(artist.getName(), album.getName());
    }

    @Override
    public Collection<MPDSong> findAlbumByArtist(String artistName, String albumName) {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, albumName));

        List<MPDSong> list = new ArrayList<>();
        for (MPDSong song : songList) {
            if (song.getArtistName() != null && song.getArtistName().equals(artistName)) {
                list.add(song);
            }
        }
        return list;

    }

    @Override
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        List<MPDSong> list = new ArrayList<>();
        for (MPDSong song : songList) {
            if (song.getGenre() != null && song.getGenre().equals(genre.getName())) {
                list.add(song);
            }
        }
        return list;
    }

    @Override
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        List<MPDSong> list = new ArrayList<>();
        for (MPDSong song : songList) {
            if (song.getYear() != null && song.getYear().equals(year)) {
                list.add(song);
            }
        }
        return list;
    }

    @Override
    public Collection<MPDSong> searchAlbum(MPDAlbum album) {
        return searchAlbum(album.getName());
    }

    @Override
    public Collection<MPDSong> searchAlbum(String album) {
        return songSearcher.search(SongSearcher.ScopeType.ALBUM, album);
    }

    @Override
    public Collection<MPDSong> findArtist(MPDArtist artist) {
        return findArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> findArtist(String artist) {
        return songSearcher.find(SongSearcher.ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> searchArtist(MPDArtist artist) {
        return searchArtist(artist.getName());
    }

    @Override
    public Collection<MPDSong> searchArtist(String artist) {
        return songSearcher.search(SongSearcher.ScopeType.ARTIST, artist);
    }

    @Override
    public Collection<MPDSong> findYear(String year) {
        return songSearcher.find(SongSearcher.ScopeType.DATE, year);
    }

    @Override
    public Collection<MPDSong> findTitle(String title) {
        return songSearcher.find(SongSearcher.ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> findAny(String criteria) {
        return songSearcher.find(SongSearcher.ScopeType.ANY, criteria);
    }

    @Override
    public Collection<MPDSong> searchTitle(String title) {
        return songSearcher.search(SongSearcher.ScopeType.TITLE, title);
    }

    @Override
    public Collection<MPDSong> searchAny(String criteria) {
        return songSearcher.search(SongSearcher.ScopeType.ANY, criteria);
    }

    @Override
    public Collection<MPDSong> searchTitle(String title, int startYear, int endYear) {
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
    public Collection<MPDSong> searchFileName(String fileName) {
        return songSearcher.search(SongSearcher.ScopeType.FILENAME, removeSlashes(fileName));
    }

    @Override
    public Collection<MPDSong> findGenre(MPDGenre genre) {
        return findGenre(genre.getName());
    }

    @Override
    public Collection<MPDSong> findGenre(String genre) {
        return songSearcher.find(SongSearcher.ScopeType.GENRE, genre);
    }

    @Override
    public MPDSong findSong(String name, String album, String artist) {
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
