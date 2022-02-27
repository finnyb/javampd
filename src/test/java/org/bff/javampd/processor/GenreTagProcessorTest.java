package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class GenreTagProcessorTest {

    @Test
    void testProcess() {
        String testGenre = "testGenre";
        String line = "Genre:" + testGenre;

        assertEquals(testGenre, new GenreTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new GenreTagProcessor().processTag("BadGenre: test"));
    }
}
