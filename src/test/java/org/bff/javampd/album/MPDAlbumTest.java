package org.bff.javampd.album;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * @author Bill
 */
class MPDAlbumTest {
  @Test
  @DisplayName("add artist using default list")
  void defaultArtistList() {
    var artist = "Greta Van Fleet";
    var album = MPDAlbum.builder("Anthem of the Peaceful Army").build();
    album.addArtist(artist);
    assertThat(artist, is(equalTo(album.getArtistNames().get(0))));
  }

  @Test
  void addArtist() {
    var album = MPDAlbum.builder("album").build();
    album.addArtist("Tool");
    assertThat("Tool", is(equalTo(album.getArtistNames().get(0))));
  }

  @Test
  void addArtists() {
    var album = MPDAlbum.builder("album").build();
    album.addArtists(Arrays.asList("Tool", "Breaking Benjamin"));

    assertAll(
        () -> assertThat(2, is(equalTo(album.getArtistNames().size()))),
        () -> assertThat("Tool", is(equalTo(album.getArtistNames().get(0)))),
        () -> assertThat("Breaking Benjamin", is(equalTo(album.getArtistNames().get(1)))));
  }

  @Test
  void addGenre() {
    var album = MPDAlbum.builder("album").build();
    album.addGenre("Rock");
    assertThat("Rock", is(equalTo(album.getGenres().get(0))));
  }

  @Test
  void addGenres() {
    var album = MPDAlbum.builder("album").build();
    album.addGenres(Arrays.asList("Rock", "Heavy Metal"));

    assertAll(
        () -> assertThat(2, is(equalTo(album.getGenres().size()))),
        () -> assertThat("Rock", is(equalTo(album.getGenres().get(0)))),
        () -> assertThat("Heavy Metal", is(equalTo(album.getGenres().get(1)))));
  }

  @Test
  void addDate() {
    var album = MPDAlbum.builder("album").build();
    album.addDate("1990");
    assertThat("1990", is(equalTo(album.getDates().get(0))));
  }

  @Test
  void addDates() {
    var album = MPDAlbum.builder("album").build();
    album.addDates(Arrays.asList("1990", "2006-05-24"));

    assertAll(
        () -> assertThat(2, is(equalTo(album.getDates().size()))),
        () -> assertThat("1990", is(equalTo(album.getDates().get(0)))),
        () -> assertThat("2006-05-24", is(equalTo(album.getDates().get(1)))));
  }

  @Test
  void testCompareToLessThanZero() {
    MPDAlbum album1 =
        MPDAlbum.builder("Album1").artistNames(Collections.singletonList("artistName1")).build();

    MPDAlbum album2 =
        MPDAlbum.builder("Album2").artistNames(Collections.singletonList("artistName1")).build();

    assertTrue(album1.compareTo(album2) < 0);
  }

  @Test
  void testCompareToGreaterThanZero() {
    MPDAlbum album1 =
        MPDAlbum.builder("Album2").artistNames(Collections.singletonList("artistName1")).build();

    MPDAlbum album2 =
        MPDAlbum.builder("Album1").artistNames(Collections.singletonList("artistName1")).build();

    assertTrue(album1.compareTo(album2) > 0);
  }

  @Test
  void testCompareToEquals() {
    MPDAlbum album1 =
        MPDAlbum.builder("Album1").artistNames(Collections.singletonList("artistName1")).build();

    MPDAlbum album2 =
        MPDAlbum.builder("Album1").artistNames(Collections.singletonList("artistName1")).build();

    assertEquals(0, album1.compareTo(album2));
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDAlbum.class).verify();
  }
}
