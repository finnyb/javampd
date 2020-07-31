package org.bff.javampd.output;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MPDOutputTest {

  @Test
  public void testEquals() {
    MPDOutput output1 = new MPDOutput(1);
    MPDOutput output2 = new MPDOutput(1);

    assertEquals(output1, output2);
  }

  @Test
  public void testNotEquals() {
    MPDOutput output1 = new MPDOutput(1);
    MPDOutput output2 = new MPDOutput(2);

    assertNotEquals(output1, output2);
  }

  @Test
  public void testEqualsNull() {
    MPDOutput item = new MPDOutput(1);

    assertNotEquals(item, null);
  }

  @Test
  public void testEqualsSameObject() {
    MPDOutput item = new MPDOutput(1);

    assertTrue(item.equals(item));
  }

  @Test
  public void testHashCode() {
    MPDOutput output1 = new MPDOutput(1);
    MPDOutput output2 = new MPDOutput(2);

    assertEquals(output1.hashCode(), output2.hashCode());
  }
}
