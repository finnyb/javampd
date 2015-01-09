package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AlbumProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testAlbum = "testAlbum";

        AlbumProcessor albumProcessor = new AlbumProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Album:" + testAlbum;
        albumProcessor.processSong(song, line);

        assertEquals(testAlbum, song.getAlbumName());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testAlbum = "testAlbum";

        AlbumProcessor albumProcessor = new AlbumProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "BadAlbum:" + testAlbum;
        albumProcessor.processSong(song, line);

        assertNull(song.getAlbumName());
    }
}