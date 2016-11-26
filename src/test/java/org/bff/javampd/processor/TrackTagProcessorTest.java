package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrackTagProcessorTest {
    @Test
    public void testProcessSong() throws Exception {
        String testTrack = "2/10";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Track:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(2, song.getTrack());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testTrack = "2/10";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "BadTrack:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(0, song.getTrack());
    }

    @Test
    public void testProcessSongUnparseable() throws Exception {
        String testTrack = "junk";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = new MPDSong("testFile", "title");

        String line = "Track:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(0, song.getTrack());
    }
}