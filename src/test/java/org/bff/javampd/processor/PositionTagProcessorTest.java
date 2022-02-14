package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PositionTagProcessorTest {

    @Test
    void testProcessSong() {
        int testPosition = 1;

        PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("title").build();

        String line = "Pos:" + testPosition;
        positionTagProcessor.processTag(song, line);

        assertEquals(testPosition, song.getPosition());
    }

    @Test
    void testProcessSongBadLine() {
        int testPosition = 1;

        PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("title").build();

        String line = "BadPos:" + testPosition;
        positionTagProcessor.processTag(song, line);

        assertEquals(-1, song.getId());
    }
}
