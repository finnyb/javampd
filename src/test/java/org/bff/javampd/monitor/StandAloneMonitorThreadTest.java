package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StandAloneMonitorThreadTest {

    @Mock
    private ServerStatus serverStatus;
    @Mock
    private ConnectionMonitor connectionMonitor;

    private StandAloneMonitorThread standAloneMonitorThread;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {
        standAloneMonitorThread.setStopped(true);
    }

    @Test
    public void testAddMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();

        for (int i = 1; i < 5000; i++) {
            if (called[0]) {
                break;
            }
            sleep(1);
        }

        assertTrue(called[0]);
    }

    @Test
    public void testRemoveMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        ThreadedMonitor threadedMonitor = new ThreadedMonitor(monitor, 0);
        StandAloneMonitorThread monitorThread = createMonitor(0, 0);
        monitorThread.addMonitor(threadedMonitor);
        runMonitor();

        int count = 0;
        for (int i = 1; i < 5000; i++) {
            if (called[0]) {
                count = i;
                break;
            }
            sleep(1);
        }

        assertTrue(called[0]);
        monitorThread.removeMonitor(threadedMonitor);

        called[0] = false;
        for (int i = 1; i < (count + 5); i++) {
            sleep(1);
        }

        assertFalse(called[0]);
    }

    @Test
    public void testRunInterupted() throws Exception {
        createMonitor(1, 1);
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        thread.interrupt();

        for (int i = 1; i < 5000; i++) {
            if (!thread.isAlive()) {
                break;
            }
            sleep(1);
        }

        assertFalse(thread.isAlive());
    }

    @Test
    public void testRunError() throws Exception {
        Monitor monitor = () -> {
            throw new MPDException();
        };
        when(connectionMonitor.isConnected()).thenReturn(true);
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();
        verify(connectionMonitor, atLeastOnce()).checkStatus();
    }

    @Test(expected = MPDException.class)
    public void testRunConnectionError() throws Exception {
        Monitor monitor = () -> {
            throw new MPDException();
        };
        when(connectionMonitor.isConnected()).thenThrow(new MPDException());
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        standAloneMonitorThread.run();
    }

    @Test(expected = MPDException.class)
    public void testLoadInitialStatusException() throws Exception {
        when(serverStatus.getStatus()).thenThrow(new MPDException());
        createMonitor(0, 0);
        standAloneMonitorThread.run();
    }

    @Test
    public void testIsStopped() throws Exception {
        createMonitor(0, 0);
        runMonitor();

        assertFalse(standAloneMonitorThread.isStopped());
        standAloneMonitorThread.setStopped(true);

        for (int i = 1; i < 5000; i++) {
            if (standAloneMonitorThread.isStopped()) {
                break;
            }
            sleep(1);
        }

        assertTrue(standAloneMonitorThread.isStopped());
    }

    private StandAloneMonitorThread createMonitor(int delay, int exceptionDelay) {
        standAloneMonitorThread = new StandAloneMonitorThread(serverStatus,
                connectionMonitor,
                delay,
                exceptionDelay);

        return standAloneMonitorThread;
    }

    private void runMonitor() throws Exception {
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();
    }

    private void sleep(long millis) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep((millis));
    }
}