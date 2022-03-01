package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileTagProcessorTest {

    @Test
    void testProcess() {
        String testFile = "testFile";
        String line = "file:" + testFile;

        assertEquals(testFile, new FileTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new FileTagProcessor().processTag("BadFile: test"));
    }
}
