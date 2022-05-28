package org.bff.javampd.statistics;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;

class MPDStatisticsTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.simple().forClass(MPDStatistics.class).verify();
  }
}
