package org.bff.javampd.output;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MPDOutputTest {

    @Test
    void testEquals() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(1);

        assertThat(output1, is(equalTo(output2)));
    }

    @Test
    void testNotEquals() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertThat(output1, is(not(equalTo(output2))));
    }

    @Test
    void testEqualsNull() {
        MPDOutput item = new MPDOutput(1);

        assertThat(item, is(notNullValue()));
    }

    @Test
    void testEqualsSameObject() {
        MPDOutput item = new MPDOutput(1);

        assertThat(item, is(equalTo(item)));
    }

    @Test
    void testHashCode() {
        MPDOutput output1 = new MPDOutput(1);
        MPDOutput output2 = new MPDOutput(2);

        assertThat(output1.hashCode(), is(not(equalTo(output2.hashCode()))));
    }

}
