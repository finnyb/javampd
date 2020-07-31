package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ServerPropertiesTest {
  private ServerProperties serverProperties;

  @BeforeEach
  public void setUp() {
    serverProperties = new ServerProperties();
  }

  @Test
  public void getClearError() {
    assertEquals("clearerror", serverProperties.getClearError());
  }

  @Test
  public void getStatus() {
    assertEquals("status", serverProperties.getStatus());
  }

  @Test
  public void getStats() {
    assertEquals("stats", serverProperties.getStats());
  }

  @Test
  public void getPing() {
    assertEquals("ping", serverProperties.getPing());
  }

  @Test
  public void getPassword() {
    assertEquals("password", serverProperties.getPassword());
  }

  @Test
  public void getClose() {
    assertEquals("close", serverProperties.getClose());
  }

  @Test
  public void getStartBulk() {
    assertEquals("command_list_ok_begin", serverProperties.getStartBulk());
  }

  @Test
  public void getEndBulk() {
    assertEquals("command_list_end", serverProperties.getEndBulk());
  }

  @Test
  public void getEncoding() {
    assertEquals("UTF-8", serverProperties.getEncoding());
  }
}
