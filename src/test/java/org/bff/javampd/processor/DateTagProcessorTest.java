package org.bff.javampd.processor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class DateTagProcessorTest {

  @Test
  void testProcess() {
    var testDate = "1990";
    var line = "Date:" + testDate;

    assertEquals(testDate, new DateTagProcessor().processTag(line));
  }

  @Test
  void testProcessBadLine() {
    assertNull(new DateTagProcessor().processTag("BadDate: 1990"));
  }
}
