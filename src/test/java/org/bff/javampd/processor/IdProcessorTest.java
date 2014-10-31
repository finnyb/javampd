package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class IdProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        int testId = 1;

        IdProcessor idProcessor = new IdProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "Id:" + testId;
        idProcessor.processSong(song, line);

        assertEquals(testId, song.getId());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        int testId = 1;

        IdProcessor idProcessor = new IdProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "BadId:" + testId;
        idProcessor.processSong(song, line);

        assertEquals(-1, song.getId());
    }
}