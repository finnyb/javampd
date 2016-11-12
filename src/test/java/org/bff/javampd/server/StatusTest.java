package org.bff.javampd.server;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class StatusTest {
    @Test
    public void lookupUnknownStatus() throws Exception {
        assertEquals(Status.UNKNOWN, Status.lookupStatus("junk"));
    }

}