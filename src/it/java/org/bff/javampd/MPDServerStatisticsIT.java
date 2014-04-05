package org.bff.javampd;

import org.bff.javampd.integrationdata.Albums;
import org.bff.javampd.integrationdata.Artists;
import org.bff.javampd.integrationdata.Songs;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDServerStatisticsIT extends BaseTest {

    @Test
    public void testGetPlaytime() throws Exception {
        assertTrue(getServerStatistics().getPlaytime() > 0);
    }

    @Test
    public void testGetUptime() throws Exception {
        assertTrue(getServerStatistics().getUptime() > 0);
    }

    @Test
    public void testGetAlbums() throws Exception {
        assertEquals(Albums.albums.size(), getServerStatistics().getAlbums());
    }

    @Test
    public void testGetArtists() throws Exception {
        assertEquals(Artists.artists.size(), getServerStatistics().getArtists());
    }

    @Test
    public void testGetSongs() throws Exception {
        assertEquals(Songs.songs.size(), getServerStatistics().getSongs());
    }

    @Test
    public void testGetDatabasePlaytime() throws Exception {
        assertTrue(getServerStatistics().getDatabasePlaytime() > 0);
    }

    @Test
    public void testGetDatabaseUpdateTime() throws Exception {
        assertTrue(getServerStatistics().getDatabaseUpdateTime() > 0);
    }
}
