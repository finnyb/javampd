package org.bff.javampd.statistics;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.Clock;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDServerStatisticsTest {
  @Mock
  private MPDCommandExecutor commandExecutor;

  @Mock
  private ServerProperties properties;

  @Mock
  private Clock clock;

  private MPDServerStatistics serverStatistics;

  private List<String> statList;

  @BeforeEach
  public void setUp() {
    when(clock.min()).thenReturn(LocalDateTime.MIN);
    serverStatistics =
      new MPDServerStatistics(properties, commandExecutor, clock);
    statList = new ArrayList<>();

    when(properties.getStats()).thenReturn(new ServerProperties().getStats());
    when(clock.now()).thenReturn(LocalDateTime.now());
  }

  @Test
  public void testGetPlaytime() {
    String playTime = "5";
    statList.add("playtime: " + playTime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(Long.parseLong(playTime), serverStatistics.getPlaytime());
  }

  @Test
  public void testGetPlaytimeParseException() {
    String playTime = "junk";
    statList.add("playtime: " + playTime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(0, serverStatistics.getPlaytime());
  }

  @Test
  public void testGetUptime() {
    String uptime = "5";
    statList.add("uptime: " + uptime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(Long.parseLong(uptime), serverStatistics.getUptime());
  }

  @Test
  public void testGetAlbumCount() {
    String albums = "5";
    statList.add("albums: " + albums);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(Integer.parseInt(albums), serverStatistics.getAlbumCount());
  }

  @Test
  public void testGetArtistCount() {
    String artists = "5";
    statList.add("artists: " + artists);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(Integer.parseInt(artists), serverStatistics.getArtistCount());
  }

  @Test
  public void testGetSongCount() {
    String songs = "5";
    statList.add("songs: " + songs);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(Integer.parseInt(songs), serverStatistics.getSongCount());
  }

  @Test
  public void testGetDatabasePlaytime() {
    String playtime = "5";
    statList.add("db_playtime: " + playtime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(
      Long.parseLong(playtime),
      serverStatistics.getDatabasePlaytime()
    );
  }

  @Test
  public void testGetLastUpdateTime() {
    String updateTime = "5";
    statList.add("db_update: " + updateTime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(
      Long.parseLong(updateTime),
      serverStatistics.getLastUpdateTime()
    );
  }

  @Test
  public void testNonexistantStat() {
    String updateTime = "5";
    statList.add("fake: " + updateTime);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);

    assertEquals(0, serverStatistics.getLastUpdateTime());
  }

  @Test
  public void testInsideDefaultExpiry() {
    String songs = "5";
    statList.add("songs: " + songs);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);
    when(clock.now()).thenReturn(LocalDateTime.now());
    when(clock.now()).thenReturn(LocalDateTime.MIN.plusMinutes(5));
    serverStatistics.getSongCount();
    serverStatistics.getSongCount();
    Mockito
      .verify(commandExecutor, times(1))
      .sendCommand(properties.getStats());
  }

  @Test
  public void testOutsideDefaultExpiry() throws InterruptedException {
    String songs = "5";
    statList.add("songs: " + songs);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);
    serverStatistics.getSongCount();
    when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(5));
    serverStatistics.getSongCount();
    Mockito
      .verify(commandExecutor, times(2))
      .sendCommand(properties.getStats());
  }

  @Test
  public void testSetExpiry() throws InterruptedException {
    int interval = 1;
    serverStatistics.setExpiryInterval(interval);
    String songs = "5";
    statList.add("songs: " + songs);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);
    serverStatistics.getSongCount();
    when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(interval * 2));
    serverStatistics.getSongCount();
    Mockito
      .verify(commandExecutor, times(2))
      .sendCommand(properties.getStats());
  }

  @Test
  public void testForceUpdate() {
    String songs = "5";
    statList.add("songs: " + songs);
    when(commandExecutor.sendCommand(properties.getStats()))
      .thenReturn(statList);
    serverStatistics.getSongCount();
    serverStatistics.forceUpdate();
    serverStatistics.getSongCount();
    Mockito
      .verify(commandExecutor, times(2))
      .sendCommand(properties.getStats());
  }
}
