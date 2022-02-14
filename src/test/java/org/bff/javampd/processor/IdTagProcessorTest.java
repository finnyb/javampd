package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class IdTagProcessorTest {

    @Test
    void testProcessSong() {
        int testId = 1;

        IdTagProcessor idTagProcessor = new IdTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "Id:" + testId;
        idTagProcessor.processTag(song, line);

        assertEquals(testId, song.getId());
    }

    @Test
    void testProcessSongBadLine() {
        int testId = 1;

        IdTagProcessor idTagProcessor = new IdTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testName").build();

        String line = "BadId:" + testId;
        idTagProcessor.processTag(song, line);

        assertEquals(-1, song.getId());
    }
}
