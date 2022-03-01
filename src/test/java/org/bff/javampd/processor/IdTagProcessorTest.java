package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class IdTagProcessorTest {

    @Test
    void testProcess() {
        var testId = "1";
        var line = "Id:" + testId;

        assertEquals(testId, new IdTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new IdTagProcessor().processTag("BadId: test"));
    }
}
