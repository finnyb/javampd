package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NameTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testName = "testName";

        NameTagProcessor nameTagProcessor = new NameTagProcessor();
        MPDSong song = new MPDSong("testFile", "testTitle");

        String line = "Name:" + testName;
        nameTagProcessor.processTag(song, line);

        assertEquals(testName, song.getName());
    }

    @Test
    public void testProcessSongNoName() throws Exception {
        String testTitle = "testTitle";

        NameTagProcessor nameTagProcessor = new NameTagProcessor();
        MPDSong song = new MPDSong("testFile", testTitle);

        String line = "";
        nameTagProcessor.processTag(song, line);

        assertEquals(testTitle, song.getName());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testName = "testName";

        TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
        MPDSong song = new MPDSong("testFile", null);

        String line = "BadName:" + testName;
        titleTagProcessor.processTag(song, line);

        assertNull(song.getName());
    }
}