package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTagProcessorTest {

    @Test
    void testProcessSong() {
        String testFile = "testFile";

        FileTagProcessor fileTagProcessor = new FileTagProcessor();
        MPDSong song = MPDSong.builder().file("file").title("title").build();

        String line = "file:" + testFile;
        fileTagProcessor.processTag(song, line);

        assertEquals(testFile, song.getFile());
    }

    @Test
    void testProcessSongBadLine() {
        String testFile = "testFile";
        String f = "file";

        FileTagProcessor fileTagProcessor = new FileTagProcessor();
        MPDSong song = MPDSong.builder().file(f).title("title").build();

        String line = "BadFile:" + testFile;
        fileTagProcessor.processTag(song, line);

        assertEquals(f, song.getFile());
    }
}
