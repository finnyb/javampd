package org.bff.javampd.artist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDArtistDatabaseTest {
  private static final String ARTIST_RESPONSE_PREFIX = "Artist: ";

  @Captor private ArgumentCaptor<TagLister.ListType> captor;

  @Mock private TagLister tagLister;

  private MPDArtistDatabase artistDatabase;

  @BeforeEach
  void before() {
    artistDatabase = new MPDArtistDatabase(tagLister);
  }

  @Test
  void testListAllArtists() {
    MPDArtist testArtist = new MPDArtist("testName");

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.name());

    when(tagLister.list(TagLister.ListType.ARTIST)).thenReturn(mockReturn);

    List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists());
    assertEquals(1, artists.size());
    assertEquals(testArtist, artists.getFirst());
  }

  @Test
  void listAllAlbumArtistsCommand() {
    MPDArtist testArtist = new MPDArtist("Tool");

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.name());

    when(tagLister.list(TagLister.ListType.ALBUM_ARTIST)).thenReturn(mockReturn);

    artistDatabase.listAllAlbumArtists();
    verify(tagLister).list(captor.capture());

    assertEquals(TagLister.ListType.ALBUM_ARTIST, captor.getValue());
  }

  @Test
  void listAllAlbumArtists() {
    MPDArtist testArtist = new MPDArtist("Spiritbox");

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.name());

    when(tagLister.list(TagLister.ListType.ARTIST)).thenReturn(mockReturn);

    List<MPDArtist> artists = new ArrayList<>(artistDatabase.listAllArtists());
    assertEquals(1, artists.size());
    assertEquals(testArtist, artists.getFirst());
  }

  @Test
  void testListArtistsByGenre() {
    MPDGenre testGenre = new MPDGenre("testGenreName");

    String testArtistName = "testArtist";

    List<String> mockGenreList = new ArrayList<>();
    mockGenreList.add(TagLister.ListType.GENRE.getType());
    mockGenreList.add(testGenre.name());

    List<String> mockReturnGenreList = new ArrayList<>();
    mockReturnGenreList.add(testArtistName);

    List<String> mockReturnArtist = new ArrayList<>();
    mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);

    when(tagLister.list(TagLister.ListType.ARTIST, mockGenreList)).thenReturn(mockReturnArtist);

    MPDArtist testArtist = new MPDArtist(testArtistName);

    List<MPDArtist> artists = new ArrayList<>(artistDatabase.listArtistsByGenre(testGenre));
    assertEquals(1, artists.size());
    assertEquals(testArtist, artists.getFirst());
  }

  @Test
  void testListArtistByName() {
    String testArtistName = "testArtist";

    List<String> mockReturnName = new ArrayList<>();
    mockReturnName.add(TagLister.ListType.ARTIST.getType());
    mockReturnName.add(testArtistName);

    List<String> mockReturnArtist = new ArrayList<>();
    mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);

    when(tagLister.list(TagLister.ListType.ARTIST, mockReturnName)).thenReturn(mockReturnArtist);

    MPDArtist testArtist = new MPDArtist(testArtistName);

    List<MPDArtist> artists = new ArrayList<>();
    artists.add(artistDatabase.listArtistByName(testArtistName));

    assertEquals(1, artists.size());
    assertEquals(testArtist, artists.getFirst());
  }

  @Test
  void testListMultipleArtistByName() {
    String testArtistName = "testArtist";
    String testArtistName2 = "testArtist";

    List<String> mockReturnName = new ArrayList<>();
    mockReturnName.add(TagLister.ListType.ARTIST.getType());
    mockReturnName.add(testArtistName);

    List<String> mockReturnArtist = new ArrayList<>();
    mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName);
    mockReturnArtist.add(ARTIST_RESPONSE_PREFIX + testArtistName2);

    when(tagLister.list(TagLister.ListType.ARTIST, mockReturnName)).thenReturn(mockReturnArtist);

    MPDArtist testArtist = new MPDArtist(testArtistName);

    List<MPDArtist> artists = new ArrayList<>();
    artists.add(artistDatabase.listArtistByName(testArtistName));

    assertEquals(1, artists.size());
    assertEquals(testArtist, artists.getFirst());
  }
}
