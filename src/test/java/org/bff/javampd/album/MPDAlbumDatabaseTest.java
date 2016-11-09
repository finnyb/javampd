package org.bff.javampd.album;

import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDAlbumDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDAlbumDatabase albumDatabase;

    @Test
    public void testListSingleAlbumsByArtist() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");

        String testAlbumName = "testAlbum";

        List<String> mockList = new ArrayList<>();
        mockList.add(testArtist.getName());
        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockList))
                .thenReturn(mockReturn);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAlbumsByArtist(testArtist));
        assertEquals(1, albums.size());
        assertEquals(testAlbum, albums.get(0));
    }

    @Test
    public void testListMultipleAlbumsByArtist() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");

        String testAlbumName1 = "testAlbum1";
        String testAlbumName2 = "testAlbum2";

        List<String> mockList = new ArrayList<>();
        mockList.add(testArtist.getName());
        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testAlbumName1);
        mockReturn.add(testAlbumName2);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockList))
                .thenReturn(mockReturn);

        MPDAlbum testAlbum1 = new MPDAlbum(testAlbumName1, testArtist.getName());
        MPDAlbum testAlbum2 = new MPDAlbum(testAlbumName2, testArtist.getName());

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAlbumsByArtist(testArtist));
        assertEquals(2, albums.size());
        assertEquals(testAlbum1, albums.get(0));
        assertEquals(testAlbum2, albums.get(1));
    }

    @Test
    public void testListAlbumsByGenre() throws Exception {
        MPDArtist testArtist = new MPDArtist("testArtistName");
        MPDGenre testGenre = new MPDGenre("testGenreName");

        String testAlbumName = "testAlbum";

        List<String> mockGenreList = new ArrayList<>();
        mockGenreList.add(TagLister.ListType.GENRE.getType());
        mockGenreList.add(testGenre.getName());

        List<String> mockReturnGenreList = new ArrayList<>();
        mockReturnGenreList.add(testAlbumName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtist.getName());
        List<String> mockListArtist = new ArrayList<>();
        mockListArtist.add(testArtist.getName());

        List<String> mockAlbumList = new ArrayList<>();
        mockAlbumList.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockGenreList))
                .thenReturn(mockReturnGenreList);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList))
                .thenReturn(mockReturnArtist);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockGenreList))
                .thenReturn(mockReturnArtist);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAlbumsByGenre(testGenre));
        assertEquals(1, albums.size());
        assertEquals(testAlbum, albums.get(0));
    }

    @Test
    public void testListAlbumsByYear() throws Exception {
        MPDArtist testArtist = new MPDArtist("testArtistName");
        String testYear = "testYear";

        String testAlbumName = "testAlbum";

        List<String> mockYearList = new ArrayList<>();
        mockYearList.add(TagLister.ListType.DATE.getType());
        mockYearList.add(testYear);

        List<String> mockReturnAlbumList = new ArrayList<>();
        mockReturnAlbumList.add(testAlbumName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtist.getName());
        List<String> mockListArtist = new ArrayList<>();
        mockListArtist.add(testArtist.getName());

        List<String> mockAlbumList = new ArrayList<>();
        mockAlbumList.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockYearList))
                .thenReturn(mockReturnAlbumList);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList))
                .thenReturn(mockReturnArtist);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockYearList))
                .thenReturn(mockReturnArtist);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAlbumsByYear(testYear));
        assertEquals(1, albums.size());
        assertEquals(testAlbum, albums.get(0));
    }

    @Test
    public void testListAlbumNamesByYear() throws Exception {
        String testYear = "testYear";
        String testAlbumName = "testAlbum";

        List<String> mockYearList = new ArrayList<>();
        mockYearList.add(TagLister.ListType.DATE.getType());
        mockYearList.add(testYear);

        List<String> mockReturnAlbumList = new ArrayList<>();
        mockReturnAlbumList.add(testAlbumName);

        List<String> mockAlbumList = new ArrayList<>();
        mockAlbumList.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockYearList))
                .thenReturn(mockReturnAlbumList);

        List<String> albums = new ArrayList<>(albumDatabase.listAlbumNamesByYear(testYear));
        assertEquals(1, albums.size());
        assertEquals(testAlbumName, albums.get(0));
    }

    @Test
    public void testListAllAlbumNames() throws Exception {
        String testAlbumName1 = "testAlbum1";
        String testAlbumName2 = "testAlbum2";

        List<String> mockAlbumNameList = new ArrayList<>();
        mockAlbumNameList.add(testAlbumName1);
        mockAlbumNameList.add(testAlbumName2);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<String> albums = new ArrayList<>(albumDatabase.listAllAlbumNames());
        assertEquals(2, albums.size());
        assertEquals(testAlbumName1, albums.get(0));
        assertEquals(testAlbumName2, albums.get(1));
    }

    @Test
    public void testListAllAlbumsWindowed() {
        int start = 0;
        int end = 100;

        String albumPrefix = "testAlbum";
        String artistPrefix = "testArtist";

        List<String> mockAlbumNameList = loadMockAlbums(albumPrefix, artistPrefix, end);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(start, end));

        assertEquals(end - start, albums.size());
        for (int i = start; i < end; ++i) {
            MPDAlbum album = new MPDAlbum(albumPrefix + i, artistPrefix + i);
            assertEquals(album, albums.get(i));
        }
    }

    @Test
    public void testListAllAlbums() {
        int end = 100;

        String albumPrefix = "testAlbum";
        String artistPrefix = "testArtist";

        List<String> mockAlbumNameList = loadMockAlbums(albumPrefix, artistPrefix, end);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums());

        assertEquals(end, albums.size());
        for (int i = 0; i < end; ++i) {
            MPDAlbum album = new MPDAlbum(albumPrefix + i, artistPrefix + i);
            assertEquals(album, albums.get(i));
        }
    }

    @Test
    public void testListAllAlbumsBelowMinimum() {
        int start = 0;
        int end = 50;
        int requested = 100;

        String albumPrefix = "testAlbum";
        String artistPrefix = "testArtist";

        List<String> mockAlbumNameList = loadMockAlbums(albumPrefix, artistPrefix, end);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(start, requested));

        assertEquals(end - start, albums.size());
        for (int i = start; i < end; ++i) {
            MPDAlbum album = new MPDAlbum(albumPrefix + i, artistPrefix + i);
            assertEquals(album, albums.get(i));
        }
    }

    @Test
    public void testListAllAlbumsEmpty() {
        int start = 0;
        int end = 50;
        String albumPrefix = "testAlbum";
        String artistPrefix = "testArtist";

        List<String> mockAlbumNameList = loadMockAlbums(albumPrefix, artistPrefix, end);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(start, start));

        assertEquals(0, albums.size());
    }

    @Test
    public void testListAllAlbumsStartBelowEnd() {
        int end = 50;
        String albumPrefix = "testAlbum";
        String artistPrefix = "testArtist";

        List<String> mockAlbumNameList = loadMockAlbums(albumPrefix, artistPrefix, end);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(end + 1, end));

        assertEquals(0, albums.size());
    }

    private List<String> loadMockAlbums(String albumPrefix, String artistPrefix, int count) {
        List<String> mockAlbumNameList = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            mockAlbumNameList.add(albumPrefix + i);
            List<String> tagParams = new ArrayList<>();
            tagParams.add("album");
            tagParams.add(albumPrefix + i);
            List<String> mockArtists = new ArrayList<>();
            mockArtists.add(artistPrefix + i);
            when(tagLister
                    .list(TagLister.ListType.ARTIST, tagParams))
                    .thenReturn(mockArtists);
        }

        return mockAlbumNameList;
    }

    @Test
    public void testFindAlbumByName() throws Exception {
        MPDArtist testArtist = new MPDArtist("testArtistName");

        String testAlbumName = "testAlbum1";

        List<String> mockAlbumList = new ArrayList<>();
        mockAlbumList.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList.add(testAlbumName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtist.getName());

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumList);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList))
                .thenReturn(mockReturnArtist);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.findAlbum(testAlbumName));
        assertEquals(1, albums.size());
        assertEquals(testAlbum, albums.get(0));
    }


    @Test
    public void testFindAlbumByArtist() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");
        String testAlbumName = "testAlbum";

        List<String> mockList = new ArrayList<>();
        mockList.add(testArtist.getName());
        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockList))
                .thenReturn(mockReturn);

        MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

        MPDAlbum album = albumDatabase.findAlbumByArtist(testArtist, testAlbumName);

        assertEquals(album, testAlbum);
    }

    @Test
    public void testFindAlbumByArtistNoMatch() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");
        String testAlbumName = "testAlbum";
        String searchName = "testAlbum1";

        List<String> mockList = new ArrayList<>();
        mockList.add(testArtist.getName());
        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testAlbumName);

        when(tagLister
                .list(TagLister.ListType.ALBUM, mockList))
                .thenReturn(mockReturn);

        MPDAlbum album = albumDatabase.findAlbumByArtist(testArtist, searchName);

        assertNull(album);
    }
}