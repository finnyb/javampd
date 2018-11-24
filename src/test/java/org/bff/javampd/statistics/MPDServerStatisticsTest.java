package org.bff.javampd.statistics;

import org.bff.javampd.Clock;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDServerStatisticsTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @Mock
    private Clock clock;

    private MPDServerStatistics serverStatistics;

    private List<String> statList;

    @Before
    public void setUp() throws Exception {
        when(clock.min()).thenReturn(LocalDateTime.fromDateFields(new Date(0)));
        serverStatistics = new MPDServerStatistics(properties, commandExecutor, clock);
        statList = new ArrayList<>();

        when(properties.getStats()).thenReturn(new ServerProperties().getStats());
        when(clock.now()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void testGetPlaytime() throws Exception {
        String playTime = "5";
        statList.add("playtime: " + playTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Long.parseLong(playTime), serverStatistics.getPlaytime());
    }

    @Test
    public void testGetPlaytimeParseException() throws Exception {
        String playTime = "junk";
        statList.add("playtime: " + playTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(0, serverStatistics.getPlaytime());
    }

    @Test
    public void testGetUptime() throws Exception {
        String uptime = "5";
        statList.add("uptime: " + uptime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Long.parseLong(uptime), serverStatistics.getUptime());
    }

    @Test
    public void testGetAlbumCount() throws Exception {
        String albums = "5";
        statList.add("albums: " + albums);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Integer.parseInt(albums), serverStatistics.getAlbumCount());
    }

    @Test
    public void testGetArtistCount() throws Exception {
        String artists = "5";
        statList.add("artists: " + artists);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Integer.parseInt(artists), serverStatistics.getArtistCount());
    }

    @Test
    public void testGetSongCount() throws Exception {
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Integer.parseInt(songs), serverStatistics.getSongCount());
    }

    @Test
    public void testGetDatabasePlaytime() throws Exception {
        String playtime = "5";
        statList.add("db_playtime: " + playtime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Long.parseLong(playtime), serverStatistics.getDatabasePlaytime());
    }

    @Test
    public void testGetLastUpdateTime() throws Exception {
        String updateTime = "5";
        statList.add("db_update: " + updateTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(Long.parseLong(updateTime), serverStatistics.getLastUpdateTime());
    }

    @Test
    public void testNonexistantStat() throws Exception {
        String updateTime = "5";
        statList.add("fake: " + updateTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(0, serverStatistics.getLastUpdateTime());
    }

    @Test
    public void testInsideDefaultExpiry() {
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        when(clock.now()).thenReturn(LocalDateTime.fromDateFields(new Date(0)).plusMinutes(5));
        serverStatistics.getSongCount();
        serverStatistics.getSongCount();
        Mockito.verify(commandExecutor, times(1)).sendCommand(properties.getStats());
    }

    @Test
    public void testOutsideDefaultExpiry() throws InterruptedException {
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);
        serverStatistics.getSongCount();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(5));
        serverStatistics.getSongCount();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStats());
    }

    @Test
    public void testSetExpiry() throws InterruptedException {
        int interval = 1;
        serverStatistics.setExpiryInterval(interval);
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);
        serverStatistics.getSongCount();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(interval * 2));
        serverStatistics.getSongCount();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStats());
    }

    @Test
    public void testForceUpdate() {
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);
        serverStatistics.getSongCount();
        serverStatistics.forceUpdate();
        serverStatistics.getSongCount();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStats());
    }
}