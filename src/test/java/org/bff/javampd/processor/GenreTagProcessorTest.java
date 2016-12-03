package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenreTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testGenre = "testGenre";

        GenreTagProcessor genreTagProcessor = new GenreTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Genre:" + testGenre;
        genreTagProcessor.processTag(song, line);

        assertEquals(testGenre, song.getGenre());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testGenre = "testGenre";

        GenreTagProcessor genreTagProcessor = new GenreTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadGenre:" + testGenre;
        genreTagProcessor.processTag(song, line);

        assertNull(song.getGenre());
    }
}