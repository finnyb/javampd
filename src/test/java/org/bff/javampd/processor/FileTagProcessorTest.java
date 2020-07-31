package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTagProcessorTest {

    @Test
    public void testProcessSong() {
        String testFile = "testFile";

        FileTagProcessor fileTagProcessor = new FileTagProcessor();
        MPDSong song = new MPDSong("file", "title");

        String line = "file:" + testFile;
        fileTagProcessor.processTag(song, line);

        assertEquals(testFile, song.getFile());
    }

    @Test
    public void testProcessSongBadLine() {
        String testFile = "testFile";
        String f = "file";

        FileTagProcessor fileTagProcessor = new FileTagProcessor();
        MPDSong song = new MPDSong(f, "title");

        String line = "BadFile:" + testFile;
        fileTagProcessor.processTag(song, line);

        assertEquals(f, song.getFile());
    }
}