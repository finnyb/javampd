package org.bff.javampd.player;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.playlist.MPDPlaylistSong;
import org.bff.javampd.playlist.PlaylistSongConverter;
import org.bff.javampd.server.ServerStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDPlayerTest {

  @Mock private ServerStatus serverStatus;
  @Mock private PlayerProperties playerProperties;

  @Mock private CommandExecutor commandExecutor;

  @Mock private PlaylistSongConverter playlistSongConverter;

  @InjectMocks private MPDPlayer mpdPlayer;

  @Captor private ArgumentCaptor<String> stringArgumentCaptor;

  @Captor private ArgumentCaptor<Integer> integerArgumentCaptor;

  @Captor private ArgumentCaptor<String> paramArgumentCaptor;

  @Test
  void testGetCurrentSong() {
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";

    MPDPlaylistSong testSong = MPDPlaylistSong.builder().file(testFile).title(testTitle).build();

    List<MPDPlaylistSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong())).thenReturn(responseList);
    when(playlistSongConverter.convertResponseToSongs(responseList)).thenReturn(testSongResponse);

    mpdPlayer
        .getCurrentSong()
        .ifPresentOrElse(
            song -> {
              assertThat(song.getFile(), is(equalTo((testSong.getFile()))));
              assertThat(song.getTitle(), is(equalTo((testSong.getTitle()))));
            },
            () -> fail("No song found"));
  }

  @Test
  void testGetCurrentSongEmpty() {
    var responseList = new ArrayList<String>();
    var testSongResponse = new ArrayList<MPDPlaylistSong>();

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong())).thenReturn(responseList);

    when(playlistSongConverter.convertResponseToSongs(responseList)).thenReturn(testSongResponse);

    assertThat(mpdPlayer.getCurrentSong(), is(Optional.empty()));
  }

  @Test
  void testPlay() {
    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getPlay()))));
  }

  @Test
  void testPlaySong() {
    int id = 1;
    String testFile = "testFile";
    String testTitle = "testTitle";
    MPDPlaylistSong testSong =
        MPDPlaylistSong.builder().file(testFile).title(testTitle).id(id).build();

    when(playerProperties.getPlayId()).thenCallRealMethod();
    mpdPlayer.playSong(testSong);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getPlayId()))));
    assertThat(id, is(equalTo(integerArgumentCaptor.getValue())));
  }

  @Test
  void testSeek() {
    int seconds = 100;
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    MPDPlaylistSong testSong =
        MPDPlaylistSong.builder()
            .file(testFile)
            .title(testTitle)
            .id(id)
            .length(seconds + 1)
            .build();

    List<MPDPlaylistSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong())).thenReturn(responseList);
    when(playlistSongConverter.convertResponseToSongs(responseList)).thenReturn(testSongResponse);

    when(playerProperties.getSeekId()).thenCallRealMethod();
    mpdPlayer.seek(seconds);

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getSeekId()))));
    assertThat(
        paramArgumentCaptor.getAllValues().get(0),
        is(equalTo((Integer.toString(testSong.getId())))));
    assertThat(paramArgumentCaptor.getAllValues().get(1), is(equalTo((Integer.toString(seconds)))));
  }

  @Test
  void testSeekSongLengthLessThanRequest() {
    int seconds = 100;
    List<String> responseList = new ArrayList<>();
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    MPDPlaylistSong testSong =
        MPDPlaylistSong.builder()
            .file(testFile)
            .title(testTitle)
            .id(id)
            .length(seconds - 1)
            .build();

    List<MPDPlaylistSong> testSongResponse = new ArrayList<>();
    testSongResponse.add(testSong);

    when(playerProperties.getCurrentSong()).thenCallRealMethod();
    when(commandExecutor.sendCommand(playerProperties.getCurrentSong())).thenReturn(responseList);
    when(playlistSongConverter.convertResponseToSongs(responseList)).thenReturn(testSongResponse);

    mpdPlayer.seek(seconds);

    verify(commandExecutor, only()).sendCommand(playerProperties.getCurrentSong());
    verifyNoMoreInteractions(commandExecutor);
  }

  @Test
  void testSeekSong() {
    int seconds = 100;
    String testFile = "testFile";
    String testTitle = "testTitle";
    int id = 5;

    MPDPlaylistSong testSong =
        MPDPlaylistSong.builder()
            .file(testFile)
            .title(testTitle)
            .id(id)
            .length(seconds + 1)
            .build();

    when(playerProperties.getSeekId()).thenCallRealMethod();
    mpdPlayer.seekSong(testSong, seconds);

    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), paramArgumentCaptor.capture());

    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getSeekId()))));
    assertThat(
        paramArgumentCaptor.getAllValues().get(0),
        is(equalTo((Integer.toString(testSong.getId())))));
    assertThat(paramArgumentCaptor.getAllValues().get(1), is(equalTo((Integer.toString(seconds)))));
  }

  @Test
  void testPlayerChangeEventStarted() {
    final PlayerChangeEvent.Event[] playerChangeEvent = {null};
    mpdPlayer.addPlayerChangeListener(event -> playerChangeEvent[0] = event.getEvent());

    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();

    assertThat(playerChangeEvent[0], is(equalTo((PlayerChangeEvent.Event.PLAYER_STARTED))));
  }

  @Test
  void testPlayerChangeEventSongSet() {
    final PlayerChangeEvent.Event[] playerChangeEvent = {null};
    mpdPlayer.addPlayerChangeListener(event -> playerChangeEvent[0] = event.getEvent());

    MPDPlaylistSong testSong = MPDPlaylistSong.builder().file("").title("").build();

    when(playerProperties.getPlay()).thenCallRealMethod();
    when(playerProperties.getPlayId()).thenCallRealMethod();
    mpdPlayer.play();
    mpdPlayer.playSong(testSong);

    assertThat(playerChangeEvent[0], is(equalTo((PlayerChangeEvent.Event.PLAYER_SONG_SET))));
  }

  @Test
  void testRemovePlayerChangedListener() {
    final PlayerChangeEvent.Event[] playerChangeEvent = {null};

    PlayerChangeListener playerChangeListener = event -> playerChangeEvent[0] = event.getEvent();

    mpdPlayer.addPlayerChangeListener(playerChangeListener);

    when(playerProperties.getPlay()).thenCallRealMethod();
    mpdPlayer.play();

    assertThat(playerChangeEvent[0], is(equalTo((PlayerChangeEvent.Event.PLAYER_STARTED))));

    when(playerProperties.getStop()).thenCallRealMethod();
    mpdPlayer.stop();
    assertThat(playerChangeEvent[0], is(equalTo((PlayerChangeEvent.Event.PLAYER_STOPPED))));

    mpdPlayer.removePlayerChangedListener(playerChangeListener);
    mpdPlayer.play();
    assertThat(playerChangeEvent[0], is(equalTo((PlayerChangeEvent.Event.PLAYER_STOPPED))));
  }

  @Test
  void testStop() {
    when(playerProperties.getStop()).thenCallRealMethod();
    mpdPlayer.stop();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getStop()))));
  }

  @Test
  void testPause() {
    when(playerProperties.getPause()).thenCallRealMethod();
    mpdPlayer.pause();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getPause()))));
  }

  @Test
  void testPlayNext() {
    when(playerProperties.getNext()).thenCallRealMethod();
    mpdPlayer.playNext();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getNext()))));
  }

  @Test
  void testPlayPrevious() {
    when(playerProperties.getPrevious()).thenCallRealMethod();
    mpdPlayer.playPrevious();
    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getPrevious()))));
  }

  @Test
  void testMute() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.mute();
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getSetVolume()))));
    assertThat(integerArgumentCaptor.getValue(), is(equalTo(0)));
  }

  @Test
  void testUnMute() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(1);
    mpdPlayer.unMute();
    verify(commandExecutor, times(2))
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getSetVolume()))));
    assertThat(integerArgumentCaptor.getAllValues().get(0), is(equalTo(1)));
    assertThat(integerArgumentCaptor.getAllValues().get(1), is(equalTo(0)));
  }

  @Test
  void testAddVolumeChangeListener() {
    final VolumeChangeEvent[] volumeChangeEvent = {null};
    mpdPlayer.addVolumeChangeListener(event -> volumeChangeEvent[0] = event);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(5);

    assertThat(volumeChangeEvent[0].getVolume(), is(equalTo((5))));
  }

  @Test
  void testAddVolumeChangeListenerOutOfRange() {
    final VolumeChangeEvent[] volumeChangeEvent = {null};
    mpdPlayer.addVolumeChangeListener(event -> volumeChangeEvent[0] = event);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(0);
    mpdPlayer.setVolume(101);

    assertThat(volumeChangeEvent[0].getVolume(), is(equalTo((0))));
  }

  @Test
  void testRemoveVolumeChangedListener() {
    final VolumeChangeEvent[] volumeChangeEvent = {null};

    VolumeChangeListener volumeChangeListener = event -> volumeChangeEvent[0] = event;

    mpdPlayer.addVolumeChangeListener(volumeChangeListener);

    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(5);

    assertThat(volumeChangeEvent[0].getVolume(), (is(equalTo((5)))));

    mpdPlayer.removeVolumeChangedListener(volumeChangeListener);
    mpdPlayer.setVolume(0);

    assertThat(volumeChangeEvent[0].getVolume(), (is(equalTo((5)))));
  }

  @Test
  void testRandomizePlay() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.randomizePlay();
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getRandom()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(("1"))));
  }

  @Test
  void testUnrandomizePlay() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.unRandomizePlay();
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo(playerProperties.getRandom())));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(("0"))));
  }

  @Test
  void testSetRandomTrue() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.setRandom(true);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getRandom()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo("1")));
  }

  @Test
  void testSetRandomFalse() {
    when(playerProperties.getRandom()).thenCallRealMethod();
    mpdPlayer.setRandom(false);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getRandom()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(("0"))));
  }

  @Test
  void testIsRandom() {
    when(serverStatus.isRandom()).thenReturn(true);
    assertThat(mpdPlayer.isRandom(), is(true));
  }

  @Test
  void testSetRepeatTrue() {
    when(playerProperties.getRepeat()).thenCallRealMethod();
    mpdPlayer.setRepeat(true);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getRepeat()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(("1"))));
  }

  @Test
  void testSetRepeatFalse() {
    when(playerProperties.getRepeat()).thenCallRealMethod();
    mpdPlayer.setRepeat(false);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getRepeat()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(("0"))));
  }

  @Test
  void testIsRepeat() {
    when(serverStatus.isRepeat()).thenReturn(true);
    assertThat(mpdPlayer.isRepeat(), is(true));
  }

  @Test
  void testGetBitrate() {
    when(serverStatus.getBitrate()).thenReturn(1);
    assertThat(mpdPlayer.getBitrate(), is(equalTo(1)));
  }

  @Test
  void testGetVolume() {
    when(serverStatus.getVolume()).thenReturn(1);
    assertThat(mpdPlayer.getVolume(), is(equalTo(1)));
  }

  @Test
  void testSetVolume() {
    when(playerProperties.getSetVolume()).thenCallRealMethod();
    mpdPlayer.setVolume(0);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getSetVolume()))));
    assertThat(integerArgumentCaptor.getValue(), is(equalTo(0)));
  }

  @Test
  void testSetVolumeOutOfRangeHigh() {
    mpdPlayer.setVolume(101);
    verify(commandExecutor, never())
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
  }

  @Test
  void testSetVolumeOutOfRangeLow() {
    mpdPlayer.setVolume(-1);
    verify(commandExecutor, never())
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
  }

  @Test
  void testGetXFade() {
    when(serverStatus.getXFade()).thenReturn(1);
    assertThat(mpdPlayer.getXFade(), is(equalTo(1)));
  }

  @Test
  void testSetXFade() {
    when(playerProperties.getXFade()).thenCallRealMethod();
    mpdPlayer.setXFade(5);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(stringArgumentCaptor.getValue(), is(equalTo((playerProperties.getXFade()))));
    assertThat(integerArgumentCaptor.getValue(), is(equalTo(5)));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testSetSingle(boolean single) {
    when(playerProperties.getSingle()).thenCallRealMethod();
    mpdPlayer.setSingle(single);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getSingle()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(single ? "1" : "0")));
  }

  @ParameterizedTest
  @ValueSource(booleans = {true, false})
  void testSetConsume(boolean consume) {
    when(playerProperties.getConsume()).thenCallRealMethod();
    mpdPlayer.setConsume(consume);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getConsume()))));
    assertThat(stringArgumentCaptor.getAllValues().get(1), is(equalTo(consume ? "1" : "0")));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, -5})
  void testSetMixRampDb(int db) {
    when(playerProperties.getMixRampDb()).thenCallRealMethod();
    mpdPlayer.setMixRampDb(db);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0), is(equalTo((playerProperties.getMixRampDb()))));
    assertThat(integerArgumentCaptor.getValue(), is(equalTo(db)));
  }

  @ParameterizedTest
  @ValueSource(ints = {5, 0, -5})
  void testSetMixRampDelay(int delay) {
    when(playerProperties.getMixRampDelay()).thenCallRealMethod();
    mpdPlayer.setMixRampDelay(delay);
    verify(commandExecutor)
        .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
    assertThat(
        stringArgumentCaptor.getAllValues().get(0),
        is(equalTo((playerProperties.getMixRampDelay()))));
    assertThat(
        stringArgumentCaptor.getAllValues().get(1),
        is(equalTo(delay < 0 ? "nan" : Integer.toString(delay))));
  }

  @Test
  void testGetElapsedTime() {
    when(serverStatus.getElapsedTime()).thenReturn(1L);
    assertThat(mpdPlayer.getElapsedTime(), is(equalTo(1L)));
  }

  @Test
  void testGetTotalTime() {
    when(serverStatus.getTotalTime()).thenReturn(1L);
    assertThat(mpdPlayer.getTotalTime(), is(equalTo(1L)));
  }

  @Test
  void testGetStatusPlaying() {
    when(serverStatus.getState()).thenReturn(Player.Status.STATUS_PLAYING.getPrefix());
    assertThat(mpdPlayer.getStatus(), is(equalTo((Player.Status.STATUS_PLAYING))));
  }

  @Test
  void testGetStatusPaused() {
    when(serverStatus.getState()).thenReturn(Player.Status.STATUS_PAUSED.getPrefix());
    assertThat(mpdPlayer.getStatus(), is(equalTo((Player.Status.STATUS_PAUSED))));
  }

  @Test
  void testGetStatusStopped() {
    when(serverStatus.getState()).thenReturn(Player.Status.STATUS_STOPPED.getPrefix());
    assertThat(mpdPlayer.getStatus(), is(equalTo((Player.Status.STATUS_STOPPED))));
  }

  @Test
  void testGetAudioDetailsBitrate() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertThat(mpdPlayer.getAudioDetails().getBits(), is(equalTo(5)));
  }

  @Test
  void testGetAudioDetailsBadBitrate() {
    when(serverStatus.getAudio()).thenReturn("4:bad:6");
    assertThat(mpdPlayer.getAudioDetails().getBits(), is(equalTo(-1)));
  }

  @Test
  void testGetAudioDetailsChannels() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertThat(mpdPlayer.getAudioDetails().getChannels(), is(equalTo(6)));
  }

  @Test
  void testGetAudioDetailsBadChannels() {
    when(serverStatus.getAudio()).thenReturn("4:5:bad");
    assertThat(mpdPlayer.getAudioDetails().getChannels(), is(equalTo(-1)));
  }

  @Test
  void testGetAudioDetailsSampleRate() {
    when(serverStatus.getAudio()).thenReturn("4:5:6");
    assertThat(mpdPlayer.getAudioDetails().getSampleRate(), is(equalTo(4)));
  }

  @Test
  void testGetAudioDetailsBadSampleRate() {
    when(serverStatus.getAudio()).thenReturn("bad:5:6");
    assertThat(mpdPlayer.getAudioDetails().getSampleRate(), is(equalTo(-1)));
  }

  @Test
  void testGetMixRampDb() {
    when(serverStatus.getMixRampDb()).thenReturn(Optional.of(1));
    mpdPlayer
        .getMixRampDb()
        .ifPresentOrElse(d -> assertThat(d, is(equalTo(1))), () -> fail("Ramp db is empty"));
  }

  @Test
  void testGetMixRampDbEmpty() {
    when(serverStatus.getMixRampDb()).thenReturn(Optional.empty());
    assertThat(mpdPlayer.getMixRampDb(), is(equalTo(Optional.empty())));
  }

  @Test
  void testGetMixRampDelay() {
    when(serverStatus.getMixRampDelay()).thenReturn(Optional.of(1));
    mpdPlayer
        .getMixRampDelay()
        .ifPresentOrElse(d -> assertThat(d, is(equalTo(1))), () -> fail("No delay"));
  }

  @Test
  void testGetMixRampDelayEmpty() {
    when(serverStatus.getMixRampDelay()).thenReturn(Optional.empty());
    assertThat(mpdPlayer.getMixRampDelay(), is(equalTo(Optional.empty())));
  }
}
