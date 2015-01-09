package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        int testLength = 1;

        TimeProcessor timeProcessor = new TimeProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Time:" + testLength;
        timeProcessor.processSong(song, line);

        assertEquals(testLength, song.getLength());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        int testLength = 1;

        TimeProcessor timeProcessor = new TimeProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadTime:" + testLength;
        timeProcessor.processSong(song, line);

        assertEquals(0, song.getLength());
    }
}