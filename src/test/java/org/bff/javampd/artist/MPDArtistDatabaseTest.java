package org.bff.javampd.artist;

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
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDArtistDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDArtistDatabase artistDatabase;

    @Test
    public void testListAllArtists() throws Exception {
        MPDArtist testArtist = new MPDArtist("testName");

        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testArtist.getName());

        when(tagLister
                .list(TagLister.ListType.ARTIST))
                .thenReturn(mockReturn);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists());
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListAllArtistsWindowed() throws Exception {
        int start = 1;
        int end = 3;

        MPDArtist testArtist1 = new MPDArtist("testName1");
        MPDArtist testArtist2 = new MPDArtist("testName2");
        MPDArtist testArtist3 = new MPDArtist("testName3");
        MPDArtist testArtist4 = new MPDArtist("testName4");
        MPDArtist testArtist5 = new MPDArtist("testName5");

        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testArtist1.getName());
        mockReturn.add(testArtist2.getName());
        mockReturn.add(testArtist3.getName());
        mockReturn.add(testArtist4.getName());
        mockReturn.add(testArtist5.getName());

        when(tagLister
                .list(TagLister.ListType.ARTIST))
                .thenReturn(mockReturn);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists(start, end));
        assertEquals(end - start, artists.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListAllArtistsWindowedIllegalArgument() throws Exception {
        int start = 5;
        int end = 4;

        artistDatabase.listAllArtists(start, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testListAllArtistsWindowedIllegalArgumentSize() throws Exception {
        int start = 8;
        int end = 10;

        MPDArtist testArtist1 = new MPDArtist("testName1");
        MPDArtist testArtist2 = new MPDArtist("testName2");
        MPDArtist testArtist3 = new MPDArtist("testName3");
        MPDArtist testArtist4 = new MPDArtist("testName4");
        MPDArtist testArtist5 = new MPDArtist("testName5");

        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(testArtist1.getName());
        mockReturn.add(testArtist2.getName());
        mockReturn.add(testArtist3.getName());
        mockReturn.add(testArtist4.getName());
        mockReturn.add(testArtist5.getName());

        when(tagLister
                .list(TagLister.ListType.ARTIST))
                .thenReturn(mockReturn);

        artistDatabase.listAllArtists(start, end);
    }

    @Test
    public void testListArtistsByGenre() throws Exception {
        MPDGenre testGenre = new MPDGenre("testGenreName");

        String testArtistName = "testArtist";

        List<String> mockGenreList = new ArrayList<>();
        mockGenreList.add(TagLister.ListType.GENRE.getType());
        mockGenreList.add(testGenre.getName());

        List<String> mockReturnGenreList = new ArrayList<>();
        mockReturnGenreList.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockGenreList))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listArtistsByGenre(testGenre));
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListArtistByName() throws Exception {
        String testArtistName = "testArtist";

        List<String> mockReturnName = new ArrayList<>();
        mockReturnName.add(TagLister.ListType.ARTIST.getType());
        mockReturnName.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockReturnName))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>();
        artists.add(artistDatabase.listArtistByName(testArtistName));
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }
}