package org.bff.javampd.song;

import static org.junit.jupiter.api.Assertions.*;

import org.bff.javampd.MPDItem;
import org.junit.jupiter.api.Test;

public class MPDSongTest {

  @Test
  public void testEquals() {
    MPDItem song1 = new MPDSong("file1", "song1");
    MPDItem song2 = new MPDSong("file1", "song1");

    assertEquals(song1, song2);
  }

  @Test
  public void testEqualsNull() {
    MPDItem song1 = new MPDSong("file1", "song1");

    assertNotEquals(song1, null);
  }

  @Test
  public void testEqualsSameObject() {
    MPDItem song = new MPDSong("file1", "song");

    assertTrue(song.equals(song));
  }

  @Test
  public void testNotEquals() {
    MPDItem song1 = new MPDSong("file1", "song1");
    MPDItem song2 = new MPDSong("file2", "song2");

    assertNotEquals(song1, song2);
  }

  @Test
  public void testHashCode() {
    MPDItem song1 = new MPDSong("file1", "song1");
    MPDItem song2 = new MPDSong("file1", "song1");

    assertEquals(song1.hashCode(), song2.hashCode());
  }

  @Test
  public void testCompareToLessThanZero() {
    MPDItem song1 = new MPDSong("file1", "song1");
    MPDItem song2 = new MPDSong("file2", "song2");

    assertTrue(song1.compareTo(song2) < 0);
  }

  @Test
  public void testCompareToGreaterThanZero() {
    MPDItem song1 = new MPDSong("file2", "song2");
    MPDItem song2 = new MPDSong("file1", "song1");

    assertTrue(song1.compareTo(song2) > 0);
  }

  @Test
  public void testCompareToEquals() {
    MPDItem song1 = new MPDSong("file1", "song1");
    MPDItem song2 = new MPDSong("file1", "song1");

    assertTrue(song1.compareTo(song2) == 0);
  }

  @Test
  public void testToString() {
    String file = "file1";
    MPDItem song = new MPDSong("file1", "song1");

    assertEquals(file, song.toString());
  }

  @Test
  public void testGetName() {
    MPDItem song = new MPDSong("file1", "song1");
    song.setName("name1");

    assertEquals("name1", song.getName());
  }

  @Test
  public void testGetNameNullName() {
    MPDItem song = new MPDSong("file1", "song1");
    song.setName(null);
    assertEquals("song1", song.getName());
  }

  @Test
  public void testGetNameEmptyName() {
    MPDItem song = new MPDSong("file1", "song1");
    song.setName("");
    assertEquals("song1", song.getName());
  }
}
