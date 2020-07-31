package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class ErrorEventTest {

  @Test
  public void getMessage() {
    String message = "message";
    ErrorEvent errorEvent = new ErrorEvent(this, message);

    assertEquals(errorEvent.getMessage(), message);
  }

  @Test
  public void getSource() {
    Object source = new Object();
    ErrorEvent errorEvent = new ErrorEvent(source);

    assertEquals(errorEvent.getSource(), source);
  }

  @Test
  public void getSourceAndMessage() {
    Object source = new Object();
    String message = "message";
    ErrorEvent errorEvent = new ErrorEvent(source, message);

    assertEquals(errorEvent.getSource(), source);
    assertEquals(errorEvent.getMessage(), message);
  }
}
