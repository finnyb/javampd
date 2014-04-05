package org.bff.javampd;

import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;
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
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private ServerProperties serverProperties;

    @InjectMocks
    private MPDServerStatistics serverStatistics;

    private static final int ALBUM_COUNT = 3;
    private static final int ARTIST_COUNT = 4;
    private static final int SONG_COUNT = 31;
    private static final int UPTIME = 1245;
    private static final int PLAYTIME = 134;
    private static final int DB_PLAYTIME = 155;
    private static final int DB_UPDATE = 1385665150;

    @Before
    public void setUp() throws MPDResponseException {
        when(serverProperties.getStats()).thenReturn(new ServerProperties().getStats());
        when(mpdCommandExecutor.sendCommand(serverProperties.getStats())).thenReturn(getResponse());
    }

    @Test
    public void testGetPlaytime() throws Exception {
        assertEquals(PLAYTIME, serverStatistics.getPlaytime());
    }

    @Test
    public void testGetUptime() throws Exception {
        assertEquals(UPTIME, serverStatistics.getUptime());
    }

    @Test
    public void testGetAlbums() throws Exception {
        assertEquals(ALBUM_COUNT, serverStatistics.getAlbums());
    }

    @Test
    public void testGetArtists() throws Exception {
        assertEquals(ARTIST_COUNT, serverStatistics.getArtists());
    }

    @Test
    public void testGetSongs() throws Exception {
        assertEquals(SONG_COUNT, serverStatistics.getSongs());
    }

    @Test
    public void testGetDatabasePlaytime() throws Exception {
        assertEquals(DB_PLAYTIME, serverStatistics.getDatabasePlaytime());
    }

    @Test
    public void testGetDatabaseUpdateTime() throws Exception {
        assertEquals(DB_UPDATE, serverStatistics.getDatabaseUpdateTime());
    }

    private List<String> getResponse() {
        List<String> response = new ArrayList<>();

        response.add("artists: " + ARTIST_COUNT);
        response.add("albums: " + ALBUM_COUNT);
        response.add("songs: " + SONG_COUNT);
        response.add("uptime: " + UPTIME);
        response.add("playtime: " + PLAYTIME);
        response.add("db_playtime: " + DB_PLAYTIME);
        response.add("db_update: " + DB_UPDATE);

        return response;
    }
}
