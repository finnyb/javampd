package org.bff.javampd.playlist;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.file.MPDFile;
import org.bff.javampd.server.ServerStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDPlaylistTest {
  @Mock private ServerStatus serverStatus;
  @Mock private PlaylistProperties playlistProperties;
  @Mock private CommandExecutor commandExecutor;
  @InjectMocks private MPDPlaylist playlist;
  @Captor private ArgumentCaptor<String> stringArgumentCaptor;
  @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;
  @Captor private ArgumentCaptor<List<MPDCommand>> commandArgumentCaptor;

  private PlaylistProperties realPlaylistProperties;

  @BeforeEach
  void setup() {
    realPlaylistProperties = new PlaylistProperties();
  }

  @Test
  void testAddPlaylistChangeListener() {
    final boolean[] gotEvent = {true};
    playlist.addPlaylistChangeListener(event -> gotEvent[0] = true);
    playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
    assertTrue(gotEvent[0]);
  }

  @Test
  void testRemovePlaylistStatusChangedListener() {
    final boolean[] gotEvent = {true};

    PlaylistChangeListener pcl = event -> gotEvent[0] = true;

    playlist.addPlaylistChangeListener(pcl);
    playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
    assertTrue(gotEvent[0]);

    gotEvent[0] = false;
    playlist.removePlaylistStatusChangedListener(pcl);
    playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
    assertFalse(gotEvent[0]);
  }

  @Test
  void testFirePlaylistChangeEvent() {
    final boolean[] gotEvent = {true};
    playlist.addPlaylistChangeListener(event -> gotEvent[0] = true);
    playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED);
    assertTrue(gotEvent[0]);
  }

  @Test
  void testFirePlaylistChangeEventSongAdded() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_ADDED, "name");
    assertEquals("name", changeEvent[0].getName());
  }

  @Test
  void testLoadPlaylist() {
    String testPlaylist = "testPlaylist";
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.loadPlaylist(testPlaylist);

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertEquals(realPlaylistProperties.getLoad(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testLoadPlaylistM3u() {
    String testPlaylist = "testPlaylist.m3u";
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.loadPlaylist(testPlaylist);

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertEquals(realPlaylistProperties.getLoad(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testAddFileOrDirectory() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.addFileOrDirectory(MPDFile.builder("test").build());

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("test", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.FILE_ADDED, changeEvent[0].getEvent());
  }

  @Test
  void testClearPlaylist() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.clearPlaylist();

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getClear(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CLEARED, changeEvent[0].getEvent());
  }

  @Test
  void testDeletePlaylist() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDSavedPlaylist savedPlaylist = MPDSavedPlaylist.builder("testPlaylist").build();
    playlist.deletePlaylist(savedPlaylist);

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getDelete(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_DELETED, changeEvent[0].getEvent());
  }

  @Test
  void testDeletePlaylistByName() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
    playlist.deletePlaylist("testPlaylist");

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getDelete(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_DELETED, changeEvent[0].getEvent());
  }

  @Test
  void testMoveById() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDPlaylistSong song = MPDPlaylistSong.builder().file("test").title("test").id(3).build();
    playlist.move(song, 5);

    verify(commandExecutor)
        .sendCommand(
            stringArgumentCaptor.capture(),
            integerArgumentCaptor.capture(),
            integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getMoveId(), stringArgumentCaptor.getValue());
    assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
    assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testMoveByPosition() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDPlaylistSong song = MPDPlaylistSong.builder().file("test").title("test").position(3).build();
    playlist.move(song, 5);

    verify(commandExecutor)
        .sendCommand(
            stringArgumentCaptor.capture(),
            integerArgumentCaptor.capture(),
            integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getMove(), stringArgumentCaptor.getValue());
    assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
    assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testShuffle() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.shuffle();

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getShuffle(), stringArgumentCaptor.getValue());
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testSwapByExplicitId() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDPlaylistSong song1 = MPDPlaylistSong.builder().file("test").title("test").id(3).build();
    playlist.swap(song1, 5);

    verify(commandExecutor)
        .sendCommand(
            stringArgumentCaptor.capture(),
            integerArgumentCaptor.capture(),
            integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getSwapId(), stringArgumentCaptor.getValue());
    assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
    assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testSwapById() {
    when(serverStatus.getPlaylistVersion()).thenReturn(1);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDPlaylistSong song1 = MPDPlaylistSong.builder().file("test").title("test").id(3).build();
    MPDPlaylistSong song2 = MPDPlaylistSong.builder().file("test").title("test").id(5).build();

    playlist.swap(song1, song2);

    verify(commandExecutor)
        .sendCommand(
            stringArgumentCaptor.capture(),
            integerArgumentCaptor.capture(),
            integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getSwapId(), stringArgumentCaptor.getValue());
    assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
    assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testSwapByPosition() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    MPDPlaylistSong song1 =
        MPDPlaylistSong.builder().file("test").title("test").position(3).build();
    MPDPlaylistSong song2 =
        MPDPlaylistSong.builder().file("test").title("test").position(5).build();

    playlist.swap(song1, song2);

    verify(commandExecutor)
        .sendCommand(
            stringArgumentCaptor.capture(),
            integerArgumentCaptor.capture(),
            integerArgumentCaptor.capture());

    assertEquals(realPlaylistProperties.getSwap(), stringArgumentCaptor.getValue());
    assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
    assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
  }

  @Test
  void testPlaylistSave() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    boolean saved = playlist.savePlaylist("name");

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertTrue(saved);
    assertEquals(realPlaylistProperties.getSave(), stringArgumentCaptor.getAllValues().get(0));
    assertEquals("name", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(PlaylistChangeEvent.Event.PLAYLIST_SAVED, changeEvent[0].getEvent());
  }

  @Test
  void testPlaylistSaveNull() {
    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    boolean saved = playlist.savePlaylist(null);

    assertFalse(saved);
    assertNull(changeEvent[0]);
  }
}
