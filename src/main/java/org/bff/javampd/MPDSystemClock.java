package org.bff.javampd;

import java.util.Date;
import org.joda.time.LocalDateTime;


public class MPDSystemClock implements Clock {
    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }

    @Override
    public LocalDateTime min() {
        return LocalDateTime.fromDateFields(new Date(0));
    }
}
