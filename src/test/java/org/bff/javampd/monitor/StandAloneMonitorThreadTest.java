package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertFalse;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandAloneMonitorThreadTest {

    @Mock
    private ServerStatus serverStatus;
    @Mock
    private ConnectionMonitor connectionMonitor;

    private StandAloneMonitorThread standAloneMonitorThread;

    @After
    public void tearDown() throws Exception {
        standAloneMonitorThread.setStopped(true);
    }

    @Test
    public void testInitialStatus() throws Exception {
        final boolean[] called = new boolean[1];
        List<String> returnStatus1 = new ArrayList<>();
        returnStatus1.add("volume: 1");

        List<String> returnStatus2 = new ArrayList<>();
        returnStatus2.add("volume: 2");

        when(serverStatus.getStatus()).thenReturn(returnStatus1).thenReturn(returnStatus2);
        VolumeMonitor volumeMonitor = new MPDVolumeMonitor();
        volumeMonitor.addVolumeChangeListener(event -> called[0] = true);
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(volumeMonitor, 0));
        runMonitor();

        await().until(() -> called[0]);
    }

    @Test
    public void testAddMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();

        await().until(() -> called[0]);
    }

    @Test
    public void testRemoveMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        ThreadedMonitor threadedMonitor = new ThreadedMonitor(monitor, 0);
        StandAloneMonitorThread monitorThread = createMonitor(0, 0);
        monitorThread.addMonitor(threadedMonitor);
        runMonitor();

        await().until(() -> called[0]);
        monitorThread.removeMonitor(threadedMonitor);

        called[0] = false;
        await().until(() -> !called[0]);
    }

    @Test
    public void testRunInterupted() throws Exception {
        createMonitor(1, 1);
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        thread.interrupt();

        await().until(() -> !thread.isAlive());
    }

    @Test
    public void testRunConnectionErrorWithInterrupt() throws Exception {
        final int[] count = {0};
        Monitor monitor = () -> {
            ++count[0];
            throw new MPDException("Test Exception");
        };

        when(connectionMonitor.isConnected()).thenReturn(true);
        createMonitor(0, 5000).addMonitor(new ThreadedMonitor(monitor, 0));
        Thread thread = runMonitor();

        thread.interrupt();

        await().until(() -> !thread.isAlive());
    }

    @Test
    public void testRunError() throws Exception {
        final int[] count = {0};
        Monitor monitor = () -> {
            ++count[0];
            throw new MPDException("Test Exception");
        };
        when(connectionMonitor.isConnected()).thenReturn(true);
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();

        await().until(() -> count[0] > 1);
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

        await().until(() -> !standAloneMonitorThread.isDone());

        standAloneMonitorThread.setStopped(true);

        await().until(() -> standAloneMonitorThread.isDone());
    }

    @Test
    public void testIsLoaded() throws Exception {
        createMonitor(0, 0);
        assertFalse(standAloneMonitorThread.isInitialized());
        runMonitor();
        await().until(() -> standAloneMonitorThread.isInitialized());
    }

    private StandAloneMonitorThread createMonitor(int delay, int exceptionDelay) {
        standAloneMonitorThread = new StandAloneMonitorThread(serverStatus,
                connectionMonitor,
                delay,
                exceptionDelay);

        return standAloneMonitorThread;
    }

    private Thread runMonitor() throws Exception {
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        return thread;
    }
}