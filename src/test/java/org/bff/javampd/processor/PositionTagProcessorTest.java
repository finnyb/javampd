package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PositionTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        int testPosition = 1;

        PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Pos:" + testPosition;
        positionTagProcessor.processTag(song, line);

        assertEquals(testPosition, song.getPosition());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        int testPosition = 1;

        PositionTagProcessor positionTagProcessor = new PositionTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "BadPos:" + testPosition;
        positionTagProcessor.processTag(song, line);

        assertEquals(-1, song.getId());
    }
}