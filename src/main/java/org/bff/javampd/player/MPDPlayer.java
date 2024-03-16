package org.bff.javampd.player;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.playlist.MPDPlaylistSong;
import org.bff.javampd.playlist.PlaylistSongConverter;
import org.bff.javampd.server.ServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MPDPlayer represents a player controller to a MPD server. To obtain an instance of the class you
 * must use the <code>getMPDPlayer</code> method from the {@link org.bff.javampd.server.MPD}
 * connection class. This class does not have a public constructor (singleton model) so the object
 * must be obtained from the connection object.
 *
 * @author Bill
 */
public class MPDPlayer implements Player {
  private static final Logger LOGGER = LoggerFactory.getLogger(MPDPlayer.class);

  private int oldVolume;
  private final List<PlayerChangeListener> listeners = new ArrayList<>();
  private final VolumeChangeDelegate volumeChangeDelegate;

  private Status status = Status.STATUS_STOPPED;
  private final ServerStatus serverStatus;
  private final PlayerProperties playerProperties;
  private final CommandExecutor commandExecutor;
  private final PlaylistSongConverter playlistSongConverter;

  @Inject
  public MPDPlayer(
      ServerStatus serverStatus,
      PlayerProperties playerProperties,
      CommandExecutor commandExecutor,
      PlaylistSongConverter playlistSongConverter) {
    this.serverStatus = serverStatus;
    this.playerProperties = playerProperties;
    this.commandExecutor = commandExecutor;
    this.playlistSongConverter = playlistSongConverter;
    this.volumeChangeDelegate = new VolumeChangeDelegate();
  }

  @Override
  public Optional<MPDPlaylistSong> getCurrentSong() {
    List<MPDPlaylistSong> songList =
        playlistSongConverter.convertResponseToSongs(
            commandExecutor.sendCommand(playerProperties.getCurrentSong()));

    if (songList.isEmpty()) {
      return Optional.empty();
    } else {
      return Optional.ofNullable(songList.get(0));
    }
  }

