package org.bff.javampd.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StatusTest {
    @Test
    public void lookupUnknownStatus() {
        assertEquals(Status.UNKNOWN, Status.lookupStatus("junk"));
    }

}