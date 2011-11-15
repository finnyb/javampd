/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd.objects;

import org.bff.javampd.BaseTest;
import org.junit.*;

/**
 * @author Bill
 */
public class MPDAlbumTest extends BaseTest {

    public MPDAlbumTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testInEqualityAlbumNames() {
        MPDAlbum alb1 = new MPDAlbum("Album1");
        MPDAlbum alb2 = new MPDAlbum("Album2");

        Assert.assertFalse(alb1.equals(alb2));
    }

    @Test
    public void testInEqualityAlbumArtists() {
        MPDAlbum alb1 = new MPDAlbum("Album1");
        MPDAlbum alb2 = new MPDAlbum("Album1");

        alb1.setArtist(new MPDArtist("Artist1"));
        alb2.setArtist(new MPDArtist("Artist2"));

        Assert.assertFalse(alb1.equals(alb2));
    }

    @Test
    public void testEqualityAlbumNames() {
        MPDAlbum alb1 = new MPDAlbum("Album1");
        MPDAlbum alb2 = new MPDAlbum("Album1");

        Assert.assertTrue(alb1.equals(alb2));
    }

    @Test
    public void testEqualityAlbumArtists() {
        MPDAlbum alb1 = new MPDAlbum("Album1");
        MPDAlbum alb2 = new MPDAlbum("Album1");

        alb1.setArtist(new MPDArtist("Artist1"));
        alb2.setArtist(new MPDArtist("Artist1"));

        Assert.assertTrue(alb1.equals(alb2));
    }
}