package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testDate = "1990";

        DateTagProcessor dateTagProcessor = new DateTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Date:" + testDate;
        dateTagProcessor.processTag(song, line);

        assertEquals(testDate, song.getYear());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testDate = "1990";

        DateTagProcessor dateTagProcessor = new DateTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadDate:" + testDate;
        dateTagProcessor.processTag(song, line);

        assertNull(song.getYear());
    }
}