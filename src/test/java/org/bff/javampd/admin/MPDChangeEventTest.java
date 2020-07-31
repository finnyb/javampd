package org.bff.javampd.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class MPDChangeEventTest {

  @Test
  public void testConstructor2Params() {
    Object source = new Object();
    MPDChangeEvent.Event event = MPDChangeEvent.Event.KILLED;

    MPDChangeEvent changeEvent = new MPDChangeEvent(source, event);
    assertEquals(source, changeEvent.getSource());
    assertEquals(event, changeEvent.getEvent());
  }
}
