package org.bff.javampd.database;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.bff.javampd.server.MPD;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MPDMusicDatabaseTest {

  private MPD mpd;

  @BeforeEach
  void before() {
    mpd = MPD.builder().build();
  }

  @Test
  void testGetArtistDatabase() {
    assertNotNull(mpd.getMusicDatabase().getArtistDatabase());
  }

  @Test
  void testGetAlbumDatabase() {
    assertNotNull(mpd.getMusicDatabase().getAlbumDatabase());
  }

  @Test
  void testGetGenreDatabase() {
    assertNotNull(mpd.getMusicDatabase().getGenreDatabase());
  }

  @Test
  void testGetPlaylistDatabase() {
    assertNotNull(mpd.getMusicDatabase().getPlaylistDatabase());
  }

  @Test
  void testGetFileDatabase() {
    assertNotNull(mpd.getMusicDatabase().getFileDatabase());
  }

  @Test
  void testGetDateDatabase() {
    assertNotNull(mpd.getMusicDatabase().getDateDatabase());
  }

  @Test
  void testGetSongDatabase() {
    assertNotNull(mpd.getMusicDatabase().getSongDatabase());
  }
}
