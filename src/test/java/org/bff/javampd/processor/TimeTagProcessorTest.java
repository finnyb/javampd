package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class TimeTagProcessorTest {

  @Test
  public void testProcessSong() {
    int testLength = 1;

    TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
    MPDSong song = new MPDSong("testFile", "testName");

    String line = "Time:" + testLength;
    timeTagProcessor.processTag(song, line);

    assertEquals(testLength, song.getLength());
  }

  @Test
  public void testProcessSongBadLine() {
    int testLength = 1;

    TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
    MPDSong song = new MPDSong("testFile", "testName");

    String line = "BadTime:" + testLength;
    timeTagProcessor.processTag(song, line);

    assertEquals(0, song.getLength());
  }
}
