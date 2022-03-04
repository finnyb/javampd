package org.bff.javampd;

import java.time.LocalDateTime;

public class MPDSystemClock implements Clock {
  @Override
  public LocalDateTime now() {
    return LocalDateTime.now();
  }

  @Override
  public LocalDateTime min() {
    return LocalDateTime.MIN;
  }
}
