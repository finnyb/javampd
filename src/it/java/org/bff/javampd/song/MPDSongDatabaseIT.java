package org.bff.javampd.song;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestSongs;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

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

    private static List<MPDSong> databaseList;

    @Before
    public void setUp() throws Exception {
        songDatabase = getMpd().getDatabaseManager().getSongDatabase();
        loadDatabaseSongs();
    }

    private void loadDatabaseSongs() throws MPDDatabaseException {
        if (databaseList == null) {
            databaseList = new ArrayList<>(songDatabase.listAllSongs());
        }
    }

    @Test
    public void testSongCount() throws MPDException {
        assertEquals(TestSongs.getSongs().size(), databaseList.size());
    }

    @Test
    public void testFindAlbum() throws MPDException {
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
    public void testFindAlbumString() throws MPDException {
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
    public void testFindAlbumByArtist() throws Exception {
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
    public void testFindAlbumByArtistString() throws Exception {
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
    public void testFindAlbumByGenre() throws Exception {
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
    public void testFindAlbumByGenreString() throws Exception {
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
    public void testFindAlbumByYear() throws Exception {
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
    public void testSearchAlbum() throws MPDException {
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
    public void testSearchAlbumString() throws MPDException {
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
    public void testFindArtist() throws Exception {
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
    public void testFindArtistString() throws MPDException {
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
    public void testSearchArtist() throws MPDException {
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
    public void testSearchArtistString() throws MPDException {
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
    public void testFindYear() throws MPDException {
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
    public void testFindTitle() throws Exception {
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
    public void testFindAny() throws MPDException {
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
    public void testListAllSongs() throws Exception {
        for (MPDSong testSong : TestSongs.getSongs()) {

            boolean exists = false;
            for (MPDSong song : songDatabase.listAllSongs()) {
                if (song.getFile().equals(testSong.getFile())) {
                    exists = true;
                    compareSongs(testSong, song);
                    break;
                }
            }

            if (!exists) {
                fail("Song " + testSong + " does not exist.");
            }
        }
    }

    @Test
    public void testListAllSongsForPath() throws Exception {

    }

    @Test
    public void testSearchAny() throws MPDException {
        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains(SEARCH_ANY)) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs = new ArrayList<>(songDatabase.searchAny(SEARCH_ANY));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testSearchTitleBetweenYears() throws Exception {

    }

    @Test
    public void testSearchTitle() throws MPDException {
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
    public void testSearchTitleStringSpace() throws MPDException {
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
    public void testSearchFileName() throws Exception {

    }

    @Test
    public void testFindGenre() throws MPDException {
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
    public void testFindGenreString() throws MPDException {
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

    private void compareSongLists(List<MPDSong> testResults, List<MPDSong> foundSongs) {

        if (testResults.isEmpty()) {
            assertTrue("Bad test criteria.  Should have a size of at least 1", false);
        }

        assertEquals(testResults.size(), foundSongs.size());

        for (MPDSong song : testResults) {
            boolean found = false;
            for (MPDSong songDb : foundSongs) {
                if (song.getFile().equals(songDb.getFile())) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);
        }
    }
}