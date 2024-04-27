package org.bff.javampd.album;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bff.javampd.artist.MPDArtist;
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
class MPDAlbumDatabaseTest {

  @Captor private ArgumentCaptor<List<String>> argumentCaptor;
  @Captor private ArgumentCaptor<TagLister.GroupType> groupCaptor;
  @Captor private ArgumentCaptor<TagLister.ListType> listCaptor;

  @Mock private TagLister tagLister;

  private MPDAlbumDatabase albumDatabase;

  @BeforeEach
  void before() {
    albumDatabase = new MPDAlbumDatabase(tagLister, new MPDAlbumConverter());
  }

  @Test
  void albumsByAlbumArtist() {
    String albumArtist = "Tool";

    when(tagLister.list(any(), (List<String>) any(), any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.listAlbumsByAlbumArtist(new MPDArtist(albumArtist));

    verify(tagLister)
        .list(
            listCaptor.capture(),
            argumentCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals("albumartist", argumentCaptor.getValue().getFirst()),
        () -> assertEquals(albumArtist, argumentCaptor.getValue().get(1)),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }

  @Test
  void albumsByArtist() {
    String artist = "Tool";

    when(tagLister.list(any(), (List<String>) any(), any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.listAlbumsByArtist(new MPDArtist(artist));

    verify(tagLister)
        .list(
            listCaptor.capture(),
            argumentCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals("artist", argumentCaptor.getValue().getFirst()),
        () -> assertEquals(artist, argumentCaptor.getValue().get(1)),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }

  @Test
  void allAlbums() {
    when(tagLister.list(any(), (TagLister.GroupType) any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.listAllAlbums();

    verify(tagLister)
        .list(
            listCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals(TagLister.ListType.ALBUM, listCaptor.getValue()),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }

  @Test
  void allAlbumNames() {
    when(tagLister.list(any())).thenReturn(new ArrayList<>());

    albumDatabase.listAllAlbumNames();

    verify(tagLister).list(listCaptor.capture());

    assertEquals(TagLister.ListType.ALBUM, listCaptor.getValue());
  }

  @Test
  void allAlbumNamesParsing() {
    when(tagLister.list(any()))
        .thenReturn(
            Arrays.asList(
                "Album: 10,000 Days",
                "Album: 72826",
                "Album: Anthem of the Peaceful Army",
                "Album: Dark Before Dawn"));

    var albums = new ArrayList<>(albumDatabase.listAllAlbumNames());

    assertAll(
        () -> assertEquals("10,000 Days", albums.getFirst()),
        () -> assertEquals("72826", albums.get(1)),
        () -> assertEquals("Anthem of the Peaceful Army", albums.get(2)),
        () -> assertEquals("Dark Before Dawn", albums.get(3)));
  }

  @Test
  void findAlbumsByName() {
    String album = "Saturate";

    when(tagLister.list(any(), (List<String>) any(), any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.findAlbum(album);

    verify(tagLister)
        .list(
            listCaptor.capture(),
            argumentCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals("album", argumentCaptor.getValue().getFirst()),
        () -> assertEquals(album, argumentCaptor.getValue().get(1)),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }

  @Test
  void albumsByGenre() {
    String genre = "Heavy Metal";

    when(tagLister.list(any(), (List<String>) any(), any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.listAlbumsByGenre(new MPDGenre(genre));

    verify(tagLister)
        .list(
            listCaptor.capture(),
            argumentCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals("genre", argumentCaptor.getValue().getFirst()),
        () -> assertEquals(genre, argumentCaptor.getValue().get(1)),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }

  @Test
  void albumsByYear() {
    String year = "1990";

    when(tagLister.list(any(), (List<String>) any(), any(), any(), any(), any()))
        .thenReturn(new ArrayList<>());

    albumDatabase.listAlbumsByYear(year);

    verify(tagLister)
        .list(
            listCaptor.capture(),
            argumentCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture(),
            groupCaptor.capture());

    assertAll(
        () -> assertEquals("date", argumentCaptor.getValue().getFirst()),
        () -> assertEquals(year, argumentCaptor.getValue().get(1)),
        () -> assertEquals(TagLister.GroupType.ARTIST, groupCaptor.getAllValues().getFirst()),
        () -> assertEquals(TagLister.GroupType.DATE, groupCaptor.getAllValues().get(1)),
        () -> assertEquals(TagLister.GroupType.GENRE, groupCaptor.getAllValues().get(2)),
        () -> assertEquals(TagLister.GroupType.ALBUM_ARTIST, groupCaptor.getAllValues().get(3)));
  }
}
