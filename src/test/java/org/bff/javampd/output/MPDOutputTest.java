package org.bff.javampd.output;

import org.junit.Test;

import static org.junit.Assert.*;

public class MPDOutputTest {

    @Test
    public void testEquals() throws Exception {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(1);

        assertEquals(output1, output2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertNotEquals(output1, output2);
    }

    @Test
    public void testEqualsNull() throws Exception {
        MPDOutput item = new MPDOutput(1);

        assertNotEquals(item, null);
    }

    @Test
    public void testEqualsSameObject() throws Exception {
        MPDOutput item = new MPDOutput(1);

        assertTrue(item.equals(item));
    }

    @Test
    public void testHashCode() throws Exception {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertEquals(output1.hashCode(), output2.hashCode());
    }

}