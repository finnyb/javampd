package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AlbumTagProcessorTest {

  @Test
  void testProcess() {
    String testAlbum = "testAlbum";
    String line = "Album:" + testAlbum;

    assertEquals(testAlbum, new AlbumTagProcessor().processTag(line));
  }

  @Test
  void testProcessSongBadLine() {
    assertNull(new AlbumTagProcessor().processTag("BadAlbum: test"));
  }
}
