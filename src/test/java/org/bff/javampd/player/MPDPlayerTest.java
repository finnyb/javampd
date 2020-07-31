package org.bff.javampd.player;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDPlayerTest {
  @Mock
  private ServerStatus serverStatus;

  @Mock
  private PlayerProperties playerProperties;

  @Mock
  private CommandExecutor commandExecutor;

  @Mock
  private SongConverter songConverter;

  @InjectMocks
  private MPDPlayer mpdPlayer;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @Captor
  private ArgumentCaptor<Integer> integerArgumentCaptor;

  @Captor
  private ArgumentCaptor<String> paramArgumentCaptor;

  @Test
  public void testGetCurrentSong() {
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";

    MPDSong testSong = new MPDSong(testFile, testTitle);
    List<MPDSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
      .thenReturn(responseList);
    when(songConverter.convertResponseToSong(responseList))
      .thenReturn(testSongResponse);

    MPDSong song = mpdPlayer.getCurrentSong();

    assertEquals(testSong.getFile(), song.getFile());
    assertEquals(testSong.getTitle(), song.getTitle());
  }

  @Test
  public void testGetCurrentSongEmpty() {
    List<String> responseList = new ArrayList<>();
    List<MPDSong> testSongResponse = new ArrayList<>();

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
      .thenReturn(responseList);

    when(songConverter.convertResponseToSong(responseList))
      .thenReturn(testSongResponse);

    assertNull(mpdPlayer.getCurrentSong());
  }

  @Test
  public void testPlay() {
    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertEquals(playerProperties.getPlay(), stringArgumentCaptor.getValue());
  }

  @Test
  public void testPlaySong() {
    int id = 1;
    String testFile = "testFile";
    String testTitle = "testTitle";
    MPDSong testSong = new MPDSong(testFile, testTitle);
    testSong.setId(id);

    when(playerProperties.getPlayId()).thenCallRealMethod();
    mpdPlayer.playSong(testSong);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );
    assertEquals(playerProperties.getPlayId(), stringArgumentCaptor.getValue());
    assertTrue(id == integerArgumentCaptor.getValue());
  }

  @Test
  public void testSeek() {
    int seconds = 100;
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    String[] params = new String[2];
    params[1] = Long.toString(seconds);

    MPDSong testSong = new MPDSong(testFile, testTitle);
    testSong.setId(id);
    testSong.setLength(seconds + 1);

    List<MPDSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
      .thenReturn(responseList);
    when(songConverter.convertResponseToSong(responseList))
      .thenReturn(testSongResponse);

    when(playerProperties.getSeekId()).thenCallRealMethod();
    mpdPlayer.seek(seconds);

    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        paramArgumentCaptor.capture()
      );

    assertEquals(playerProperties.getSeekId(), stringArgumentCaptor.getValue());
    assertEquals(
      Integer.toString(testSong.getId()),
      paramArgumentCaptor.getAllValues().get(0)
    );
    assertEquals(
      Integer.toString(seconds),
      paramArgumentCaptor.getAllValues().get(1)
    );
  }

  @Test
  public void testSeekSongLengthLessThanRequest() {
    int seconds = 100;
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    String[] params = new String[2];
    params[1] = Long.toString(seconds);

    MPDSong testSong = new MPDSong(testFile, testTitle);
    testSong.setId(id);
    testSong.setLength(seconds - 1);

    List<MPDSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
      .thenReturn(responseList);
    when(songConverter.convertResponseToSong(responseList))
      .thenReturn(testSongResponse);

    mpdPlayer.seek(seconds);

    verify(commandExecutor, only())
      .sendCommand(playerProperties.getCurrentSong());
    verifyNoMoreInteractions(commandExecutor);
  }

  @Test
  public void testSeekSong() {
    int seconds = 100;
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    String[] params = new String[2];
    params[1] = Long.toString(seconds);

    MPDSong testSong = new MPDSong(testFile, testTitle);
    testSong.setId(id);
    testSong.setLength(seconds + 1);

    List<MPDSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getSeekId()).thenCallRealMethod();
    mpdPlayer.seekSong(testSong, seconds);

    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        paramArgumentCaptor.capture()
      );

    assertEquals(playerProperties.getSeekId(), stringArgumentCaptor.getValue());
    assertEquals(
      Integer.toString(testSong.getId()),
      paramArgumentCaptor.getAllValues().get(0)
    );
    assertEquals(
      Integer.toString(seconds),
      paramArgumentCaptor.getAllValues().get(1)
    );
  }

  @Test
  public void testPlayerChangeEventStarted() {
    final PlayerChangeEvent.Event[] playerChangeEvent = { null };
    mpdPlayer.addPlayerChangeListener(
      event -> playerChangeEvent[0] = event.getEvent()
    );

    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();

    assertEquals(PlayerChangeEvent.Event.PLAYER_STARTED, playerChangeEvent[0]);
  }

  @Test
  public void testPlayerChangeEventSongSet() {
    final PlayerChangeEvent.Event[] playerChangeEvent = { null };
    mpdPlayer.addPlayerChangeListener(
      event -> playerChangeEvent[0] = event.getEvent()
    );

    MPDSong testSong = new MPDSong("", "");

    when(playerProperties.getPlay()).thenCallRealMethod();
    when(playerProperties.getPlayId()).thenCallRealMethod();
    mpdPlayer.play();
    mpdPlayer.playSong(testSong);

    assertEquals(PlayerChangeEvent.Event.PLAYER_SONG_SET, playerChangeEvent[0]);
  }

  @Test
  public void testRemovePlayerChangedListener() {
    final PlayerChangeEvent.Event[] playerChangeEvent = { null };

    PlayerChangeListener playerChangeListener = event ->
      playerChangeEvent[0] = event.getEvent();

    mpdPlayer.addPlayerChangeListener(playerChangeListener);

    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();

    assertEquals(PlayerChangeEvent.Event.PLAYER_STARTED, playerChangeEvent[0]);

    when(playerProperties.getStop()).thenCallRealMethod();
    mpdPlayer.stop();
    assertEquals(PlayerChangeEvent.Event.PLAYER_STOPPED, playerChangeEvent[0]);

    mpdPlayer.removePlayerChangedListener(playerChangeListener);
    mpdPlayer.play();
    assertEquals(PlayerChangeEvent.Event.PLAYER_STOPPED, playerChangeEvent[0]);
  }

  @Test
  public void testStop() {
    when(playerProperties.getStop()).thenCallRealMethod();
    mpdPlayer.stop();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertEquals(playerProperties.getStop(), stringArgumentCaptor.getValue());
  }

  @Test
  public void testPause() {
    when(playerProperties.getPause()).thenCallRealMethod();
    mpdPlayer.pause();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertEquals(playerProperties.getPause(), stringArgumentCaptor.getValue());
  }

  @Test
  public void testPlayNext() {
    when(playerProperties.getNext()).thenCallRealMethod();
    mpdPlayer.playNext();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertEquals(playerProperties.getNext(), stringArgumentCaptor.getValue());
  }

  @Test
  public void testPlayPrevious() {
    when(playerProperties.getPrevious()).thenCallRealMethod();
    mpdPlayer.playPrevious();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertEquals(
      playerProperties.getPrevious(),
      stringArgumentCaptor.getValue()
    );
  }

  @Test
  public void testMute() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.mute();
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getSetVolume(),
      stringArgumentCaptor.getValue()
    );
    assertTrue(0 == integerArgumentCaptor.getValue());
  }

  @Test
  public void testUnMute() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(1);
    mpdPlayer.unMute();
    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getSetVolume(),
      stringArgumentCaptor.getValue()
    );
    assertTrue(1 == integerArgumentCaptor.getAllValues().get(0));
    assertTrue(0 == integerArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testAddVolumeChangeListener() {
    final VolumeChangeEvent[] volumeChangeEvent = { null };
    mpdPlayer.addVolumeChangeListener(event -> volumeChangeEvent[0] = event);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(5);

    assertEquals(5, volumeChangeEvent[0].getVolume());
  }

  @Test
  public void testAddVolumeChangeListenerOutOfRange() {
    final VolumeChangeEvent[] volumeChangeEvent = { null };
    mpdPlayer.addVolumeChangeListener(event -> volumeChangeEvent[0] = event);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(0);
    mpdPlayer.setVolume(101);

    assertEquals(0, volumeChangeEvent[0].getVolume());
  }

  @Test
  public void testRemoveVolumeChangedListener() {
    final VolumeChangeEvent[] volumeChangeEvent = { null };

    VolumeChangeListener volumeChangeListener = event ->
      volumeChangeEvent[0] = event;

    mpdPlayer.addVolumeChangeListener(volumeChangeListener);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(5);

    assertEquals(5, volumeChangeEvent[0].getVolume());

    mpdPlayer.removeVolumeChangedListener(volumeChangeListener);
    mpdPlayer.setVolume(0);

    assertEquals(5, volumeChangeEvent[0].getVolume());
  }

  @Test
  public void testRandomizePlay() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.randomizePlay();
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRandom(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("1", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testUnrandomizePlay() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.unRandomizePlay();
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRandom(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("0", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testSetRandomTrue() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.setRandom(true);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRandom(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("1", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testSetRandomFalse() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.setRandom(false);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRandom(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("0", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testIsRandom() {
    when(serverStatus.isRandom()).thenReturn(true);
    assertTrue(mpdPlayer.isRandom());
  }

  @Test
  public void testSetRepeatTrue() {
    when(playerProperties.getRepeat()).thenCallRealMethod();
    mpdPlayer.setRepeat(true);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRepeat(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("1", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testSetRepeatFalse() {
    when(playerProperties.getRepeat()).thenCallRealMethod();
    mpdPlayer.setRepeat(false);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getRepeat(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("0", stringArgumentCaptor.getAllValues().get(1));
  }

  @Test
  public void testIsRepeat() {
    when(serverStatus.isRepeat()).thenReturn(true);
    assertTrue(mpdPlayer.isRepeat());
  }

  @Test
  public void testGetBitrate() {
    when(serverStatus.getBitrate()).thenReturn(1);
    assertTrue(1 == mpdPlayer.getBitrate());
  }

  @Test
  public void testGetVolume() {
    when(serverStatus.getVolume()).thenReturn(1);
    assertTrue(1 == mpdPlayer.getVolume());
  }

  @Test
  public void testSetVolume() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(0);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );
    assertEquals(
      playerProperties.getSetVolume(),
      stringArgumentCaptor.getValue()
    );
    assertTrue(0 == integerArgumentCaptor.getValue());
  }

  @Test
  public void testSetVolumeOutOfRangeHigh() {
    mpdPlayer.setVolume(101);
    verify(commandExecutor, never())
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
  }

  @Test
  public void testSetVolumeOutOfRangeLow() {
    mpdPlayer.setVolume(-1);
    verify(commandExecutor, never())
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );
  }

  @Test
  public void testGetXFade() {
    when(serverStatus.getXFade()).thenReturn(1);
    assertTrue(1 == mpdPlayer.getXFade());
  }

  @Test
  public void testSetXFade() {
    when(playerProperties.getXFade()).thenCallRealMethod();
    mpdPlayer.setXFade(5);
    verify(commandExecutor)
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );
    assertEquals(playerProperties.getXFade(), stringArgumentCaptor.getValue());
    assertTrue(5 == integerArgumentCaptor.getValue());
  }

  @Test
  public void testGetElapsedTime() {
    when(serverStatus.getElapsedTime()).thenReturn(1L);
    assertTrue(1 == mpdPlayer.getElapsedTime());
  }

  @Test
  public void testGetTotalTime() {
    when(serverStatus.getTotalTime()).thenReturn(1L);
    assertTrue(1 == mpdPlayer.getTotalTime());
  }

  @Test
  public void testGetStatusPlaying() {
    when(serverStatus.getState())
      .thenReturn(Player.Status.STATUS_PLAYING.getPrefix());
    assertEquals(Player.Status.STATUS_PLAYING, mpdPlayer.getStatus());
  }

  @Test
  public void testGetStatusPaused() {
    when(serverStatus.getState())
      .thenReturn(Player.Status.STATUS_PAUSED.getPrefix());
    assertEquals(Player.Status.STATUS_PAUSED, mpdPlayer.getStatus());
  }

  @Test
  public void testGetStatusStopped() {
    when(serverStatus.getState())
      .thenReturn(Player.Status.STATUS_STOPPED.getPrefix());
    assertEquals(Player.Status.STATUS_STOPPED, mpdPlayer.getStatus());
  }

  @Test
  public void testGetAudioDetailsBitrate() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertTrue(5 == mpdPlayer.getAudioDetails().getBits());
  }

  @Test
  public void testGetAudioDetailsBadBitrate() {
    when(serverStatus.getAudio()).thenReturn("4:bad:6");
    assertTrue(-1 == mpdPlayer.getAudioDetails().getBits());
  }

  @Test
  public void testGetAudioDetailsChannels() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertTrue(6 == mpdPlayer.getAudioDetails().getChannels());
  }

  @Test
  public void testGetAudioDetailsBadChannels() {
    when(serverStatus.getAudio()).thenReturn("4:5:bad");
    assertTrue(-1 == mpdPlayer.getAudioDetails().getChannels());
  }

  @Test
  public void testGetAudioDetailsSampleRate() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertTrue(4 == mpdPlayer.getAudioDetails().getSampleRate());
  }

  @Test
  public void testGetAudioDetailsBadSampleRate() {
    when(serverStatus.getAudio()).thenReturn("bad:5:6");
    assertTrue(-1 == mpdPlayer.getAudioDetails().getSampleRate());
  }
}
