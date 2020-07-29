package org.bff.javampd.song;

import org.bff.javampd.BaseTest;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestSongs;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MPDSongDatabaseIT extends BaseTest {
    private static final String FIND_ARTIST = "Artist1";
    private static final String SEARCH_ANY = "2";
    private static final String SEARCH_ARTIST = "Artist";
    private static final String SEARCH_ALBUM = "Album";
    private static final String SEARCH_TITLE = "Title";
    private static final String SEARCH_TITLE_SPACE = "Title 1";
    private static final String FIND_GENRE = "Rock";
    private static final String FIND_ALBUM = "Album1";
    private static final String FIND_TITLE = "Title0";
    private static final String FIND_ANY = "Artist0";
    private static final String FIND_YEAR = "1990";

    private SongDatabase songDatabase;
    private ArtistDatabase artistDatabase;

    private static List<MPDSong> databaseList;

    @BeforeEach
    public void setUp() {
        songDatabase = getMpd().getMusicDatabase().getSongDatabase();
        artistDatabase = getMpd().getMusicDatabase().getArtistDatabase();
        loadDatabaseSongs();
    }

    private void loadDatabaseSongs() {
        if (databaseList == null) {
            databaseList = new ArrayList<>();
            for (MPDArtist artist : artistDatabase.listAllArtists()) {
                databaseList.addAll(songDatabase.findArtist(artist));
            }
        }
    }

    @Test
    public void testSongCount() {
        assertEquals(TestSongs.getSongs().size(), databaseList.size());
    }

    @Test
    public void testFindAlbum() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + FIND_ALBUM + "-")) {
                testResults.add(song);
            }
        }

        MPDAlbum album = new MPDAlbum(FIND_ALBUM, FIND_ARTIST);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findAlbum(album));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_ALBUM + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findAlbum(FIND_ALBUM));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumByArtist() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + FIND_ALBUM + "-") &&
                    song.getFile().contains(FIND_ARTIST + "-")) {
                testResults.add(song);
            }
        }

        MPDAlbum album = new MPDAlbum(FIND_ALBUM, FIND_ARTIST);
        MPDArtist artist = new MPDArtist(FIND_ARTIST);

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findAlbumByArtist(artist, album));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumByArtistString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + FIND_ALBUM + "-") &&
                    song.getFile().contains(FIND_ARTIST + "-")) {
                testResults.add(song);
            }
        }

        MPDAlbum album = new MPDAlbum(FIND_ALBUM, FIND_ARTIST);
        MPDArtist artist = new MPDArtist(FIND_ARTIST);

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findAlbumByArtist(artist, album));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumByGenre() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_GENRE + "-")) {
                testResults.add(song);
            }
        }

        MPDGenre genre = new MPDGenre(FIND_GENRE);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findGenre(genre));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumByGenreString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_GENRE + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findGenre(FIND_GENRE));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAlbumByYear() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_YEAR + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findYear(FIND_YEAR));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchAlbum() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_ALBUM)) {
                testResults.add(song);
            }
        }

        MPDAlbum album = new MPDAlbum(SEARCH_ALBUM, SEARCH_ARTIST);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchAlbum(album));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchAlbumString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_ALBUM)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchAlbum(SEARCH_ALBUM));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindArtist() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_ARTIST + "-")) {
                testResults.add(song);
            }
        }

        MPDArtist artist = new MPDArtist(FIND_ARTIST);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findArtist(artist));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindArtistString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_ARTIST + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findArtist(FIND_ARTIST));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchArtist() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_ARTIST)) {
                testResults.add(song);
            }
        }

        MPDArtist artist = new MPDArtist(SEARCH_ARTIST);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchArtist(artist));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchArtistString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_ARTIST)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchArtist(SEARCH_ARTIST));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindYear() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + FIND_YEAR + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findYear(FIND_YEAR));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindTitle() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_TITLE + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findTitle(FIND_TITLE));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindAny() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_ANY + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findAny(FIND_ANY));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testListAllSongsForPath() {

    }

    @Test
    public void testSearchAny() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getArtistName().contains(SEARCH_ANY) ||
                    song.getAlbumName().contains(SEARCH_ANY) ||
                    song.getTitle().contains(SEARCH_ANY) ||
                    song.getGenre().contains(SEARCH_ANY) ||
                    song.getName().contains(SEARCH_ANY) ||
                    song.getYear().contains(SEARCH_ANY) ||
                    song.getDiscNumber().contains(SEARCH_ANY) ||
                    Integer.toString(song.getTrack()).contains(SEARCH_ANY)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchAny(SEARCH_ANY));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchTitleBetweenYears() {

    }

    @Test
    public void testSearchTitle() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_TITLE)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchTitle(SEARCH_TITLE));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchTitleStringSpace() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_TITLE_SPACE)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchTitle(SEARCH_TITLE_SPACE));

        assertEquals(testResults.size(), foundSongs.size());
    }

    @Test
    public void testSearchFileName() {

    }

    @Test
    public void testFindGenre() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + FIND_GENRE + "-")) {
                testResults.add(song);
            }
        }

        MPDGenre genre = new MPDGenre(FIND_GENRE);
        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findGenre(genre));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFindSong() {
        String name = "Title0";
        String artist = "Artist0";
        String album = "Album0";

        MPDSong song = songDatabase.findSong(name, album, artist);

        assertNotNull(song);
        assertEquals(name, song.getName());
        assertEquals(name, song.getTitle());
        assertEquals(artist, song.getArtistName());
        assertEquals(album, song.getAlbumName());
    }

    @Test
    public void testFindGenreString() {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(FIND_GENRE + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.findGenre(FIND_GENRE));

        compareSongLists(testResults, foundSongs);
    }

    private void compareSongs(MPDSong item1, MPDSong item2) {
        assertEquals(item1.getId(), item2.getId());
        assertEquals(item1.getName(), item2.getName());
        assertEquals(item1.getFile(), item2.getFile());
        assertEquals(item1.getAlbumName(), item2.getAlbumName());
        assertEquals(item1.getGenre(), item2.getGenre());
        assertEquals(item1.getGenre(), item2.getGenre());
        assertEquals(item1.getYear(), item2.getYear());
        assertEquals(item1.getTrack(), item2.getTrack());
        assertEquals(item1.getDiscNumber(), item2.getDiscNumber());
    }
}