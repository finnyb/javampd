package org.bff.javampd.database;

import org.bff.javampd.server.MPD;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MPDMusicDatabaseTest {

    private MPD mpd;

    @Before
    public void before() {
        mpd = new MPD.Builder().build();
    }

    @Test
    public void testGetArtistDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getArtistDatabase());
    }

    @Test
    public void testGetAlbumDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getAlbumDatabase());
    }

    @Test
    public void testGetGenreDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getGenreDatabase());
    }

    @Test
    public void testGetPlaylistDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getPlaylistDatabase());
    }

    @Test
    public void testGetFileDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getFileDatabase());
    }

    @Test
    public void testGetDateDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getDateDatabase());
    }

    @Test
    public void testGetSongDatabase() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getSongDatabase());
    }

    @Test
    public void testGetSongSearcher() throws Exception {
        assertNotNull(mpd.getMusicDatabase().getSongSearcher());
    }
}