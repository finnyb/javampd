package org.bff.javampd.integrationdata;

import org.bff.javampd.TestProperties;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.song.MPDSong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

/**
 * Song ids must be filled since not reliable
 *
 * @author bill
 */
public class TestSongs {
    public static final String EXTENSION = ".mp3";

    private static final int INDEX_ARTIST = 0;
    private static final int INDEX_ALBUM = 1;
    private static final int INDEX_TRACK = 2;
    private static final int INDEX_TITLE = 3;
    private static final int INDEX_YEAR = 4;
    private static final int INDEX_GENRE = 5;
    private static final int INDEX_COMMENT = 6;
    private static final int INDEX_DISC = 7;
    private static final String NULL_TITLE = "";
    private static final String NULL_DISC = "";
    private static final String NULL_GENRE = "";
    private static final String NULL_COMMENT = "";
    private static final String NULL_TRACK = "0";

    private static final Collection<MPDSong> songs = new ArrayList<>();
    private static final HashMap<MPDArtist, Collection<MPDSong>> SONG_ARTIST_MAP =
            new HashMap<>();

    static void loadSong(File file, File f) throws IOException {
        String[] s = file.getName().replace(EXTENSION, "").split("-");
        String title = "null".equalsIgnoreCase(s[INDEX_TITLE]) ? NULL_TITLE : s[INDEX_TITLE];

        MPDSong song = new MPDSong(file.getName(), title);
        song.setFile(file.getPath().replace(TestProperties.getInstance().getPath() + "/", ""));
        song.setComment("null".equalsIgnoreCase(s[INDEX_COMMENT]) ? NULL_COMMENT : s[INDEX_COMMENT]);
        song.setTrack(Integer.parseInt("null".equalsIgnoreCase(s[INDEX_TRACK]) ? NULL_TRACK : s[INDEX_TRACK]));
        song.setDiscNumber("null".equalsIgnoreCase(s[INDEX_DISC]) ? NULL_DISC : s[INDEX_DISC]);

        MPDArtist artist = processArtist(song, "null".equalsIgnoreCase(s[INDEX_ARTIST]) ? TestArtists.NULL_ARTIST : s[INDEX_ARTIST]);
        MPDGenre genre = processGenre(song, s[INDEX_GENRE]);
        String year = "null".equalsIgnoreCase(s[INDEX_YEAR]) ? TestYears.NULL_YEAR : s[INDEX_YEAR];

        song.setYear(year);
        TestYears.addYear(year);

        MPDAlbum album = processAlbum(song, s[INDEX_ALBUM], genre.getName());
        TestYears.addAlbum(year, album);

        if (artist != null) {
            TestArtists.addAlbum(artist, album);
            TestGenres.addArtist(genre, artist);
        }

        TestGenres.addAlbum(genre, album);

        songs.add(song);

        SONG_ARTIST_MAP.computeIfAbsent(artist, k -> new ArrayList<>());

        if (!SONG_ARTIST_MAP.get(artist).contains(song)) {
            SONG_ARTIST_MAP.get(artist).add(song);
        }
    }

    private static MPDAlbum processAlbum(MPDSong song, String albumName, String genre) {
        if ("null".equalsIgnoreCase(albumName)) {
            albumName = "";
        } else {
            albumName = albumName.replace("[colon]", ":");
        }
        song.setAlbumName(albumName != null ? albumName : TestAlbums.NULL_ALBUM);
        return TestAlbums.addAlbum(albumName, song.getArtistName(), song.getYear(), genre);
    }

    private static MPDArtist processArtist(MPDSong song, String artistName) {
        song.setArtistName(artistName != null ? artistName : TestArtists.NULL_ARTIST);
        return TestArtists.addArtist(artistName);
    }

    private static MPDGenre processGenre(MPDSong song, String genreName) {
        song.setGenre("null".equalsIgnoreCase(genreName) ? NULL_GENRE : genreName);
        return TestGenres.addGenre(song.getGenre());
    }

    public static Collection<MPDSong> getSongs() {
        return songs;
    }
}