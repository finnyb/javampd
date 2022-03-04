package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class GenreTagProcessorTest {

  @Test
  void testProcess() {
    String testGenre = "testGenre";
    String line = "Genre:" + testGenre;

    assertEquals(testGenre, new GenreTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new GenreTagProcessor().processTag("BadGenre: test"));
  }
}
