package org.bff.javampd.monitor;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ThreadedMonitorTest {
    private boolean checked;
    private boolean processedResponse;
    private boolean reset;

    private ThreadedMonitor threadedMonitor;

    @Test
    public void testCheckStatusDelayed() {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);

        threadedMonitor.checkStatus();
        assertFalse(checked);
    }

    @Test
    public void testCheckStatus() {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);

        threadedMonitor.checkStatus();
        threadedMonitor.checkStatus();

        assertTrue(checked);
    }

    @Test
    public void testShouldntProcessResponseLine() {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);
        threadedMonitor.processResponseLine("");

        assertFalse(processedResponse);
    }

    @Test
    public void testProcessResponseLine() {
        Monitor testMonitor = new TestStatusMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);
        threadedMonitor.processResponseLine("");

        assertTrue(processedResponse);
    }

    @Test
    public void testReset() {
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