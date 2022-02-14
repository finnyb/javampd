package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TimeTagProcessorTest {

    @Test
    void testProcessSong() {
        int testLength = 1;

        TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "Time:" + testLength;
        timeTagProcessor.processTag(song, line);

        assertEquals(testLength, song.getLength());
    }

    @Test
    void testProcessSongBadLine() {
        int testLength = 1;

        TimeTagProcessor timeTagProcessor = new TimeTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "BadTime:" + testLength;
        timeTagProcessor.processTag(song, line);

        assertEquals(0, song.getLength());
    }
}
