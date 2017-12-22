package org.bff.javampd.art;

import org.bff.javampd.artist.MPDArtist;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MPDArtworkTest {
    @Test
    public void testEqualsSameObject() {
        MPDArtwork artwork = new MPDArtwork("name", "path");
        assertEquals(artwork, artwork);
    }

    @Test
    public void testGetName() {
        String name = "name";
        MPDArtwork artwork = new MPDArtwork(name, "path");
        assertEquals(name, artwork.getName());
    }

    @Test
    public void testGetPath() {
        String path = "path";
        MPDArtwork artwork = new MPDArtwork("name", path);
        assertEquals(path, artwork.getPath());
    }

    @Test
    public void testEqualsSamePath() {
        MPDArtwork artwork1 = new MPDArtwork("name", "path");
        MPDArtwork artwork2 = new MPDArtwork("name", "path");
        assertEquals(artwork1, artwork2);
    }

    @Test
    public void testEqualsDifferentPath() {
        MPDArtwork artwork1 = new MPDArtwork("name", "path1");
        MPDArtwork artwork2 = new MPDArtwork("name", "path2");
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    public void testEqualsDifferentObject() {
        MPDArtwork artwork = new MPDArtwork("name", "path");
        MPDArtist artist = new MPDArtist("artist");
        assertNotEquals(artwork, artist);
    }

    @Test
    public void testEqualsNullPath() {
        MPDArtwork artwork1 = new MPDArtwork("name", null);
        MPDArtwork artwork2 = new MPDArtwork("name", "path2");
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    public void testEqualsNullPathParamater() {
        MPDArtwork artwork1 = new MPDArtwork("name", "path1");
        MPDArtwork artwork2 = new MPDArtwork("name", null);
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    public void testEqualsBothNull() {
        MPDArtwork artwork1 = new MPDArtwork("name", null);
        MPDArtwork artwork2 = new MPDArtwork("name", null);
        assertEquals(artwork1, artwork2);
    }

    @Test
    public void testHashCodeSamePath() throws Exception {
        MPDArtwork artwork1 = new MPDArtwork("name", "path");
        MPDArtwork artwork2 = new MPDArtwork("name", "path");
        assertEquals(artwork1.hashCode(), artwork2.hashCode());
    }

    @Test
    public void testHashCodeDifferentPath() {
        MPDArtwork artwork1 = new MPDArtwork("name", "path1");
        MPDArtwork artwork2 = new MPDArtwork("name", "path2");
        assertNotEquals(artwork1.hashCode(), artwork2.hashCode());
    }

    @Test
    public void testHashCodeNull() {
        MPDArtwork artwork = new MPDArtwork("name", null);
        assertEquals(0, artwork.hashCode());
    }

    @Test
    public void testGetBytes() {
        byte[] bytes = {
                0
        };

        MPDArtwork artwork = new MPDArtwork("name", "path");
        artwork.setBytes(bytes);

        assertEquals(bytes, artwork.getBytes());
    }
}