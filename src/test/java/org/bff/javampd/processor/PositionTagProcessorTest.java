package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PositionTagProcessorTest {

    @Test
    void testProcess() {
        var testPosition = "1";
        var line = "Pos:" + testPosition;

        assertEquals(testPosition, new PositionTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new PositionTagProcessor().processTag("BadPos: test"));
    }
}
