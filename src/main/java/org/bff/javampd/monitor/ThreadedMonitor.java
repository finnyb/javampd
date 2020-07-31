package org.bff.javampd.monitor;

/**
 * Threaded version of a {@link Monitor}
 */
public class ThreadedMonitor {
  private Monitor monitor;
  private int delay;
  private int count;

  /**
   * Threaded version of {@link Monitor}
   *
   * @param monitor the {@link Monitor}
   * @param delay   The number of seconds to delay before performing the check status.  If your
   *                {@link #checkStatus} is expensive this should be a larger number.
   */
  ThreadedMonitor(Monitor monitor, int delay) {
    this.monitor = monitor;
    this.delay = delay;
  }

  public void checkStatus() {
    if (count++ == delay) {
      count = 0;
      monitor.checkStatus();
    }
  }

  public void processResponseLine(String line) {
    if (monitor instanceof StatusMonitor) {
      ((StatusMonitor) monitor).processResponseStatus(line);
    }
  }

  public void reset() {
    if (monitor instanceof StatusMonitor) {
      ((StatusMonitor) monitor).reset();
    }
  }
}
