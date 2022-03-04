package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class CommentTagProcessorTest {

  @Test
  void testProcess() {
    var testComment = "testComment";
    var line = "Comment:" + testComment;

    assertEquals(testComment, new CommentTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new CommentTagProcessor().processTag("BadComment: test"));
  }
}
