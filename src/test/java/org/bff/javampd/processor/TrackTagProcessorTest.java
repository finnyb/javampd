package org.bff.javampd.processor;

import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrackTagProcessorTest {
    @Test
    void testProcessSong() {
        String testTrack = "2/10";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("title").build();

        String line = "Track:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(2, song.getTrack());
    }

    @Test
    void testProcessSongBadLine() {
        String testTrack = "2/10";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("title").build();

        String line = "BadTrack:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(0, song.getTrack());
    }

    @Test
    void testProcessSongUnparseable() {
        String testTrack = "junk";

        TrackTagProcessor trackTagProcessor = new TrackTagProcessor();
        MPDSong song = MPDSong.builder().file("testFile").title("title").build();

        String line = "Track:" + testTrack;
        trackTagProcessor.processTag(song, line);

        assertEquals(0, song.getTrack());
    }
}
