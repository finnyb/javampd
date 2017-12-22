package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        int testId = 1;

        IdTagProcessor idTagProcessor = new IdTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Id:" + testId;
        idTagProcessor.processTag(song, line);

        assertEquals(testId, song.getId());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        int testId = 1;

        IdTagProcessor idTagProcessor = new IdTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadId:" + testId;
        idTagProcessor.processTag(song, line);

        assertEquals(-1, song.getId());
    }
}