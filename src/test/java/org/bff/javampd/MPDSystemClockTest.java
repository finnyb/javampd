package org.bff.javampd;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MPDSystemClockTest {
  private Clock clock;

  @BeforeEach
  public void before() {
    clock = new MPDSystemClock();
  }

  @Test
  public void now() {
    LocalDateTime systemTime = LocalDateTime.now();
    LocalDateTime clockTime = clock.now();

    int deviation = 1;
    assertTrue(
      systemTime.minusSeconds(deviation).isBefore(clockTime) &&
      systemTime.plusSeconds(deviation).isAfter(clockTime)
    );
  }

  @Test
  public void min() {
    assertEquals(LocalDateTime.MIN, clock.min());
  }
}
