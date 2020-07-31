package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class PositionTagProcessorTest {

  @Test
  public void testProcessSong() {
    int testPosition = 1;

    PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
    MPDSong song = new MPDSong("testFile", "title");

    String line = "Pos:" + testPosition;
    positionTagProcessor.processTag(song, line);

    assertEquals(testPosition, song.getPosition());
  }

  @Test
  public void testProcessSongBadLine() {
    int testPosition = 1;

    PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
    MPDSong song = new MPDSong("testFile", "title");

    String line = "BadPos:" + testPosition;
    positionTagProcessor.processTag(song, line);

    assertEquals(-1, song.getId());
  }
}
