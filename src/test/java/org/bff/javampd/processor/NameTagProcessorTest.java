package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class NameTagProcessorTest {

  @Test
  public void testProcessSong() {
    String testName = "testName";

    NameTagProcessor nameTagProcessor = new NameTagProcessor();
    MPDSong song = new MPDSong("testFile", "testTitle");

    String line = "Name:" + testName;
    nameTagProcessor.processTag(song, line);

    assertEquals(testName, song.getName());
  }

  @Test
  public void testProcessSongNoName() {
    String testTitle = "testTitle";

    NameTagProcessor nameTagProcessor = new NameTagProcessor();
    MPDSong song = new MPDSong("testFile", testTitle);

    String line = "";
    nameTagProcessor.processTag(song, line);

    assertEquals(testTitle, song.getName());
  }

  @Test
  public void testProcessSongBadLine() {
    String testName = "testName";

    TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
    MPDSong song = new MPDSong("testFile", null);

    String line = "BadName:" + testName;
    titleTagProcessor.processTag(song, line);

    assertNull(song.getName());
  }
}
