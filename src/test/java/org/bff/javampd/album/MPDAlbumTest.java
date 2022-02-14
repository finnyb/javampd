package org.bff.javampd.album;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Bill
 */
class MPDAlbumTest {

    @Test
    void testInEqualityAlbumNames() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album2").artistName("artistName1").build();

        assertNotEquals(album2, album1);
    }

    @Test
    void testInEqualityartistNames() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName2").build();

        assertNotEquals(album2, album1);
    }

    @Test
    void testEqualityAlbumNames() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").build();

        assertEquals(album2, album1);
    }

    @Test
    void testEqualityartistNames() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").build();

        assertEquals(album2, album1);
    }

    @Test
    void testEqualityDifferentGenres() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").genre("genre1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").genre("genre2").build();

        assertNotEquals(album2, album1);
    }


    @Test
    void testCompareArtists() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album2").artistName("artistName1").build();

        assertEquals(album1.getArtistName(), album2.getArtistName());
    }

    @Test
    void testHashCode() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").build();

        assertEquals(album1.hashCode(), album2.hashCode());
    }

    @Test
    void testCompareToLessThanZero() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album2").artistName("artistName1").build();

        assertTrue(album1.compareTo(album2) < 0);
    }

    @Test
    void testCompareToGreaterThanZero() {
        MPDAlbum album1 = MPDAlbum.builder("Album2").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").build();

        assertTrue(album1.compareTo(album2) > 0);
    }

    @Test
    void testCompareToEquals() {
        MPDAlbum album1 = MPDAlbum.builder("Album1").artistName("artistName1").build();
        MPDAlbum album2 = MPDAlbum.builder("Album1").artistName("artistName1").build();

        assertEquals(0, album1.compareTo(album2));
    }

    @Test
    void equalsContract() {
        EqualsVerifier.simple().forClass(MPDAlbum.class).verify();
    }

}
