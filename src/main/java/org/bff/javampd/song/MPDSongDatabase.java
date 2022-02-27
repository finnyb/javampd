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
import java.util.Optional;
import java.util.stream.Collectors;

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

    private final SongSearcher songSearcher;

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

        return songList.stream()
                .filter(song -> song.getArtistName() != null && song.getArtistName().equals(artistName))
                .collect(Collectors.toList());

    }

    @Override
    public Collection<MPDSong> findAlbumByGenre(MPDGenre genre, MPDAlbum album) {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        return songList.stream()
                .filter(song -> song.getGenre() != null && song.getGenre().equals(genre.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MPDSong> findAlbumByYear(String year, MPDAlbum album) {
        List<MPDSong> songList = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album.getName()));

        return songList.stream()
                .filter(song -> song.getDate() != null && song.getDate().equals(year))
                .collect(Collectors.toList());
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
            if (song.getDate() == null) {
                continue;
            }

            try {
                if (song.getDate().contains("-")) {
                    year = Integer.parseInt(song.getDate().split("-")[0]);
                } else {
                    year = Integer.parseInt(song.getDate());
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
    public Optional<MPDSong> findSong(String name, String album, String artist) {
        List<MPDSong> songs = new ArrayList<>(songSearcher.find(SongSearcher.ScopeType.ALBUM, album));

        for (MPDSong song : songs) {
            if (artist.equals(song.getArtistName())) {
                return Optional.of(song);
            }
        }
        LOGGER.info("Song not found title --> {}, artist --> {}, album --> {}", name, artist, album);
        return Optional.empty();
    }

    /**
     * Removes leading or trailing slashes from a string.
     *
     * @param path the string to remove leading or trailing slashes
     * @return the string without leading or trailing slashes
     */
    private static String removeSlashes(String path) {
        var retString = path;
        var slash = System.getProperty("file.separator");

        //if non-Unix slashes replace
        retString = retString.replace(slash, "/");

        retString = retString.replaceFirst("^/", "");
        retString = retString.replaceFirst("/$", "");

        return retString;
    }
}
