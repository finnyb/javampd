package org.bff.javampd;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class MPDItemTest {

  @Test
  public void testEquals() {
    MPDItem item1 = new TestItem("item1");
    MPDItem item2 = new TestItem("item1");

    assertEquals(item1, item2);
  }

  @Test
  public void testNotEquals() {
    MPDItem item1 = new TestItem("item1");
    MPDItem item2 = new TestItem();

    assertNotEquals(item1, item2);
  }

  @Test
  public void testEqualsNull() {
    MPDItem item = new TestItem("item");

    assertNotEquals(item, null);
  }

  @Test
  public void testEqualsSameObject() {
    MPDItem item = new TestItem("item");

    assertTrue(item.equals(item));
  }

  @Test
  public void testEqualsDifferentClass() {
    MPDItem item = new TestItem("item");

    assertNotEquals("", item);
  }

  @Test
  public void testHashCode() {
    MPDItem item1 = new TestItem("item1");
    MPDItem item2 = new TestItem("item1");

    assertEquals(item1.hashCode(), item2.hashCode());
  }

  @Test
  public void testHashCodeNull() {
    MPDItem item = new TestItem(null);

    assertEquals(0, item.hashCode());
  }

  @Test
  public void testCompareToLessThanZero() {
    MPDItem item1 = new TestItem("item1");
    MPDItem item2 = new TestItem("item2");

    assertTrue(item1.compareTo(item2) < 0);
  }

  @Test
  public void testCompareToGreaterThanZero() {
    MPDItem item1 = new TestItem("item2");
    MPDItem item2 = new TestItem("item1");

    assertTrue(item1.compareTo(item2) > 0);
  }

  @Test
  public void testCompareToEquals() {
    MPDItem item1 = new TestItem("item1");
    MPDItem item2 = new TestItem("item1");

    assertTrue(item1.compareTo(item2) == 0);
  }

  @Test
  public void testToString() {
    String name = "name";
    MPDItem item = new TestItem(name);

    assertEquals(name, item.toString());
  }

  private static class TestItem extends MPDItem {

    public TestItem() {
      super();
    }

    public TestItem(String name) {
      super(name);
    }
  }
}
