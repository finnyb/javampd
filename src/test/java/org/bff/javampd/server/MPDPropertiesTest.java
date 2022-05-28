package org.bff.javampd.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.io.InputStream;
import org.bff.javampd.MPDException;
import org.junit.jupiter.api.Test;

class MPDPropertiesTest {

  @Test
  void testGetPropertyString() {
    TestProperties testProperties = new TestProperties();
    assertEquals("OK", testProperties.getOk());
  }

  @Test
  void testBadProperties() {
    assertThrows(MPDException.class, TestBadProperties::new);
  }

  @Test
  void testBadPropertiesLoad() {
    assertThrows(MPDException.class, TestBadPropertiesLoad::new);
  }

  private static class TestProperties extends MPDProperties {
    String getOk() {
      return getPropertyString("cmd.response.ok");
    }
  }

  private static class TestBadProperties extends MPDProperties {
    @Override
    protected void loadValues(String propertiesResourceLocation) {
      super.loadValues("badLocation");
    }
  }

  private static class TestBadPropertiesLoad extends MPDProperties {
    @Override
    protected void loadProperties(InputStream inputStream) throws IOException {
      throw new IOException();
    }
  }
}
