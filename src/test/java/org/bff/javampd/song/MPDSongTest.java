package org.bff.javampd.song;

import org.bff.javampd.MPDItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MPDSongTest {

    @Test
    void testEquals() {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertEquals(song1, song2);
    }

    @Test
    void testEqualsNull() {
        MPDItem song1 = new MPDSong("file1", "song1");

        assertNotEquals(song1, null);
    }

    @Test
    void testEqualsSameObject() {
        MPDItem song = new MPDSong("file1", "song");

        assertTrue(song.equals(song));
    }

    @Test
    void testNotEquals() {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file2", "song2");

        assertNotEquals(song1, song2);
    }

    @Test
    void testHashCode() {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertEquals(song1.hashCode(), song2.hashCode());
    }

    @Test
    void testCompareToLessThanZero() {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file2", "song2");

        assertTrue(song1.compareTo(song2) < 0);
    }

    @Test
    void testCompareToGreaterThanZero() {
        MPDItem song1 = new MPDSong("file2", "song2");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertTrue(song1.compareTo(song2) > 0);
    }

    @Test
    void testCompareToEquals() {
        MPDItem song1 = new MPDSong("file1", "song1");
        MPDItem song2 = new MPDSong("file1", "song1");

        assertTrue(song1.compareTo(song2) == 0);
    }

    @Test
    void testToString() {
        String file = "file1";
        MPDItem song = new MPDSong("file1", "song1");

        assertEquals(file, song.toString());
    }

    @Test
    void testGetName() {
        MPDItem song = new MPDSong("file1", "song1");
        song.setName("name1");

        assertEquals("name1", song.getName());
    }

    @Test
    void testGetNameNullName() {
        MPDItem song = new MPDSong("file1", "song1");
        song.setName(null);
        assertEquals("song1", song.getName());
    }

    @Test
    void testGetNameEmptyName() {
        MPDItem song = new MPDSong("file1", "song1");
        song.setName("");
        assertEquals("song1", song.getName());
    }
}
