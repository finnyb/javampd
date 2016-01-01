package org.bff.javampd.monitor;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ThreadedMonitorTest {
    private boolean checked;
    private boolean processedResponse;
    private ThreadedMonitor threadedMonitor;

    @Test
    public void testCheckStatusDelayed() throws Exception {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);

        threadedMonitor.checkStatus();
        assertFalse(checked);
    }

    @Test
    public void testCheckStatus() throws Exception {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);

        threadedMonitor.checkStatus();
        threadedMonitor.checkStatus();

        assertTrue(checked);
    }

    @Test
    public void testShouldntProcessResponseLine() throws Exception {
        Monitor testMonitor = new TestMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);
        threadedMonitor.processResponseLine("");

        assertFalse(processedResponse);
    }

    @Test
    public void testProcessResponseLine() throws Exception {
        Monitor testMonitor = new TestStatusMonitor();

        threadedMonitor = new ThreadedMonitor(testMonitor, 1);
        threadedMonitor.processResponseLine("");

        assertTrue(processedResponse);
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
    }
}