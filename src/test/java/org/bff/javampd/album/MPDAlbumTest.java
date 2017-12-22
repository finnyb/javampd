package org.bff.javampd.album;

import org.bff.javampd.MPDItem;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bill
 */
public class MPDAlbumTest {

    @Test
    public void testInEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertFalse(album1.equals(album2));
    }

    @Test
    public void testInEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist2");

        assertFalse(album1.equals(album2));
    }

    @Test
    public void testEqualityAlbumNames() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(album1.equals(album2));
    }

    @Test
    public void testEqualityAlbumArtists() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(album1.equals(album2));
    }

    @Test
    public void testEqualityDifferentGenres() {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        album1.setGenre("genre1");
        MPDAlbum album2 = new MPDAlbum("Album1", "Artist1");
        album2.setGenre("genre2");

        assertFalse(album1.equals(album2));
    }


    @Test
    public void testCompareArtists() throws Exception {
        MPDAlbum album1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum album2 = new MPDAlbum("Album2", "Artist1");

        assertEquals(album1.getArtistName(), album2.getArtistName());
    }
    
    @Test
    public void testEqualsNull() throws Exception {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertNotEquals(album, null);
    }

    @Test
    public void testEqualsSameObject() throws Exception {
        MPDAlbum album = new MPDAlbum("Album", "Artist");

        assertTrue(album.equals(album));
    }

    @Test
    public void testHashCode() throws Exception {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void testCompareToLessThanZero() throws Exception {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album2", "Artist1");

        assertTrue(item1.compareTo(item2) < 0);
    }

    @Test
    public void testCompareToGreaterThanZero() throws Exception {
        MPDItem item1 = new MPDAlbum("Album2", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(item1.compareTo(item2) > 0);
    }

    @Test
    public void testCompareToEquals() throws Exception {
        MPDItem item1 = new MPDAlbum("Album1", "Artist1");
        MPDItem item2 = new MPDAlbum("Album1", "Artist1");

        assertTrue(item1.compareTo(item2) == 0);
    }
}