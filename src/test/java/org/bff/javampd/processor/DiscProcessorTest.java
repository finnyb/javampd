package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DiscProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testDisc = "testDisc";

        DiscProcessor discProcessor = new DiscProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Disc:" + testDisc;
        discProcessor.processSong(song, line);

        assertEquals(testDisc, song.getDiscNumber());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testDisc = "testDisc";

        DiscProcessor discProcessor = new DiscProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadDisc:" + testDisc;
        discProcessor.processSong(song, line);

        assertNull(song.getDiscNumber());
    }
}