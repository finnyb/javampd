package org.bff.javampd.database;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDTagListerTest {

  @Mock private MPDCommandExecutor commandExecutor;
  @Mock private DatabaseProperties databaseProperties;
  @Captor private ArgumentCaptor<String> argumentCaptor;

  @InjectMocks private MPDTagLister tagLister;

  @Test
  void testListInfoSingle() {
    List<String> retList = new ArrayList<>();
    retList.add("playlist: 5");
    when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
    when(databaseProperties.getListInfo()).thenReturn("lsinfo");

    List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

    assertEquals(1, infoList.size());
    assertEquals("5", infoList.get(0));
  }

  @Test
  void testListInfoDouble() {
    List<String> retList = new ArrayList<>();
    retList.add("playlist: 5");
    retList.add("directory: 6");
    when(databaseProperties.getListInfo()).thenReturn("lsinfo");
    when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
    List<String> infoList =
        new ArrayList<>(
            tagLister.listInfo(TagLister.ListInfoType.PLAYLIST, TagLister.ListInfoType.DIRECTORY));

    assertEquals(2, infoList.size());
    assertEquals("5", infoList.get(0));
    assertEquals("6", infoList.get(1));
  }

  @Test
  void testListInfoNone() {
    List<String> retList = new ArrayList<>();
    retList.add("bogus: 5");
    when(databaseProperties.getListInfo()).thenReturn("lsinfo");
    when(commandExecutor.sendCommand("lsinfo")).thenReturn(retList);
    List<String> infoList = new ArrayList<>(tagLister.listInfo(TagLister.ListInfoType.PLAYLIST));

    assertEquals(0, infoList.size());
  }

  @Test
  void testList() {
    String testAlbumResponse = "album: 5";
    List<String> retList = new ArrayList<>();
    retList.add(testAlbumResponse);
    when(commandExecutor.sendCommand("list", "album")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");
    List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM));

    assertEquals(1, infoList.size());
    assertEquals(testAlbumResponse, infoList.get(0));
  }

  @Test
  void testListGroupArtist() {
    List<String> retList = new ArrayList<>();
    retList.add("album: testAlbum");
    retList.add("artist: testArtist");
    when(commandExecutor.sendCommand("list", "album", "group", "artist")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");
    when(databaseProperties.getGroup()).thenReturn("group");
    List<String> infoList =
        new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.ARTIST));

    assertEquals(2, infoList.size());
    assertEquals("album: testAlbum", infoList.get(0));
    assertEquals("artist: testArtist", infoList.get(1));
  }

  @Test
  void testListGroupDate() {
    List<String> retList = new ArrayList<>();
    retList.add("album: testAlbum");
    retList.add("date: testDate");
    when(commandExecutor.sendCommand("list", "album", "group", "date")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");
    when(databaseProperties.getGroup()).thenReturn("group");

    List<String> infoList =
        new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.DATE));

    assertEquals(2, infoList.size());
    assertEquals("album: testAlbum", infoList.get(0));
    assertEquals("date: testDate", infoList.get(1));
  }

  @Test
  void testListGroupGenre() {
    List<String> retList = new ArrayList<>();
    retList.add("album: testAlbum");
    retList.add("genre: testGenre");
    when(commandExecutor.sendCommand("list", "album", "group", "genre")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");
    when(databaseProperties.getGroup()).thenReturn("group");

    List<String> infoList =
        new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, TagLister.GroupType.GENRE));

    assertEquals(2, infoList.size());
    assertEquals("album: testAlbum", infoList.get(0));
    assertEquals("genre: testGenre", infoList.get(1));
  }

  @Test
  void testListError() {
    List<String> retList = new ArrayList<>();
    retList.add("");
    when(commandExecutor.sendCommand("list", "artist")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");

    List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST));

    assertEquals(1, infoList.size());
    assertEquals("", infoList.get(0));
  }

  @Test
  void testListWithParam() {
    String testResponse = "album: artist";
    List<String> retList = new ArrayList<>();
    retList.add(testResponse);

    List<String> params = new ArrayList<>();
    params.add("artist");

    when(commandExecutor.sendCommand("list", "album", "artist")).thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");

    List<String> infoList = new ArrayList<>(tagLister.list(TagLister.ListType.ALBUM, params));

    assertEquals(1, infoList.size());
    assertEquals(testResponse, infoList.get(0));
  }

  @Test
  void testListWithParamAndGroups() {
    String testResponse = "album: artist";
    List<String> retList = new ArrayList<>();
    retList.add(testResponse);

    List<String> params = new ArrayList<>();
    params.add("artist");

    when(commandExecutor.sendCommand(
            "list", "album", "artist", "group", TagLister.GroupType.ARTIST.getType()))
        .thenReturn(retList);
    when(databaseProperties.getList()).thenReturn("list");
    when(databaseProperties.getGroup()).thenReturn("group");

    List<String> infoList =
        new ArrayList<>(
            tagLister.list(TagLister.ListType.ALBUM, params, TagLister.GroupType.ARTIST));

    assertEquals(1, infoList.size());
    assertEquals(testResponse, infoList.get(0));
  }

  @Test
  @DisplayName("verify order of group params")
  void groupOrder() {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.ARTIST.getType());
    list.add("Tool");

    when(databaseProperties.getList()).thenReturn("list");
    when(databaseProperties.getGroup()).thenReturn("group");

    tagLister.list(
        TagLister.ListType.ALBUM,
        list,
        TagLister.GroupType.ARTIST,
        TagLister.GroupType.DATE,
        TagLister.GroupType.GENRE,
        TagLister.GroupType.ALBUM_ARTIST);

    verify(commandExecutor)
        .sendCommand(
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture(),
            argumentCaptor.capture());

    assertAll(
        () -> assertThat(argumentCaptor.getAllValues().get(0), is(equalTo("list"))),
        () -> assertThat(argumentCaptor.getAllValues().get(1), is(equalTo("album"))),
        () -> assertThat(argumentCaptor.getAllValues().get(2), is(equalTo("artist"))),
        () -> assertThat(argumentCaptor.getAllValues().get(3), is(equalTo("Tool"))),
        () -> assertThat(argumentCaptor.getAllValues().get(4), is(equalTo("group"))),
        () -> assertThat(argumentCaptor.getAllValues().get(5), is(equalTo("artist"))),
        () -> assertThat(argumentCaptor.getAllValues().get(6), is(equalTo("group"))),
        () -> assertThat(argumentCaptor.getAllValues().get(7), is(equalTo("date"))),
        () -> assertThat(argumentCaptor.getAllValues().get(8), is(equalTo("group"))),
        () -> assertThat(argumentCaptor.getAllValues().get(9), is(equalTo("genre"))),
        () -> assertThat(argumentCaptor.getAllValues().get(10), is(equalTo("group"))),
        () -> assertThat(argumentCaptor.getAllValues().get(11), is(equalTo("albumartist"))));
  }
}
