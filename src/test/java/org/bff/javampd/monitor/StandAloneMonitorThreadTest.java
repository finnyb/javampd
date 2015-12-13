package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

    @Test
    public void testAddMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        createMonitor(1, 1).addMonitor(new ThreadedMonitor(monitor, 1));
        runMonitor(1);

        assertTrue(called[0]);
    }

    @Test
    public void testRemoveMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        ThreadedMonitor threadedMonitor = new ThreadedMonitor(monitor, 1);
        createMonitor(1, 1).addMonitor(threadedMonitor);
        runMonitor(1);
        assertTrue(called[0]);

        called[0] = false;
        createMonitor(1, 1).removeMonitor(threadedMonitor);
        runMonitor(1);
        assertFalse(called[0]);
    }

    @Test
    public void testRunInterupted() throws Exception {
        createMonitor(1, 1);
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        thread.interrupt();

        TimeUnit.SECONDS.sleep(2);

        assertFalse(thread.isAlive());
    }

    @Test
    public void testRunError() throws Exception {
        Monitor monitor = () -> {
            throw new MPDException();
        };
        when(connectionMonitor.isConnected()).thenReturn(true);
        createMonitor(1, 1).addMonitor(new ThreadedMonitor(monitor, 1));
        runMonitor(2);
        verify(connectionMonitor).checkStatus();
    }

    @Test(expected = MPDException.class)
    public void testRunConnectionError() throws Exception {
        Monitor monitor = () -> {
            throw new MPDException();
        };
        when(connectionMonitor.isConnected()).thenThrow(new MPDException());
        createMonitor(1, 1).addMonitor(new ThreadedMonitor(monitor, 1));
        standAloneMonitorThread.run();
    }

    @Test(expected = MPDException.class)
    public void testLoadInitialStatusException() throws Exception {
        when(serverStatus.getStatus()).thenThrow(new MPDException());
        createMonitor(1, 1);
        standAloneMonitorThread.run();
    }

    @Test
    public void testIsStopped() throws Exception {
        createMonitor(1, 1);
        runMonitor(1);
        assertTrue(standAloneMonitorThread.isStopped());
    }

    private StandAloneMonitorThread createMonitor(int delay, int exceptionDelay) {
        standAloneMonitorThread = new StandAloneMonitorThread(serverStatus,
                connectionMonitor,
                delay,
                exceptionDelay);

        return standAloneMonitorThread;
    }

    private void runMonitor(int delay) throws Exception {
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        TimeUnit.SECONDS.sleep(delay + 1);
        standAloneMonitorThread.setStopped(true);
    }
}