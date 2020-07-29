package org.bff.javampd.database;

import org.bff.javampd.server.MPD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class MPDMusicDatabaseTest {

    private MPD mpd;

    @BeforeEach
    public void before() {
        mpd = new MPD.Builder().build();
    }

    @Test
    public void testGetArtistDatabase() {
        assertNotNull(mpd.getMusicDatabase().getArtistDatabase());
    }

    @Test
    public void testGetAlbumDatabase() {
        assertNotNull(mpd.getMusicDatabase().getAlbumDatabase());
    }

    @Test
    public void testGetGenreDatabase() {
        assertNotNull(mpd.getMusicDatabase().getGenreDatabase());
    }

    @Test
    public void testGetPlaylistDatabase() {
        assertNotNull(mpd.getMusicDatabase().getPlaylistDatabase());
    }

    @Test
    public void testGetFileDatabase() {
        assertNotNull(mpd.getMusicDatabase().getFileDatabase());
    }

    @Test
    public void testGetDateDatabase() {
        assertNotNull(mpd.getMusicDatabase().getDateDatabase());
    }

    @Test
    public void testGetSongDatabase() {
        assertNotNull(mpd.getMusicDatabase().getSongDatabase());
    }
}