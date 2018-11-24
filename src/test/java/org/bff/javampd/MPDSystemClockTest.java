package org.bff.javampd;

import java.util.Date;
import org.joda.time.LocalDateTime;
import org.junit.Before;
import org.junit.Test;

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
        assertEquals(LocalDateTime.fromDateFields(new Date(0)), clock.min());
    }
}