package org.bff.javampd.art;

import org.bff.javampd.artist.MPDArtist;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MPDArtworkTest {
    @Test
    void testEqualsSameObject() {
        MPDArtwork artwork = MPDArtwork.builder().name("name").path("path").build();
        assertEquals(artwork, artwork);
    }

    @Test
    void testGetName() {
        String name = "name";
        MPDArtwork artwork = MPDArtwork.builder().name(name).path("path").build();
        assertEquals(name, artwork.getName());
    }

    @Test
    void testGetPath() {
        String path = "path";
        MPDArtwork artwork = MPDArtwork.builder().name("name").path(path).build();
        assertEquals(path, artwork.getPath());
    }

    @Test
    void testEqualsSamePath() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").path("path").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").path("path").build();
        assertEquals(artwork1, artwork2);
    }

    @Test
    void testEqualsDifferentPath() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").path("path1").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").path("path2").build();
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    void testEqualsNullPath() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").path("path2").build();
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    void testEqualsNullPathParameter() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").path("path1").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").build();
        assertNotEquals(artwork1, artwork2);
    }

    @Test
    void testEqualsDifferentObject() {
        MPDArtwork artwork = MPDArtwork.builder().name("name").path("path").build();
        MPDArtist artist = new MPDArtist("artist");
        assertNotEquals(artwork, artist);
    }

    @Test
    void testEqualsBothNull() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").build();
        assertEquals(artwork1, artwork2);
    }

    @Test
    void testHashCodeSamePath() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").path("path").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").path("path").build();
        assertEquals(artwork1.hashCode(), artwork2.hashCode());
    }

    @Test
    void testHashCodeDifferentPath() {
        MPDArtwork artwork1 = MPDArtwork.builder().name("name").path("path1").build();
        MPDArtwork artwork2 = MPDArtwork.builder().name("name").path("path2").build();
        assertNotEquals(artwork1.hashCode(), artwork2.hashCode());
    }

    @Test
    void testGetBytes() {
        byte[] bytes = {
                0
        };

        MPDArtwork artwork = MPDArtwork.builder().name("name").path("path").build();
        artwork.setBytes(bytes);

        assertEquals(bytes, artwork.getBytes());
    }
}
