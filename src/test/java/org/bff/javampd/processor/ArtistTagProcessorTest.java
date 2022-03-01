package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ArtistTagProcessorTest {

    @Test
    void testProcess() {
        var testArtist = "testArtist";
        var line = "Artist:" + testArtist;

        assertEquals(testArtist, new ArtistTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new ArtistTagProcessor().processTag("BadArtist: test"));
    }
}
