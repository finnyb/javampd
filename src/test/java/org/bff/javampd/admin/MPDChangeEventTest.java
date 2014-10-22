package org.bff.javampd.admin;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MPDChangeEventTest {

    @Test
    public void testConstructor2Params() {
        Object source = new Object();
        MPDChangeEvent.Event event = MPDChangeEvent.Event.MPD_KILLED;

        MPDChangeEvent changeEvent = new MPDChangeEvent(source, event);
        assertEquals(source, changeEvent.getSource());
        assertEquals(event, changeEvent.getEvent());
        assertEquals(null, changeEvent.getMsg());
    }

    @Test
    public void testConstructor3Params() {
        Object source = new Object();
        MPDChangeEvent.Event event = MPDChangeEvent.Event.MPD_KILLED;
        String testMessage = "testMessage";

        MPDChangeEvent changeEvent = new MPDChangeEvent(source, event, testMessage);
        assertEquals(source, changeEvent.getSource());
        assertEquals(event, changeEvent.getEvent());
        assertEquals(testMessage, changeEvent.getMsg());
    }

}