package org.bff.javampd.album;

import org.bff.javampd.MPDItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bill
 */
class MPDAlbumTest {

    @Test
    void testInEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertNotEquals(album2, album1);
    }

    @Test
    void testInEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist2");

        assertNotEquals(album2, album1);
    }

    @Test
    void testEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(album2, album1);
    }

    @Test
    void testEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(album2, album1);
    }

    @Test
    void testEqualityDifferentGenres() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        album1.setGenre("genre1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");
        album2.setGenre("genre2");

        assertNotEquals(album2, album1);
    }


    @Test
    void testCompareArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertEquals(album1.getArtistName(), album2.getArtistName());
    }
    
    @Test
    void testEqualsNull() {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertNotEquals(album, null);
    }

    @Test
    void testEqualsSameObject() {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertEquals(album, album);
    }

    @Test
    void testHashCode() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testCompareToLessThanZero() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album2", "Artist1");

        assertTrue(item1.compareTo(item2) < 0);
    }

    @Test
    void testCompareToGreaterThanZero() {
        MPDItem item1 = new MPDAlbum("Album2", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(item1.compareTo(item2) > 0);
    }

    @Test
    void testCompareToEquals() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(item1.compareTo(item2), 0);
    }
}
