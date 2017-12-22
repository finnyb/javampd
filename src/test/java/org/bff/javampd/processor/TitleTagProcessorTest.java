package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TitleTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testTitle = "testTitle";

        TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Title:" + testTitle;
        titleTagProcessor.processTag(song, line);

        assertEquals(testTitle, song.getTitle());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testTitle = "testTitle";

        TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
        MPDSong song = new MPDSong("testFile", null);

        String line = "BadTitle:" + testTitle;
        titleTagProcessor.processTag(song, line);

        assertNull(song.getTitle());
    }
}