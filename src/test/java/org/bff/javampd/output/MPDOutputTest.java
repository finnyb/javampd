package org.bff.javampd.output;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDOutputTest {

  @Test
  void equalsContract() {
    EqualsVerifier.simple().forClass(MPDOutput.class).verify();
  }
}
