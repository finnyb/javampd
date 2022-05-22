package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class StatusTest {
  @Test
  void lookupUnknownStatus() {
    assertEquals(Status.UNKNOWN, Status.lookup("junk"));
  }
}
