package org.bff.javampd.song;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDSongDatabaseTest {
    private SongDatabase songDatabase;
    private SongSearcher mockedSongSearcher;

    @Before
    public void setup() {
        mockedSongSearcher = mock(SongSearcher.class);
        songDatabase = new MPDSongDatabase(mockedSongSearcher);
    }

    @Test
    public void testFindAlbum() throws Exception {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAlbum(testAlbum));
    }

    @Test
    public void testFindAlbumByName() throws Exception {
        String testAlbumName = "testAlbumName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAlbum(testAlbumName));
    }

    @Test
    public void testSearchAlbum() throws Exception {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtistName);

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAlbum(testAlbum));
    }

    @Test
    public void testSearchAlbumByName() throws Exception {
        String testAlbumName = "testAlbumName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAlbum(testAlbumName));
    }

    @Test
    public void testFindArtist() throws Exception {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDArtist testArtist = new MPDArtist(testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findArtist(testArtist));
    }

    @Test
    public void testFindArtistByName() throws Exception {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findArtist(testArtistName));
    }

    @Test
    public void testSearchArtist() throws Exception {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDArtist testArtist = new MPDArtist(testArtistName);

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchArtist(testArtist));
    }

    @Test
    public void testSearchArtistByName() throws Exception {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchArtist(testArtistName));
    }

    @Test
    public void testFindAlbumByArtist() throws Exception {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setArtistName(testArtistName);
        testSongs.get(2).setArtistName(testArtistName);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtistName);
        MPDArtist testArtist = new MPDArtist(testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.findAlbumByArtist(testArtist, testAlbum));
    }

    @Test
    public void testFindAlbumByArtistByName() throws Exception {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";

        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setArtistName(testArtistName);
        testSongs.get(2).setArtistName(testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.findAlbumByArtist(testArtistName, testAlbumName));
    }

    @Test
    public void testFindAlbumByGenre() throws Exception {
        String testAlbumName = "testAlbumName";
        String testGenreName = "testGenre";

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, "testArtist");
        MPDGenre testGenre = new MPDGenre(testGenreName);

        List<MPDSong> testSongs = generateSongs();
        testSongs.get(0).setGenre(testGenreName);
        testSongs.get(2).setGenre(testGenreName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.findAlbumByGenre(testGenre, testAlbum));
    }

    @Test
    public void testFindAlbumByYear() throws Exception {
        String testAlbumName = "testAlbumName";
        String testYear = "1990";
        List<MPDSong> testSongs = generateSongs();
        testSongs.get(0).setYear(testYear);
        testSongs.get(2).setYear(testYear);
        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, "testArtist");

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.findAlbumByYear(testYear, testAlbum));
    }

    @Test
    public void testFindYear() throws Exception {
        String testYear = "1990";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.DATE, testYear))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findYear(testYear));
    }

    @Test
    public void testFindTitle() throws Exception {
        String testTitle = "testTitle";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findTitle(testTitle));
    }

    @Test
    public void testFindSongByAlbumAndArtist() throws Exception {
        String testTitle = "testTitle";
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(1).setAlbumName(testAlbumName);
        testSongs.get(1).setArtistName(testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        MPDSong song = songDatabase.findSong(testTitle, testAlbumName, testArtistName);

        assertEquals(testSongs.get(1), song);
        assertEquals(testSongs.get(1).getAlbumName(), song.getAlbumName());
        assertEquals(testSongs.get(1).getArtistName(), song.getArtistName());
    }

    @Test
    public void testFindSongByAlbumAndArtistNotFound() throws Exception {
        String testTitle = "testTitle";
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(1).setAlbumName(testAlbumName);
        testSongs.get(1).setArtistName(testArtistName + "bogus");

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        assertNull(songDatabase.findSong(testTitle, testAlbumName, testArtistName));
    }

    @Test
    public void testFindAny() throws Exception {
        String testAny = "testAny";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ANY, testAny))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAny(testAny));
    }

    @Test
    public void testSearchTitle() throws Exception {
        String testTitle = "testTitle";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchTitle(testTitle));
    }

    @Test
    public void testSearchAny() throws Exception {
        String testAny = "testAny";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ANY, testAny))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAny(testAny));
    }

    @Test
    public void testSearchFileName() throws Exception {
        String testFileName = "testFileName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.FILENAME, testFileName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchFileName(testFileName));
    }

    @Test
    public void testFindGenre() throws Exception {
        String testGenreName = "testGenreName";
        List<MPDSong> testSongs = generateSongs();

        MPDGenre testGenre = new MPDGenre(testGenreName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.GENRE, testGenreName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findGenre(testGenre));
    }

    @Test
    public void testFindGenreByName() throws Exception {
        String testGenreName = "testGenreName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.GENRE, testGenreName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findGenre(testGenreName));
    }

    @Test
    public void testSearchTitleByYear() throws Exception {
        String testTitle = "testTitle";
        int testStartYear = 1990;
        int testEndYear = 1992;
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setYear("1990");
        testSongs.get(1).setYear("1995");
        testSongs.get(2).setYear("1991");

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.searchTitle(testTitle, testStartYear, testEndYear));
    }

    @Test
    public void testSearchTitleByYearNullYear() throws Exception {
        String testTitle = "testTitle";
        int testStartYear = 1990;
        int testEndYear = 1992;
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setYear("1990");
        testSongs.get(1).setYear(null);
        testSongs.get(2).setYear("1991");

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.searchTitle(testTitle, testStartYear, testEndYear));
    }

    @Test
    public void testSearchTitleByYearFullYear() throws Exception {
        String testTitle = "testTitle";
        int testStartYear = 1990;
        int testEndYear = 1992;
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setYear("1990");
        testSongs.get(1).setYear("1995-12-5");
        testSongs.get(2).setYear("1991");

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.searchTitle(testTitle, testStartYear, testEndYear));
    }

    @Test
    public void testSearchTitleByYearBadYear() throws Exception {
        String testTitle = "testTitle";
        int testStartYear = 1990;
        int testEndYear = 1992;
        List<MPDSong> testSongs = generateSongs();

        testSongs.get(0).setYear("1990");
        testSongs.get(1).setYear("junk-12-5");
        testSongs.get(2).setYear("1991");

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongsWithoutIndex1(testSongs, songDatabase.searchTitle(testTitle, testStartYear, testEndYear));
    }

    private void testSongsWithoutIndex1(List<MPDSong> testSongs, Collection<MPDSong> songList) {
        testSongs.remove(1);
        testSongs(testSongs, songList);
    }

    private void testSongs(List<MPDSong> testSongs, Collection<MPDSong> songList) {
        List<MPDSong> songs = new ArrayList<>(songList);
        assertEquals(testSongs.size(), songs.size());
        for (int i = 0; i < testSongs.size(); ++i) {
            assertEquals(testSongs.get(i), songs.get(i));
            assertEquals(testSongs.get(i).getAlbumName(), songs.get(i).getAlbumName());
            assertEquals(testSongs.get(i).getArtistName(), songs.get(i).getArtistName());
            assertEquals(testSongs.get(i).getYear(), songs.get(i).getYear());
            assertEquals(testSongs.get(i).getGenre(), songs.get(i).getGenre());
        }
    }

    private List<MPDSong> generateSongs() {
        MPDSong mpdSong1 = new MPDSong("file1", "title1");
        MPDSong mpdSong2 = new MPDSong("file2", "title2");
        MPDSong mpdSong3 = new MPDSong("file3", "title3");

        List<MPDSong> songs = new ArrayList<>();
        songs.add(mpdSong1);
        songs.add(mpdSong2);
        songs.add(mpdSong3);

        return songs;
    }
}