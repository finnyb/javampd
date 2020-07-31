package org.bff.javampd.album;

import org.bff.javampd.MPDItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bill
 */
public class MPDAlbumTest {

    @Test
    public void testInEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertNotEquals(album2, album1);
    }

    @Test
    public void testInEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist2");

        assertNotEquals(album2, album1);
    }

    @Test
    public void testEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(album2, album1);
    }

    @Test
    public void testEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(album2, album1);
    }

    @Test
    public void testEqualityDifferentGenres() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        album1.setGenre("genre1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");
        album2.setGenre("genre2");

        assertNotEquals(album2, album1);
    }


    @Test
    public void testCompareArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertEquals(album1.getArtistName(), album2.getArtistName());
    }
    
    @Test
    public void testEqualsNull() {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertNotEquals(album, null);
    }

    @Test
    public void testEqualsSameObject() {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertEquals(album, album);
    }

    @Test
    public void testHashCode() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void testCompareToLessThanZero() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album2", "Artist1");

        assertTrue(item1.compareTo(item2) < 0);
    }

    @Test
    public void testCompareToGreaterThanZero() {
        MPDItem item1 = new MPDAlbum("Album2", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(item1.compareTo(item2) > 0);
    }

    @Test
    public void testCompareToEquals() {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(item1.compareTo(item2), 0);
    }
}