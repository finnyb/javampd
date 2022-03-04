package org.bff.javampd.monitor;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class ThreadedMonitorTest {
  private boolean checked;
  private boolean processedResponse;
  private boolean reset;

  private ThreadedMonitor threadedMonitor;

  @Test
  void testCheckStatusDelayed() {
    Monitor testMonitor = new TestMonitor();

    threadedMonitor = new ThreadedMonitor(testMonitor, 1);

    threadedMonitor.checkStatus();
    assertFalse(checked);
  }

  @Test
  void testCheckStatus() {
    Monitor testMonitor = new TestMonitor();

    threadedMonitor = new ThreadedMonitor(testMonitor, 1);

    threadedMonitor.checkStatus();
    threadedMonitor.checkStatus();

    assertTrue(checked);
  }

  @Test
  void testShouldntProcessResponseLine() {
    Monitor testMonitor = new TestMonitor();

    threadedMonitor = new ThreadedMonitor(testMonitor, 1);
    threadedMonitor.processResponseLine("");

    assertFalse(processedResponse);
  }

  @Test
  void testProcessResponseLine() {
    Monitor testMonitor = new TestStatusMonitor();

    threadedMonitor = new ThreadedMonitor(testMonitor, 1);
    threadedMonitor.processResponseLine("");

    assertTrue(processedResponse);
  }

  @Test
  void testReset() {
    Monitor testMonitor = new TestStatusMonitor();

    threadedMonitor = new ThreadedMonitor(testMonitor, 1);
    threadedMonitor.reset();

    assertTrue(reset);
  }

  private class TestMonitor implements Monitor {

    @Override
    public void checkStatus() {
      checked = true;
    }
  }

  private class TestStatusMonitor implements StatusMonitor {

    @Override
    public void checkStatus() {
      checked = true;
    }

    @Override
    public void processResponseStatus(String line) {
      processedResponse = true;
    }

    @Override
    public void reset() {
      reset = true;
    }
  }
}