  @Override
  public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
    listeners.add(pcl);
  }

  @Override
  public synchronized void removePlayerChangedListener(PlayerChangeListener pcl) {
    listeners.remove(pcl);
  }

  /**
   * Sends the appropriate {@link PlayerChangeEvent} to all registered {@link
   * PlayerChangeListener}s.
   *
   * @param event the {@link PlayerChangeEvent.Event} to send
   */
  protected synchronized void firePlayerChangeEvent(PlayerChangeEvent.Event event) {
    var pce = new PlayerChangeEvent(this, event);

    for (PlayerChangeListener pcl : listeners) {
      pcl.playerChanged(pce);
    }
  }

  @Override
  public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
    volumeChangeDelegate.addVolumeChangeListener(vcl);
  }

  @Override
  public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
    volumeChangeDelegate.removeVolumeChangedListener(vcl);
  }

  /**
   * Sends the appropriate {@link VolumeChangeEvent} to all registered {@link VolumeChangeListener}.
   *
   * @param volume the new volume
   */
  protected synchronized void fireVolumeChangeEvent(int volume) {
    volumeChangeDelegate.fireVolumeChangeEvent(this, volume);
  }

  @Override
  public void play() {
    playSong(null);
  }

  @Override
  public void playSong(MPDPlaylistSong song) {
    if (song == null) {
      commandExecutor.sendCommand(playerProperties.getPlay());
    } else {
      commandExecutor.sendCommand(playerProperties.getPlayId(), song.getId());
    }

    if (status == Status.STATUS_STOPPED || status == Status.STATUS_PAUSED) {
      status = Status.STATUS_PLAYING;
      firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STARTED);
    } else {
      firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SONG_SET);
    }
  }

  @Override
  public void seek(long secs) {
    seekSong(null, secs);
  }

  @Override
  public void seekSong(MPDPlaylistSong song, long secs) {
    if (song == null) {
      getCurrentSong()
          .ifPresent(
              s -> {
                if (s.getLength() > secs) {
                  seekMPDSong(s, secs);
                }
              });

    } else {
      if (song.getLength() >= secs) {
        seekMPDSong(song, secs);
      }
    }
  }

  @Override
  public void stop() {
    commandExecutor.sendCommand(playerProperties.getStop());
    status = Status.STATUS_STOPPED;
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_STOPPED);
  }

  @Override
  public void pause() {
    commandExecutor.sendCommand(playerProperties.getPause());
    status = Status.STATUS_PAUSED;
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PAUSED);
  }

  @Override
  public void playNext() {
    commandExecutor.sendCommand(playerProperties.getNext());
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
  }

  @Override
  public void playPrevious() {
    commandExecutor.sendCommand(playerProperties.getPrevious());
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_PREVIOUS);
  }

  @Override
  public void mute() {
    oldVolume = getVolume();
    setVolume(0);
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_MUTED);
  }

  @Override
  public void unMute() {
    setVolume(oldVolume);
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_UNMUTED);
  }

  @Override
  public int getBitrate() {
    return serverStatus.getBitrate();
  }

  @Override
  public int getVolume() {
    return serverStatus.getVolume();
  }

  @Override
  public void setVolume(int volume) {
    if (volume < 0 || volume > 100) {
      LOGGER.warn("Not changing volume to {}", volume);
    } else {
      commandExecutor.sendCommand(playerProperties.getSetVolume(), volume);
      fireVolumeChangeEvent(volume);
    }
  }

  @Override
  public boolean isRepeat() {
    return serverStatus.isRepeat();
  }

  @Override
  public void setRepeat(boolean shouldRepeat) {
    String repeat;
    if (shouldRepeat) {
      repeat = "1";
    } else {
      repeat = "0";
    }
    commandExecutor.sendCommand(playerProperties.getRepeat(), repeat);
  }

  @Override
  public boolean isRandom() {
    return serverStatus.isRandom();
  }

  @Override
  public void setRandom(boolean shouldRandom) {
    String random;
    if (shouldRandom) {
      random = "1";
    } else {
      random = "0";
    }
    commandExecutor.sendCommand(playerProperties.getRandom(), random);
  }

  @Override
  public void randomizePlay() {
    setRandom(true);
  }

  @Override
  public void unRandomizePlay() {
    setRandom(false);
  }

  @Override
  public int getXFade() {
    return serverStatus.getXFade();
  }

  @Override
  public void setXFade(int xFade) {
    commandExecutor.sendCommand(playerProperties.getXFade(), xFade);
  }

  @Override
  public long getElapsedTime() {
    return serverStatus.getElapsedTime();
  }

  @Override
  public long getTotalTime() {
    return serverStatus.getTotalTime();
  }

  @Override
  public void setConsume(boolean consume) {
    commandExecutor.sendCommand(playerProperties.getConsume(), consume ? "1" : "0");
  }

  @Override
  public void setSingle(boolean single) {
    commandExecutor.sendCommand(playerProperties.getSingle(), single ? "1" : "0");
  }

  @Override
  public Optional<Integer> getMixRampDb() {
    return this.serverStatus.getMixRampDb();
  }

  @Override
  public void setMixRampDb(int db) {
    commandExecutor.sendCommand(playerProperties.getMixRampDb(), db);
  }

  @Override
  public Optional<Integer> getMixRampDelay() {
    return this.serverStatus.getMixRampDelay();
  }

  @Override
  public void setMixRampDelay(int delay) {
    commandExecutor.sendCommand(
        playerProperties.getMixRampDelay(), delay < 0 ? "nan" : Integer.toString(delay));
  }

  @Override
  public MPDAudioInfo getAudioDetails() {
    MPDAudioInfo info = null;

    String response = serverStatus.getAudio();
    if (!"".equals(response)) {
      String[] split = response.split(":");
      info =
          MPDAudioInfo.builder()
              .sampleRate(parseSampleRate(split[0]))
              .bits(parseBitRate(split[1]))
              .channels(parseChannels(split[2]))
              .build();
    }

    return info;
  }

  private static int parseSampleRate(String sampleRate) {
    try {
      return Integer.parseInt(sampleRate);
    } catch (NumberFormatException nfe) {
      LOGGER.error("Could not format sample rate", nfe);
      return -1;
    }
  }

  @Override
  public Status getStatus() {
    String currentStatus = serverStatus.getState();

    if (currentStatus.equalsIgnoreCase(Status.STATUS_PLAYING.getPrefix())) {
      return Status.STATUS_PLAYING;
    } else if (currentStatus.equalsIgnoreCase(Status.STATUS_PAUSED.getPrefix())) {
      return Status.STATUS_PAUSED;
    } else {
      return Status.STATUS_STOPPED;
    }
  }

  private static int parseChannels(String channels) {
    try {
      return Integer.parseInt(channels);
    } catch (NumberFormatException nfe) {
      LOGGER.error("Could not format channels", nfe);
      return -1;
    }
  }

  private static int parseBitRate(String bitRate) {
    try {
      return Integer.parseInt(bitRate);
    } catch (NumberFormatException nfe) {
      LOGGER.error("Could not format bits", nfe);
      return -1;
    }
  }

  private void seekMPDSong(MPDPlaylistSong song, long secs) {
    var params = new String[2];
    params[1] = Long.toString(secs);
    params[0] = Integer.toString(song.getId());
    commandExecutor.sendCommand(playerProperties.getSeekId(), params);
    firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_SEEKING);
  }
}
