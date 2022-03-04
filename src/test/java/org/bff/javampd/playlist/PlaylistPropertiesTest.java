package org.bff.javampd.playlist;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlaylistPropertiesTest {
  private PlaylistProperties playlistProperties;

  @BeforeEach
  void setUp() {
    playlistProperties = new PlaylistProperties();
  }

  @Test
  void getAdd() {
    assertEquals("add", playlistProperties.getAdd());
  }

  @Test
  void getClear() {
    assertEquals("clear", playlistProperties.getClear());
  }

  @Test
  void getCurrentSong() {
    assertEquals("currentsong", playlistProperties.getCurrentSong());
  }

  @Test
  void getDelete() {
    assertEquals("rm", playlistProperties.getDelete());
  }

  @Test
  void getChanges() {
    assertEquals("plchanges", playlistProperties.getChanges());
  }

  @Test
  void getId() {
    assertEquals("playlistid", playlistProperties.getId());
  }

  @Test
  void getInfo() {
    assertEquals("playlistinfo", playlistProperties.getInfo());
  }

  @Test
  void getLoad() {
    assertEquals("load", playlistProperties.getLoad());
  }

  @Test
  void getMove() {
    assertEquals("move", playlistProperties.getMove());
  }

  @Test
  void getMoveId() {
    assertEquals("moveid", playlistProperties.getMoveId());
  }

  @Test
  void getRemove() {
    assertEquals("delete", playlistProperties.getRemove());
  }

  @Test
  void getRemoveId() {
    assertEquals("deleteid", playlistProperties.getRemoveId());
  }

  @Test
  void getSave() {
    assertEquals("save", playlistProperties.getSave());
  }

  @Test
  void getShuffle() {
    assertEquals("shuffle", playlistProperties.getShuffle());
  }

  @Test
  void getSwap() {
    assertEquals("swap", playlistProperties.getSwap());
  }

  @Test
  void getSwapId() {
    assertEquals("swapid", playlistProperties.getSwapId());
  }
}
