package org.bff.javampd.output;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class MPDOutputTest {

    @Test
    void testEquals() {
        MPDOutput output1 = MPDOutput.builder(1).build();
        MPDOutput output2 = MPDOutput.builder(1).build();

        assertThat(output1, is(equalTo(output2)));
    }

    @Test
    void testNotEquals() {
        MPDOutput output1 = MPDOutput.builder(1).build();
        MPDOutput output2 = MPDOutput.builder(2).build();

        assertThat(output1, is(not(equalTo(output2))));
    }

    @Test
    void testEqualsNull() {
        MPDOutput item = MPDOutput.builder(1).build();

        assertThat(item, is(notNullValue()));
    }

    @Test
    void testEqualsSameObject() {
        MPDOutput item = MPDOutput.builder(1).build();

        assertThat(item, is(equalTo(item)));
    }

    @Test
    void testHashCode() {
        MPDOutput output1 = MPDOutput.builder(1).build();
        MPDOutput output2 = MPDOutput.builder(2).build();

        assertThat(output1.hashCode(), is(not(equalTo(output2.hashCode()))));
    }

}
