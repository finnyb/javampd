package org.bff.javampd.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StatusTest {
    @Test
    void lookupUnknownStatus() {
        assertEquals(Status.UNKNOWN, Status.lookup("junk"));
    }

}
