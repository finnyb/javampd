package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        int testLength = 1;

        TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Time:" + testLength;
        timeTagProcessor.processTag(song, line);

        assertEquals(testLength, song.getLength());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        int testLength = 1;

        TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadTime:" + testLength;
        timeTagProcessor.processTag(song, line);

        assertEquals(0, song.getLength());
    }
}