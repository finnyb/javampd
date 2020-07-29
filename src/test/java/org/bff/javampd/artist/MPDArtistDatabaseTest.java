package org.bff.javampd.artist;

import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MPDArtistDatabaseTest {
    private static final String ARTIST_RESPONSE_PREFIX = "Artist: ";

    @Mock
    private TagLister tagLister;

    private MPDArtistDatabase artistDatabase;

    @BeforeEach
    public void before() {
        artistDatabase = new MPDArtistDatabase(tagLister);
    }

    @Test
    public void testListAllArtists() {
        MPDArtist testArtist = new MPDArtist("testName");

        List<String> mockReturn = new ArrayList<>();
        mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

        when(tagLister
                .list(TagLister.ListType.ARTIST))
                .thenReturn(mockReturn);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists());
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListArtistsByGenre() {
        MPDGenre testGenre = new MPDGenre("testGenreName");

        String testArtistName = "testArtist";

        List<String> mockGenreList = new ArrayList<>();
        mockGenreList.add(TagLister.ListType.GENRE.getType());
        mockGenreList.add(testGenre.getName());

        List<String> mockReturnGenreList = new ArrayList<>();
        mockReturnGenreList.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockGenreList))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>(artistDatabase.listArtistsByGenre(testGenre));
        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListArtistByName() {
        String testArtistName = "testArtist";

        List<String> mockReturnName = new ArrayList<>();
        mockReturnName.add(TagLister.ListType.ARTIST.getType());
        mockReturnName.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);

        when(tagLister
                .list(TagLister.ListType.ARTIST, mockReturnName))
                .thenReturn(mockReturnArtist);

        MPDArtist testArtist = new MPDArtist(testArtistName);

        List<MPDArtist> artists = new ArrayList<>();
        artists.add(artistDatabase.listArtistByName(testArtistName));

        assertEquals(1, artists.size());
        assertEquals(testArtist, artists.get(0));
    }

    @Test
    public void testListMultipleArtistByName() {
        String testArtistName = "testArtist";
        String testArtistName2 = "testArtist";

        List<String> mockReturnName = new ArrayList<>();
        mockReturnName.add(TagLister.ListType.ARTIST.getType());
        mockReturnName.add(testArtistName);

        List<String> mockReturnArtist = new ArrayList<>();
        mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);
        mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName2);

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