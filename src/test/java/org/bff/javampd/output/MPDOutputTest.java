package org.bff.javampd.output;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MPDOutputTest {

    @Test
    void testEquals() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(1);

        assertEquals(output1, output2);
    }

    @Test
    void testNotEquals() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertNotEquals(output1, output2);
    }

    @Test
    void testEqualsNull() {
        MPDOutput item = new MPDOutput(1);

        assertNotEquals(item, null);
    }

    @Test
    void testEqualsSameObject() {
        MPDOutput item = new MPDOutput(1);

        assertTrue(item.equals(item));
    }

    @Test
    void testHashCode() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertNotEquals(output1.hashCode(), output2.hashCode());
    }

}
