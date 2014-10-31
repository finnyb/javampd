package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class GenreProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testGenre = "testGenre";

        GenreProcessor genreProcessor = new GenreProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "Genre:" + testGenre;
        genreProcessor.processSong(song, line);

        assertEquals(testGenre, song.getGenre());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testGenre = "testGenre";

        GenreProcessor genreProcessor = new GenreProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "BadGenre:" + testGenre;
        genreProcessor.processSong(song, line);

        assertNull(song.getGenre());
    }
}