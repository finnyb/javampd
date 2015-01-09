package org.bff.javampd.song;

import org.bff.javampd.MPDItem;
import org.junit.Test;

import static org.junit.Assert.*;

public class MPDSongTest {

    @Test
    public void testEquals() throws Exception {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertEquals(song1, song2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file2", "song2");

        assertNotEquals(song1, song2);
    }

    @Test
    public void testHashCode() throws Exception {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertEquals(song1.hashCode(), song2.hashCode());
    }

    @Test
    public void testCompareToLessThanZero() throws Exception {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file2", "song2");

        assertTrue(song1.compareTo(song2) < 0);
    }

    @Test
    public void testCompareToGreaterThanZero() throws Exception {
        MPDItem song1 = new MPDSong("file2", "song2");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertTrue(song1.compareTo(song2) > 0);
    }

    @Test
    public void testCompareToEquals() throws Exception {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertTrue(song1.compareTo(song2) == 0);
    }
}