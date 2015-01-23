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
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDAlbumDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDAlbumDatabase albumDatabase;

    @Test
    public void testListAllAlbums() {
        MPDArtist testArtist1 = new MPDArtist("testName1");
        MPDArtist testArtist2 = new MPDArtist("testName2");

        List<String> mockListArtist1 = new ArrayList<>();
        mockListArtist1.add(testArtist1.getName());
        mockListArtist1.add(testArtist2.getName());

        List<String> mockListArtist2 = new ArrayList<>();
        mockListArtist2.add(testArtist1.getName());
        mockListArtist2.add(testArtist2.getName());

        String testAlbumName1 = "testAlbum1";
        String testAlbumName2 = "testAlbum2";

        MPDAlbum testAlbum1 = new MPDAlbum(testAlbumName1, testArtist1.getName());
        MPDAlbum testAlbum2 = new MPDAlbum(testAlbumName2, testArtist1.getName());
        MPDAlbum testAlbum3 = new MPDAlbum(testAlbumName1, testArtist2.getName());
        MPDAlbum testAlbum4 = new MPDAlbum(testAlbumName2, testArtist2.getName());

        List<MPDAlbum> allAlbums = new ArrayList<>();
        allAlbums.add(testAlbum1);
        allAlbums.add(testAlbum2);
        allAlbums.add(testAlbum3);
        allAlbums.add(testAlbum4);

        List<String> mockAlbumList1 = new ArrayList<>();
        mockAlbumList1.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList1.add(testAlbumName1);

        List<String> mockAlbumList2 = new ArrayList<>();
        mockAlbumList2.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList2.add(testAlbumName2);


        List<String> mockAlbumNameList = new ArrayList<>();
        mockAlbumNameList.add(testAlbumName1);
        mockAlbumNameList.add(testAlbumName2);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList1))
                .thenReturn(mockListArtist1);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList2))
                .thenReturn(mockListArtist2);

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums());

        assertEquals(mockAlbumList1.size() + mockAlbumList2.size(), albums.size());
        assertTrue(albums.containsAll(allAlbums));
    }

    @Test
    public void testFindAlbumByArtist() {
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

        MPDAlbum album = albumDatabase.findAlbumByArtist(testArtist, testAlbumName1);

        assertEquals(testAlbum1, album);
    }

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

    @Test(expected = IllegalArgumentException.class)
    public void testListAllAlbumsWindowedInvalidIndexes() {
        albumDatabase.listAllAlbums(5, 3);
    }

    @Test
    public void testListAllAlbumsWindowed() {
        int start = 2;
        int end = 3;

        MPDArtist testArtist1 = new MPDArtist("testName1");

        List<String> mockListArtist1 = new ArrayList<>();
        mockListArtist1.add(testArtist1.getName());

        String testAlbumName1 = "testAlbum1";
        String testAlbumName2 = "testAlbum2";
        String testAlbumName3 = "testAlbum3";
        String testAlbumName4 = "testAlbum4";

        MPDAlbum testAlbum1 = new MPDAlbum(testAlbumName1, testArtist1.getName());
        MPDAlbum testAlbum2 = new MPDAlbum(testAlbumName2, testArtist1.getName());
        MPDAlbum testAlbum3 = new MPDAlbum(testAlbumName3, testArtist1.getName());
        MPDAlbum testAlbum4 = new MPDAlbum(testAlbumName4, testArtist1.getName());

        List<MPDAlbum> allAlbums = new ArrayList<>();
        allAlbums.add(testAlbum1);
        allAlbums.add(testAlbum2);
        allAlbums.add(testAlbum3);
        allAlbums.add(testAlbum4);

        List<String> mockAlbumList1 = new ArrayList<>();
        mockAlbumList1.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList1.add(testAlbumName1);

        List<String> mockAlbumList2 = new ArrayList<>();
        mockAlbumList2.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList2.add(testAlbumName2);

        List<String> mockAlbumList3 = new ArrayList<>();
        mockAlbumList3.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList3.add(testAlbumName3);

        List<String> mockAlbumList4 = new ArrayList<>();
        mockAlbumList4.add(TagLister.ListType.ALBUM.getType());
        mockAlbumList4.add(testAlbumName4);

        List<String> mockAlbumNameList = new ArrayList<>();
        mockAlbumNameList.add(testAlbumName1);
        mockAlbumNameList.add(testAlbumName2);
        mockAlbumNameList.add(testAlbumName3);
        mockAlbumNameList.add(testAlbumName4);

        when(tagLister
                .list(TagLister.ListType.ALBUM))
                .thenReturn(mockAlbumNameList);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList1))
                .thenReturn(mockListArtist1);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList2))
                .thenReturn(mockListArtist1);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList3))
                .thenReturn(mockListArtist1);
        when(tagLister
                .list(TagLister.ListType.ARTIST, mockAlbumList4))
                .thenReturn(mockListArtist1);


        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(start, end));

        assertEquals(end - start, albums.size());
        assertTrue(albums.contains(testAlbum3));
    }
}