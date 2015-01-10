package org.bff.javampd.album;

import org.bff.javampd.MPDItem;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Bill
 */
public class MPDAlbumTest {

    @Test
    public void testInEqualityAlbumNames() {
        MPDAlbum alb1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum alb2 = new MPDAlbum("Album2", "Artist1");

        Assert.assertFalse(alb1.equals(alb2));
    }

    @Test
    public void testInEqualityAlbumArtists() {
        MPDAlbum alb1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum alb2 = new MPDAlbum("Album1", "Artist2");

        Assert.assertFalse(alb1.equals(alb2));
    }

    @Test
    public void testEqualityAlbumNames() {
        MPDAlbum alb1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum alb2 = new MPDAlbum("Album1", "Artist1");

        Assert.assertTrue(alb1.equals(alb2));
    }

    @Test
    public void testEqualityAlbumArtists() {
        MPDAlbum alb1 = new MPDAlbum("Album1", "Artist1");
        MPDAlbum alb2 = new MPDAlbum("Album1", "Artist1");

        Assert.assertTrue(alb1.equals(alb2));
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