package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ArtistProcessorTest {

    @Test
    public void testProcessSong() throws Exception {
        String testArtist = "testArtist";

        ArtistProcessor artistProcessor = new ArtistProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "Artist:" + testArtist;
        artistProcessor.processSong(song, line);

        assertEquals(testArtist, song.getArtistName());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testArtist = "testArtist";

        ArtistProcessor artistProcessor = new ArtistProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "BadArtist:" + testArtist;
        artistProcessor.processSong(song, line);

        assertNull(song.getArtistName());
    }
}