package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

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
