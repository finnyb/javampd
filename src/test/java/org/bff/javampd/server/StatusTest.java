package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class StatusTest {

  @Test
  public void lookupUnknownStatus() {
    assertEquals(Status.UNKNOWN, Status.lookupStatus("junk"));
  }
}
