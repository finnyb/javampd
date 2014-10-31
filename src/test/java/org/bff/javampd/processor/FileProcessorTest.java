package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FileProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testFile = "testFile";

        FileProcessor fileProcessor = new FileProcessor();
        MPDSong song = new MPDSong("file");

        String line = "file:" + testFile;
        fileProcessor.processSong(song, line);

        assertEquals(testFile, song.getFile());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testFile = "testFile";
        String f = "file";

        FileProcessor fileProcessor = new FileProcessor();
        MPDSong song = new MPDSong(f);

        String line = "BadFile:" + testFile;
        fileProcessor.processSong(song, line);

        assertEquals(f, song.getFile());
    }
}