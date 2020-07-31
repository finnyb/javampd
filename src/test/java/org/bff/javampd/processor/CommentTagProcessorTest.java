package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

public class CommentTagProcessorTest {

  @Test
  public void testProcessSong() {
    String testComment = "testComment";

    CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
    MPDSong song = new MPDSong("testFile", "testName");

    String line = "Comment:" + testComment;
    commentTagProcessor.processTag(song, line);

    assertEquals(testComment, song.getComment());
  }

  @Test
  public void testProcessSongBadLine() {
    String testComment = "testComment";

    CommentTagProcessor commentTagProcessor = new CommentTagProcessor();
    MPDSong song = new MPDSong("testFile", "testName");

    String line = "BadComment:" + testComment;
    commentTagProcessor.processTag(song, line);

    assertNull(song.getComment());
  }
}
