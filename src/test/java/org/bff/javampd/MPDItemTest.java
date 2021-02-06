package org.bff.javampd;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MPDItemTest {

    @Test
    void testEquals() {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertEquals(item1, item2);
    }

    @Test
    void testNotEquals() {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem();

        assertNotEquals(item1, item2);
    }

    @Test
    void testEqualsNull() {
        MPDItem item = new TestItem("item");

        assertNotEquals(item, null);
    }

    @Test
    void testEqualsSameObject() {
        MPDItem item = new TestItem("item");

        assertTrue(item.equals(item));
    }

    @Test
    void testEqualsDifferentClass() {
        MPDItem item = new TestItem("item");

        assertNotEquals("", item);
    }

    @Test
    void testHashCode() {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    void testCompareToLessThanZero() {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item2");

        assertTrue(item1.compareTo(item2) < 0);
    }

    @Test
    void testCompareToGreaterThanZero() {
        MPDItem item1 = new TestItem("item2");
        MPDItem item2 = new TestItem("item1");

        assertTrue(item1.compareTo(item2) > 0);
    }

    @Test
    void testCompareToEquals() {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertTrue(item1.compareTo(item2) == 0);
    }

    @Test
    void testToString() {
        String name = "name";
        MPDItem item = new TestItem(name);

        assertEquals(name, item.toString());
    }

    private static class TestItem extends MPDItem {
        TestItem() {
            super();
        }

        TestItem(String name) {
            super(name);
        }
    }
}
