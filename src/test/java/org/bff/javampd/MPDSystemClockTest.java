package org.bff.javampd;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

public class MPDSystemClockTest {
    private Clock clock;

    @Before
    public void before() {
        clock = new MPDSystemClock();
    }

    @Test
    public void now() throws Exception {
        LocalDateTime systemTime = LocalDateTime.now();
        LocalDateTime clockTime = clock.now();

        int deviation = 1;
        assertTrue(systemTime.minusSeconds(deviation).isBefore(clockTime) && systemTime.plusSeconds(deviation).isAfter(clockTime));
    }

    @Test
    public void min() throws Exception {
        assertEquals(LocalDateTime.MIN, clock.min());
    }
}