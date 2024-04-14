package org.bff.javampd.song;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDSongTest {
  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDSong.class).verify();
  }

  @Test
  void testCompareToLessThanZero() {
    MPDSong song1 = MPDSong.builder().file("file1").title("song1").build();
    MPDSong song2 = MPDSong.builder().file("file2").title("song2").build();

    assertTrue(song1.compareTo(song2) < 0);
  }

  @Test
  void testCompareToGreaterThanZero() {
    MPDSong song1 = MPDSong.builder().file("file2").title("song2").build();
    MPDSong song2 = MPDSong.builder().file("file1").title("song1").build();

    assertTrue(song1.compareTo(song2) > 0);
  }

  @Test
  void testCompareToEquals() {
    MPDSong song1 = MPDSong.builder().file("file1").title("song1").build();
    MPDSong song2 = MPDSong.builder().file("file1").title("song1").build();

    assertEquals(0, song1.compareTo(song2));
  }

  @Test
  void testToString() {
    String file = "file1";
    MPDSong song = MPDSong.builder().file("file1").title("song1").build();

    assertThat(
        song.toString(),
        is(
            equalTo(
                """
                MPDSong(name=song1, title=song1, albumArtist=null,\
                 artistName=null, albumName=null, file=file1, genre=null,\
                 comment=null, date=null, discNumber=null, track=null,\
                 length=0, tagMap=null)\
                """)));
  }

  @Test
  void testGetName() {
    MPDSong song = MPDSong.builder().file("file1").title("song1").name("name1").build();
    assertEquals("name1", song.getName());
  }

  @Test
  void testGetNameNullName() {
    MPDSong song = MPDSong.builder().file("file1").title("song1").build();
    assertEquals("song1", song.getName());
  }

  @Test
  void testGetNameEmptyName() {
    MPDSong song = MPDSong.builder().file("file1").title("song1").name("").build();
    assertEquals("song1", song.getName());
  }
}
