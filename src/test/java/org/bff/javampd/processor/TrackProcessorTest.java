package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TrackProcessorTest {
    @Test
    public void testProcessSong() throws Exception {
        String testTrack = "2/10";

        TrackProcessor trackProcessor = new TrackProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "Track:" + testTrack;
        trackProcessor.processSong(song, line);

        assertEquals(2, song.getTrack());
    }

    @Test
    public void testProcessSongBadLine() throws Exception {
        String testTrack = "2/10";

        TrackProcessor trackProcessor = new TrackProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "BadTrack:" + testTrack;
        trackProcessor.processSong(song, line);

        assertEquals(0, song.getTrack());
    }

    @Test
    public void testProcessSongUnparseable() throws Exception {
        String testTrack = "junk";

        TrackProcessor trackProcessor = new TrackProcessor();
        MPDSong song = new MPDSong("testFile");

        String line = "Track:" + testTrack;
        trackProcessor.processSong(song, line);

        assertEquals(0, song.getTrack());
    }
}