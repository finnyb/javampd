package org.bff.javampd;

import java.time.LocalDateTime;

public interface Clock {
  LocalDateTime now();

  LocalDateTime min();
}
