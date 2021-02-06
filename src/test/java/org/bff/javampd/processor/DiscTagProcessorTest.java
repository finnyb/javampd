package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DiscTagProcessorTest {

    @Test
    void testProcessSong() {
        String testDisc = "testDisc";

        DiscTagProcessor discTagProcessor = new DiscTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Disc:" + testDisc;
        discTagProcessor.processTag(song, line);

        assertEquals(testDisc, song.getDiscNumber());
    }

    @Test
    void testProcessSongBadLine() {
        String testDisc = "testDisc";

        DiscTagProcessor discTagProcessor = new DiscTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadDisc:" + testDisc;
        discTagProcessor.processTag(song, line);

        assertNull(song.getDiscNumber());
    }
}
