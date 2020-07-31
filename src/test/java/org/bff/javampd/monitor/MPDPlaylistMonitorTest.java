package org.bff.javampd.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import org.bff.javampd.playlist.PlaylistBasicChangeEvent;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDPlaylistMonitorTest {
  @Mock
  private PlayerMonitor playerMonitor;

  @InjectMocks
  private MPDPlaylistMonitor playlistMonitor;

  @Test
  public void testAddPlaylistChangeListener() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.firePlaylistChangeEvent(
      PlaylistBasicChangeEvent.Event.SONG_ADDED
    );
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
  }

  @Test
  public void testRemovePlaylistChangeListener() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    PlaylistBasicChangeListener playlistChangeListener = event ->
      changeEvent[0] = event.getEvent();

    playlistMonitor.addPlaylistChangeListener(playlistChangeListener);

    playlistMonitor.firePlaylistChangeEvent(
      PlaylistBasicChangeEvent.Event.SONG_ADDED
    );
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
    playlistMonitor.removePlaylistChangeListener(playlistChangeListener);
    playlistMonitor.firePlaylistChangeEvent(
      PlaylistBasicChangeEvent.Event.SONG_DELETED
    );
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
  }

  @Test
  public void testGetSongId() {
    playlistMonitor.processResponseStatus("songid: 1");
    assertEquals(1, playlistMonitor.getSongId());
  }

  @Test
  public void testPlayerStopped() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("songid: -1");
    playlistMonitor.playerStopped();
    assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_ENDED, changeEvent[0]);
  }

  @Test
  public void testPlayerNotStopped() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );

    playlistMonitor.processResponseStatus("songid: 1");
    playlistMonitor.playerStopped();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusPlaylistChanged() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("playlist: 1");
    playlistMonitor.checkStatus();
    assertEquals(
      PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED,
      changeEvent[0]
    );
  }

  @Test
  public void testProcessResponseStatusPlaylistNotChanged() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("playlist: 1");
    playlistMonitor.checkStatus();
    assertEquals(
      PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED,
      changeEvent[0]
    );

    changeEvent[0] = null;
    playlistMonitor.processResponseStatus("playlist: 1");
    playlistMonitor.checkStatus();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusSongAdded() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("playlistlength: 1");
    playlistMonitor.checkStatus();
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusSongDeleted() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("playlistlength: 1");
    playlistMonitor.checkStatus();
    playlistMonitor.processResponseStatus("playlistlength: 0");
    playlistMonitor.checkStatus();
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_DELETED, changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusSongUnchanged() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("playlistlength: 1");
    playlistMonitor.checkStatus();
    changeEvent[0] = null;
    playlistMonitor.processResponseStatus("playlistlength: 1");
    playlistMonitor.checkStatus();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusSongChanged() {
    statusSongChanged("song: 1");
  }

  @Test
  public void testProcessResponseStatusSongChangedById() {
    statusSongChanged("songid: 1");
  }

  private void statusSongChanged(String line) {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus(line);
    when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
    playlistMonitor.checkStatus();
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_CHANGED, changeEvent[0]);
  }

  private void statusSongNoChange(String line) {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus(line);
    when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
    playlistMonitor.checkStatus();

    changeEvent[0] = null;
    playlistMonitor.processResponseStatus(line);
    when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
    playlistMonitor.checkStatus();

    assertNull(changeEvent[0]);
  }

  @Test
  public void testProcessResponseStatusSongNoChange() {
    statusSongNoChange("song: 1");
  }

  @Test
  public void testProcessResponseStatusSongNoChangeById() {
    statusSongNoChange("songid: 1");
  }

  @Test
  public void testProcessResponseStatusPlaylistNothing() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];
    playlistMonitor.processResponseStatus("bogus: 1");
    playlistMonitor.checkStatus();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testSongChangeNotPlaying() {
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus("song: 1");
    when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
    playlistMonitor.checkStatus();

    assertNull(changeEvent[0]);
  }

  @Test
  public void testResetSongId() {
    String line = "songid: 1";
    statusSongChanged(line);
    playlistMonitor.reset();
    statusSongChanged(line);
  }

  @Test
  public void testResetSong() {
    String line = "song: 1";
    statusSongChanged(line);
    playlistMonitor.reset();
    statusSongChanged(line);
  }

  @Test
  public void testResetPlaylistVersion() {
    String line = "playlist: 1";
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus(line);
    playlistMonitor.checkStatus();
    assertEquals(
      PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED,
      changeEvent[0]
    );

    playlistMonitor.reset();
    changeEvent[0] = null;

    playlistMonitor.processResponseStatus(line);
    playlistMonitor.checkStatus();
    assertEquals(
      PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED,
      changeEvent[0]
    );
  }

  @Test
  public void testResetPlaylistLength() {
    String line = "playlistlength: 1";
    final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

    playlistMonitor.addPlaylistChangeListener(
      event -> changeEvent[0] = event.getEvent()
    );
    playlistMonitor.processResponseStatus(line);
    playlistMonitor.checkStatus();
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);

    playlistMonitor.reset();
    changeEvent[0] = null;

    playlistMonitor.processResponseStatus(line);
    playlistMonitor.checkStatus();
    assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
  }
}
