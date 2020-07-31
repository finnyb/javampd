package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GenreTagProcessorTest {

    @Test
    public void testProcessSong() {
        String testGenre = "testGenre";

        GenreTagProcessor genreTagProcessor = new GenreTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "Genre:" + testGenre;
        genreTagProcessor.processTag(song, line);

        assertEquals(testGenre, song.getGenre());
    }

    @Test
    public void testProcessSongBadLine() {
        String testGenre = "testGenre";

        GenreTagProcessor genreTagProcessor = new GenreTagProcessor();
        MPDSong song = new MPDSong("testFile", "testName");

        String line = "BadGenre:" + testGenre;
        genreTagProcessor.processTag(song, line);

        assertNull(song.getGenre());
    }

    @Test
    public void testProcessAlbum() {
        String testAlbumName = "testAlbum";
        String testArtist = "testArtist";
        String testGenre = "testGenre";

        GenreTagProcessor genreTagProcessor = new GenreTagProcessor();

        MPDAlbum album = new MPDAlbum(testAlbumName, testArtist);

        String line = "Genre:" + testGenre;
        genreTagProcessor.processTag(album, line);

        assertEquals(testGenre, album.getGenre());
    }
}