package org.bff.javampd.file;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDFileTest {

  @Test
  void testGetPath() {
    String path = "/path/to/Name";
    MPDFile mpdFile = MPDFile.builder(path).build();
    mpdFile.setPath(path);
    assertEquals(path, mpdFile.getPath());
  }

  @Test
  void testIsDirectory() {
    MPDFile mpdFile = MPDFile.builder("").build();
    mpdFile.setDirectory(false);
    assertFalse(mpdFile.isDirectory());
  }

  @Test
  void testToString() {
    String path = "/path/to/Name";
    MPDFile mpdFile = MPDFile.builder(path).build();
    mpdFile.setPath(path);
    assertEquals(
        "MPDFile(directory=false, path=/path/to/Name, lastModified=null)", mpdFile.toString());
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDFile.class).verify();
  }
}
