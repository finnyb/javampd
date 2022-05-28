package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DiscTagProcessorTest {

  @Test
  void testProcess() {
    var testDisc = "testDisc";
    var line = "Disc:" + testDisc;

    assertEquals(testDisc, new DiscTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new DiscTagProcessor().processTag("BadDisc: test"));
  }
}
