package org.bff.javampd;

import org.joda.time.LocalDateTime;


public interface Clock {
    LocalDateTime now();

    LocalDateTime min();
}
