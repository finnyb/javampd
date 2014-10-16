/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd.album;

import org.junit.Assert;
import org.junit.Test;

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
}