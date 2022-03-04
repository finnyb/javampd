package org.bff.javampd.art;

import static org.junit.jupiter.api.Assertions.assertEquals;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDArtworkTest {

  @Test
  void testGetBytes() {
    byte[] bytes = {0};

    MPDArtwork artwork = MPDArtwork.builder().name("name").path("path").build();
    artwork.setBytes(bytes);

    assertEquals(bytes, artwork.getBytes());
  }

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDArtwork.class).verify();
  }
}
