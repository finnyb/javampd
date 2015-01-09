package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class DateProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testDate = "1990";

        DateProcessor dateProcessor = new DateProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Date:" + testDate;
        dateProcessor.processSong(song, line);

        assertEquals(testDate, song.getYear());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testDate = "1990";

        DateProcessor dateProcessor = new DateProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadDate:" + testDate;
        dateProcessor.processSong(song, line);

        assertNull(song.getYear());
    }
}