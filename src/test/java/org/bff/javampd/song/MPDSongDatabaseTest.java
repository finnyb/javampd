package org.bff.javampd.song;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDSongDatabaseTest {
    private SongDatabase songDatabase;
    private SongSearcher mockedSongSearcher;

    @BeforeEach
    void setup() {
        mockedSongSearcher = mock(SongSearcher.class);
        songDatabase = new MPDSongDatabase(mockedSongSearcher);
    }

    @Test
    void testFindAlbum() {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAlbum(testAlbum));
    }

    @Test
    void testFindAlbumByName() {
        String testAlbumName = "testAlbumName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAlbum(testAlbumName));
    }

    @Test
    void testSearchAlbum() {
        String testAlbumName = "testAlbumName";
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtistName);

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAlbum(testAlbum));
    }

    @Test
    void testSearchAlbumByName() {
        String testAlbumName = "testAlbumName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ALBUM, testAlbumName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAlbum(testAlbumName));
    }

    @Test
    void testFindArtist() {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDArtist testArtist = new MPDArtist(testArtistName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findArtist(testArtist));
    }

    @Test
    void testFindArtistByName() {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findArtist(testArtistName));
    }

    @Test
    void testSearchArtist() {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        MPDArtist testArtist = new MPDArtist(testArtistName);

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchArtist(testArtist));
    }

    @Test
    void testSearchArtistByName() {
        String testArtistName = "testArtistName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ARTIST, testArtistName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchArtist(testArtistName));
    }

    @Test
    void testFindAlbumByArtist() {
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
    void testFindAlbumByArtistByName() {
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
    void testFindAlbumByGenre() {
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
    void testFindAlbumByYear() {
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
    void testFindYear() {
        String testYear = "1990";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.DATE, testYear))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findYear(testYear));
    }

    @Test
    void testFindTitle() {
        String testTitle = "testTitle";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findTitle(testTitle));
    }

    @Test
    void testFindSongByAlbumAndArtist() {
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
    void testFindSongByAlbumAndArtistNotFound() {
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
    void testFindAny() {
        String testAny = "testAny";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.ANY, testAny))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findAny(testAny));
    }

    @Test
    void testSearchTitle() {
        String testTitle = "testTitle";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.TITLE, testTitle))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchTitle(testTitle));
    }

    @Test
    void testSearchAny() {
        String testAny = "testAny";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.ANY, testAny))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchAny(testAny));
    }

    @Test
    void testSearchFileName() {
        String testFileName = "testFileName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.search(SongSearcher.ScopeType.FILENAME, testFileName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.searchFileName(testFileName));
    }

    @Test
    void testFindGenre() {
        String testGenreName = "testGenreName";
        List<MPDSong> testSongs = generateSongs();

        MPDGenre testGenre = new MPDGenre(testGenreName);

        when(mockedSongSearcher.find(SongSearcher.ScopeType.GENRE, testGenreName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findGenre(testGenre));
    }

    @Test
    void testFindGenreByName() {
        String testGenreName = "testGenreName";
        List<MPDSong> testSongs = generateSongs();

        when(mockedSongSearcher.find(SongSearcher.ScopeType.GENRE, testGenreName))
                .thenReturn(testSongs);

        testSongs(testSongs, songDatabase.findGenre(testGenreName));
    }

    @Test
    void testSearchTitleByYear() {
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
    void testSearchTitleByYearNullYear() {
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
    void testSearchTitleByYearFullYear() {
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
    void testSearchTitleByYearBadYear() {
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
