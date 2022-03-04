package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class NameTagProcessorTest {

  @Test
  void testProcess() {
    var testName = "testName";
    var line = "Name:" + testName;

    assertEquals(testName, new NameTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new NameTagProcessor().processTag("BadName: test"));
  }
}
