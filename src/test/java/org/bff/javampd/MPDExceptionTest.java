package org.bff.javampd;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

class MPDExceptionTest {
  @Test
  void testDefaultConstructorCommand() {
    MPDException exception = new MPDException();
    assertNull(exception.getCommand());
    assertNull(exception.getMessage());
    assertNull(exception.getCause());
  }

  @Test
  void testMessageConstructorCommand() {
    String message = "message";
    MPDException exception = new MPDException(message);
    assertNull(exception.getCommand());
    assertEquals(exception.getMessage(), message);
    assertNull(exception.getCause());
  }

  @Test
  void testCauseConstructorCommand() {
    String message = "message";
    Exception cause = new Exception(message);
    MPDException exception = new MPDException(cause);
    assertNull(exception.getCommand());
    assertEquals("java.lang.Exception: message", exception.getMessage());
    assertEquals(exception.getCause(), cause);
  }

  @Test
  void testMessageCauseConstructorCommand() {
    Exception cause = new Exception();
    String message = "message";
    MPDException exception = new MPDException(message, cause);
    assertNull(exception.getCommand());
    assertEquals(exception.getMessage(), message);
    assertEquals(exception.getCause(), cause);
  }

  @Test
  void testMessageCauseCommandConstructorCommand() {
    Exception cause = new Exception();
    String message = "message";
    String command = "command";
    MPDException exception = new MPDException(message, command, cause);
    assertEquals(exception.getCommand(), command);
    assertEquals(exception.getMessage(), message);
    assertEquals(exception.getCause(), cause);
  }
}
