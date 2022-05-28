package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ServerPropertiesTest {
  private ServerProperties serverProperties;

  @BeforeEach
  void setUp() {
    serverProperties = new ServerProperties();
  }

  @Test
  void getClearError() {
    assertEquals("clearerror", serverProperties.getClearError());
  }

  @Test
  void getStatus() {
    assertEquals("status", serverProperties.getStatus());
  }

  @Test
  void getStats() {
    assertEquals("stats", serverProperties.getStats());
  }

  @Test
  void getPing() {
    assertEquals("ping", serverProperties.getPing());
  }

  @Test
  void getPassword() {
    assertEquals("password", serverProperties.getPassword());
  }

  @Test
  void getClose() {
    assertEquals("close", serverProperties.getClose());
  }

  @Test
  void getStartBulk() {
    assertEquals("command_list_ok_begin", serverProperties.getStartBulk());
  }

  @Test
  void getEndBulk() {
    assertEquals("command_list_end", serverProperties.getEndBulk());
  }

  @Test
  void getEncoding() {
    assertEquals("UTF-8", serverProperties.getEncoding());
  }
}
