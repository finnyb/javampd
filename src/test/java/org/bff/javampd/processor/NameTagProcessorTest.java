package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class NameTagProcessorTest {

    @Test
    void testProcessSong() {
        String testName = "testName";

        NameTagProcessor nameTagProcessor = new NameTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("testTitle").build();

        String line = "Name:" + testName;
        nameTagProcessor.processTag(song, line);

        assertEquals(testName, song.getName());
    }

    @Test
    void testProcessSongNoName() {
        String testTitle = "testTitle";

        NameTagProcessor nameTagProcessor = new NameTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title(testTitle).build();

        String line = "";
        nameTagProcessor.processTag(song, line);

        assertEquals(testTitle, song.getName());
    }

    @Test
    void testProcessSongBadLine() {
        String testName = "testName";

        TitleTagProcessor titleTagProcessor = new TitleTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title(null).build();

        String line = "BadName:" + testName;
        titleTagProcessor.processTag(song, line);

        assertNull(song.getName());
    }
}
