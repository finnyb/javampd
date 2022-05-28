package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class AlbumArtistTagProcessorTest {

  @Test
  void testProcess() {
    var testArtist = "testArtist";
    var line = "AlbumArtist:" + testArtist;

    assertEquals(testArtist, new AlbumArtistTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new AlbumArtistTagProcessor().processTag("BadArtist: testArtist"));
  }
}
