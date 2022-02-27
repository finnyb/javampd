package org.bff.javampd.processor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TrackTagProcessorTest {

    @Test
    void testProcess() {
        var testTrack = "2/10";
        var line = "Track:" + testTrack;

        assertEquals(testTrack, new TrackTagProcessor().processTag(line));
    }

    @Test
    void testProcessBadLine() {
        assertNull(new TrackTagProcessor().processTag("BadTrack: 2/10"));
    }
}
