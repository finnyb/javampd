package org.bff.javampd;

import org.junit.Test;

import static org.junit.Assert.*;


public class MPDItemTest {

    @Test
    public void testEquals() throws Exception {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertEquals(item1, item2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem();

        assertNotEquals(item1, item2);
    }

    @Test
    public void testHashCode() throws Exception {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertEquals(item1.hashCode(), item2.hashCode());
    }

    @Test
    public void testCompareToLessThanZero() throws Exception {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item2");

        assertTrue(item1.compareTo(item2) < 0);
    }

    @Test
    public void testCompareToGreaterThanZero() throws Exception {
        MPDItem item1 = new TestItem("item2");
        MPDItem item2 = new TestItem("item1");

        assertTrue(item1.compareTo(item2) > 0);
    }

    @Test
    public void testCompareToEquals() throws Exception {
        MPDItem item1 = new TestItem("item1");
        MPDItem item2 = new TestItem("item1");

        assertTrue(item1.compareTo(item2) == 0);
    }

    private class TestItem extends MPDItem {
        public TestItem() {
            super();
        }

        public TestItem(String name) {
            super(name);
        }
    }
}