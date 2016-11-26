package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AlbumTagProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testAlbum = "testAlbum";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Album:" + testAlbum;
        albumTagProcessor.processTag(song, line);

        assertEquals(testAlbum, song.getAlbumName());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testAlbum = "testAlbum";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "BadAlbum:" + testAlbum;
        albumTagProcessor.processTag(song, line);

        assertNull(song.getAlbumName());
    }

    @Test
    public void testProcessAlbum() throws Exception {
        String testAlbumName = "testAlbum";
        String testArtist = "testArtist";

        AlbumTagProcessor albumTagProcessor = new AlbumTagProcessor();

        MPDAlbum album = new MPDAlbum(testAlbumName, testArtist);

        String line = "Album:" + testAlbumName;
        albumTagProcessor.processTag(album, line);

        assertEquals(new MPDAlbum(testAlbumName, testArtist), album);
    }
}