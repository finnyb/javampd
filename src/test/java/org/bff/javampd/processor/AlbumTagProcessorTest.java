package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AlbumTagProcessorTest {

    @Test
    void testProcessSong() {
        String testAlbum = "testAlbum";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Album:" + testAlbum;
        albumTagProcessor.processTag(song, line);

        assertEquals(testAlbum, song.getAlbumName());
    }

    @Test
    void testProcessSongBadLine() {
        String testAlbum = "testAlbum";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "BadAlbum:" + testAlbum;
        albumTagProcessor.processTag(song, line);

        assertNull(song.getAlbumName());
    }

    @Test
    void testProcessAlbum() {
        String testAlbumName = "testAlbum";
        String testArtist = "testArtist";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();

        MPDAlbum album = new MPDAlbum(testAlbumName, testArtist);

        String line = "Album:" + testAlbumName;
        albumTagProcessor.processTag(album, line);

        assertEquals(new MPDAlbum(testAlbumName, testArtist), album);
    }
}
