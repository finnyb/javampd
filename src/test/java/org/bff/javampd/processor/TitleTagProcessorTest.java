package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class TitleTagProcessorTest {

  @Test
  public void testProcessSong() {
    String testTitle = "testTitle";

    TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
    MPDSong song = new MPDSong("testFile", "testName");

    String line = "Title:" + testTitle;
    titleTagProcessor.processTag(song, line);

    assertEquals(testTitle, song.getTitle());
  }

  @Test
  public void testProcessSongBadLine() {
    String testTitle = "testTitle";

    TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
    MPDSong song = new MPDSong("testFile", null);

    String line = "BadTitle:" + testTitle;
    titleTagProcessor.processTag(song, line);

    assertNull(song.getTitle());
  }
}
