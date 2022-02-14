package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class DateTagProcessorTest {

    @Test
    void testProcessSong() {
        String testDate = "1990";

        DateTagProcessor dateTagProcessor = new DateTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "Date:" + testDate;
        dateTagProcessor.processTag(song, line);

        assertEquals(testDate, song.getYear());
    }

    @Test
    void testProcessSongBadLine() {
        String testDate = "1990";

        DateTagProcessor dateTagProcessor = new DateTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "BadDate:" + testDate;
        dateTagProcessor.processTag(song, line);

        assertNull(song.getYear());
    }
}
