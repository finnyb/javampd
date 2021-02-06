package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StandAloneMonitorThreadTest {

    @Mock
    private ServerStatus serverStatus;
    @Mock
    private ConnectionMonitor connectionMonitor;

    private StandAloneMonitorThread standAloneMonitorThread;

    @AfterEach
    void tearDown() {
        standAloneMonitorThread.setStopped(true);
    }

    @Test
    void testInitialStatus() {
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
    void testAddMonitor() {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();

        await().until(() -> called[0]);
    }

    @Test
    void testRemoveMonitor() {
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
    void testRunInterupted() {
        createMonitor(1, 1);
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        thread.interrupt();

        await().until(() -> !thread.isAlive());
    }

    @Test
    void testRunConnectionErrorWithInterrupt() {
        final int[] count = {0};
        Monitor monitor = () -> {
            ++count[0];
            throw new MPDException("Test Exception");
        };

        createMonitor(0, 5000).addMonitor(new ThreadedMonitor(monitor, 0));
        Thread thread = runMonitor();

        thread.interrupt();

        await().until(() -> !thread.isAlive());
    }

    @Test
    void testRunError() {
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

    @Test
    void testRunConnectionError() {
        Monitor monitor = () -> {
            throw new MPDException();
        };
        when(connectionMonitor.isConnected()).thenThrow(new MPDException());
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        assertThrows(MPDException.class, () -> standAloneMonitorThread.run());
    }

    @Test
    void testLoadInitialStatusException() {
        when(serverStatus.getStatus()).thenThrow(new MPDException());
        createMonitor(0, 0);
        assertThrows(MPDException.class, () -> standAloneMonitorThread.run());
    }

    @Test
    void testIsStopped() {
        createMonitor(0, 0);
        runMonitor();

        await().until(() -> !standAloneMonitorThread.isDone());

        standAloneMonitorThread.setStopped(true);

        await().until(() -> standAloneMonitorThread.isDone());
    }

    @Test
    void testIsLoaded() {
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

    private Thread runMonitor() {
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        return thread;
    }
}
