package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TitleTagProcessorTest {

    @Test
    void testProcess() {
        var testTitle = "testTitle";
        var line = "Title:" + testTitle;

        assertEquals(testTitle, new TitleTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new TitleTagProcessor().processTag("BadTitle: test"));
    }
}
