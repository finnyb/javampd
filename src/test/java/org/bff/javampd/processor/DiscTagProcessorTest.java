package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DiscTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testDisc = "testDisc";

        DiscTagProcessor discTagProcessor = new DiscTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Disc:" + testDisc;
        discTagProcessor.processTag(song, line);

        assertEquals(testDisc, song.getDiscNumber());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testDisc = "testDisc";

        DiscTagProcessor discTagProcessor = new DiscTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadDisc:" + testDisc;
        discTagProcessor.processTag(song, line);

        assertNull(song.getDiscNumber());
    }
}