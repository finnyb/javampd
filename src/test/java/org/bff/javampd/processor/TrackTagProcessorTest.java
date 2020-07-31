package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class TrackTagProcessorTest {

  @Test
  public void testProcessSong() {
    String testTrack = "2/10";

    TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
    MPDSong song = new MPDSong("testFile", "title");

    String line = "Track:" + testTrack;
    trackTagProcessor.processTag(song, line);

    assertEquals(2, song.getTrack());
  }

  @Test
  public void testProcessSongBadLine() {
    String testTrack = "2/10";

    TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
    MPDSong song = new MPDSong("testFile", "title");

    String line = "BadTrack:" + testTrack;
    trackTagProcessor.processTag(song, line);

    assertEquals(0, song.getTrack());
  }

  @Test
  public void testProcessSongUnparseable() {
    String testTrack = "junk";

    TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
    MPDSong song = new MPDSong("testFile", "title");

    String line = "Track:" + testTrack;
    trackTagProcessor.processTag(song, line);

    assertEquals(0, song.getTrack());
  }
}
