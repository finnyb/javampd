package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class TimeTagProcessorTest {

  @Test
  void testProcess() {
    var testLength = "1";
    var line = "Time:" + testLength;

    assertEquals(testLength, new TimeTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new TimeTagProcessor().processTag("BadTime: test"));
  }
}
