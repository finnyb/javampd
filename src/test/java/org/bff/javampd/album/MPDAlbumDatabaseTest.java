package org.bff.javampd.album;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.processor.AlbumTagProcessor;
import org.bff.javampd.processor.ArtistTagProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDAlbumDatabaseTest {
  private static final String ARTIST_RESPONSE_PREFIX = "Artist: ";
  private static final String ALBUM_RESPONSE_PREFIX = "Album: ";
  private static final String DATE_RESPONSE_PREFIX = "Date: ";
  private static final String GENRE_RESPONSE_PREFIX = "Genre: ";

  private static final TagLister.GroupType[] ALBUM_GROUPS = {
    TagLister.GroupType.ARTIST,
    TagLister.GroupType.DATE,
    TagLister.GroupType.GENRE,
  };

  @Mock
  private TagLister tagLister;

  private MPDAlbumDatabase albumDatabase;

  @BeforeEach
  public void before() {
    albumDatabase = new MPDAlbumDatabase(tagLister, new MPDAlbumConverter());
  }

  @Test
  public void testListSingleAlbumsByArtist() {
    MPDArtist testArtist = new MPDArtist("testName");

    String testAlbumName = "testAlbum";

    List<String> mockList = new ArrayList<>();
    mockList.add(TagLister.ListType.ARTIST.getType());
    mockList.add(testArtist.getName());

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ALBUM_RESPONSE_PREFIX + testAlbumName);
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

    when(tagLister.list(TagLister.ListType.ALBUM, mockList, ALBUM_GROUPS))
      .thenReturn(mockReturn);

    MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAlbumsByArtist(testArtist)
    );
    assertEquals(1, albums.size());
    assertEquals(testAlbum, albums.get(0));
  }

  @Test
  public void testListMultipleAlbumsByArtist() {
    MPDArtist testArtist = new MPDArtist("testName");

    String testAlbumName1 = "testAlbum1";
    String testAlbumName2 = "testAlbum2";

    List<String> mockList = new ArrayList<>();
    mockList.add(TagLister.ListType.ARTIST.getType());
    mockList.add(testArtist.getName());

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ALBUM_RESPONSE_PREFIX + testAlbumName1);
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

    mockReturn.add(ALBUM_RESPONSE_PREFIX + testAlbumName2);
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

    when(tagLister.list(TagLister.ListType.ALBUM, mockList, ALBUM_GROUPS))
      .thenReturn(mockReturn);

    MPDAlbum testAlbum1 = new MPDAlbum(testAlbumName1, testArtist.getName());
    MPDAlbum testAlbum2 = new MPDAlbum(testAlbumName2, testArtist.getName());

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAlbumsByArtist(testArtist)
    );
    assertEquals(2, albums.size());
    assertEquals(testAlbum1, albums.get(0));
    assertEquals(testAlbum2, albums.get(1));
  }

  @Test
  public void testListAlbumsByGenre() {
    MPDArtist testArtist = new MPDArtist("testArtistName");
    MPDGenre testGenre = new MPDGenre("testGenreName");

    String testAlbumName = "testAlbum";

    List<String> mockList = new ArrayList<>();
    mockList.add(TagLister.ListType.GENRE.getType());
    mockList.add(testGenre.getName());

    List<String> mockReturnGenreList = new ArrayList<>();
    mockReturnGenreList.add(ALBUM_RESPONSE_PREFIX + testAlbumName);
    mockReturnGenreList.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

    List<String> mockAlbumList = new ArrayList<>();
    mockAlbumList.add(TagLister.ListType.ALBUM.getType());
    mockAlbumList.add(testAlbumName);

    when(tagLister.list(TagLister.ListType.ALBUM, mockList, ALBUM_GROUPS))
      .thenReturn(mockReturnGenreList);

    MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAlbumsByGenre(testGenre)
    );
    assertEquals(1, albums.size());
    assertEquals(testAlbum, albums.get(0));
  }

  @Test
  public void testListAlbumsByYear() {
    MPDArtist testArtist = new MPDArtist("testArtistName");
    String testYear = "testYear";
    String testAlbumName = "testAlbum";

    List<String> mockList = new ArrayList<>();
    mockList.add(TagLister.ListType.DATE.getType());
    mockList.add(testYear);

    List<String> mockReturn = new ArrayList<>();
    mockReturn.add(ALBUM_RESPONSE_PREFIX + testAlbumName);
    mockReturn.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());
    mockReturn.add(DATE_RESPONSE_PREFIX + testYear);

    when(tagLister.list(TagLister.ListType.ALBUM, mockList, ALBUM_GROUPS))
      .thenReturn(mockReturn);

    MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());
    testAlbum.setDate(testYear);

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAlbumsByYear(testYear)
    );
    assertEquals(1, albums.size());
    assertEquals(testAlbum, albums.get(0));
  }

  @Test
  public void testListAlbumNamesByYear() {
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

    when(tagLister.list(TagLister.ListType.ALBUM, mockYearList))
      .thenReturn(mockReturnAlbumList);

    List<String> albums = new ArrayList<>(
      albumDatabase.listAlbumNamesByYear(testYear)
    );
    assertEquals(1, albums.size());
    assertEquals(testAlbumName, albums.get(0));
  }

  @Test
  public void testListAllAlbumNames() {
    String testAlbumName1 = "testAlbum1";
    String testAlbumName2 = "testAlbum2";

    List<String> mockAlbumNameList = new ArrayList<>();
    mockAlbumNameList.add(ALBUM_RESPONSE_PREFIX + testAlbumName1);
    mockAlbumNameList.add(ALBUM_RESPONSE_PREFIX + testAlbumName2);

    when(tagLister.list(TagLister.ListType.ALBUM))
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

    loadMockAlbums(albumPrefix, artistPrefix, end);

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAllAlbums(start, end)
    );

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

    loadMockAlbums(albumPrefix, artistPrefix, end);

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

    loadMockAlbums(albumPrefix, artistPrefix, end);

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAllAlbums(start, requested)
    );

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

    loadMockAlbums(albumPrefix, artistPrefix, end);

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAllAlbums(start, start)
    );

    assertEquals(0, albums.size());
  }

  @Test
  public void testListAllAlbumsStartBelowEnd() {
    int end = 50;
    String albumPrefix = "testAlbum";
    String artistPrefix = "testArtist";

    loadMockAlbums(albumPrefix, artistPrefix, end);

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.listAllAlbums(end + 1, end)
    );

    assertEquals(0, albums.size());
  }

  @Test
  public void testFindAlbumByName() {
    MPDArtist testArtist = new MPDArtist("testArtistName");
    String testAlbumName = "testAlbum1";

    List<String> mockList = new ArrayList<>();
    mockList.add(TagLister.ListType.ALBUM.getType());
    mockList.add(testAlbumName);

    List<String> mockAlbumList = new ArrayList<>();
    mockAlbumList.add(TagLister.ListType.ALBUM.getType());
    mockAlbumList.add(ALBUM_RESPONSE_PREFIX + testAlbumName);
    mockAlbumList.add(ARTIST_RESPONSE_PREFIX + testArtist.getName());

    when(tagLister.list(TagLister.ListType.ALBUM, mockList, ALBUM_GROUPS))
      .thenReturn(mockAlbumList);

    MPDAlbum testAlbum = new MPDAlbum(testAlbumName, testArtist.getName());

    List<MPDAlbum> albums = new ArrayList<>(
      albumDatabase.findAlbum(testAlbumName)
    );
    assertEquals(1, albums.size());
    assertEquals(testAlbum, albums.get(0));
  }

  private void loadMockAlbums(
    String albumPrefix,
    String artistPrefix,
    int count
  ) {
    List<String> mockAlbumList = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      mockAlbumList.add(
        new AlbumTagProcessor().getPrefix() + " " + albumPrefix + i
      );
      mockAlbumList.add(
        new ArtistTagProcessor().getPrefix() + " " + artistPrefix + i
      );
    }

    when(tagLister.list(TagLister.ListType.ALBUM, ALBUM_GROUPS))
      .thenReturn(mockAlbumList);
  }
}
