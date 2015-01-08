package org.bff.javampd.statistics;

import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDServerStatisticsTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @InjectMocks
    private MPDServerStatistics serverStatistics;

    private List<String> statList;

    @Before
    public void setUp() throws Exception {
        statList = new ArrayList<>();
        when(properties.getStats()).thenReturn(new ServerProperties().getStats());
    }

    @Test
    public void testGetPlaytime() throws Exception {
        String playTime = "5";
        statList.add("playtime: " + playTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5L, serverStatistics.getPlaytime());
    }

    @Test
    public void testGetUptime() throws Exception {
        String uptime = "5";
        statList.add("uptime: " + uptime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5L, serverStatistics.getUptime());
    }

    @Test
    public void testGetAlbumCount() throws Exception {
        String albums = "5";
        statList.add("albums: " + albums);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5, serverStatistics.getAlbumCount());
    }

    @Test
    public void testGetArtistCount() throws Exception {
        String artists = "5";
        statList.add("artists: " + artists);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5, serverStatistics.getArtistCount());
    }

    @Test
    public void testGetSongCount() throws Exception {
        String songs = "5";
        statList.add("songs: " + songs);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5, serverStatistics.getSongCount());
    }

    @Test
    public void testGetDatabasePlaytime() throws Exception {
        String playtime = "5";
        statList.add("db_playtime: " + playtime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5L, serverStatistics.getDatabasePlaytime());
    }

    @Test
    public void testGetLastUpdateTime() throws Exception {
        String updateTime = "5";
        statList.add("db_update: " + updateTime);
        when(commandExecutor.sendCommand(properties.getStats())).thenReturn(statList);

        assertEquals(5L, serverStatistics.getLastUpdateTime());
    }
}