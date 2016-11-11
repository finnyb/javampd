package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StandAloneMonitorThreadTest {

    public static final int MAX_WAIT_TIME = 60000;
            
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

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
            if (called[0]) {
                break;
            }
            sleep(1);
        }

        assertTrue(called[0]);
    }

    @Test
    public void testAddMonitor() throws Exception {
        final boolean[] called = new boolean[1];
        Monitor monitor = () -> called[0] = true;
        createMonitor(0, 0).addMonitor(new ThreadedMonitor(monitor, 0));
        runMonitor();

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
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
        for (int i = 1; i < MAX_WAIT_TIME; i++) {
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

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
            if (!thread.isAlive()) {
                break;
            }
            sleep(1);
        }

        assertFalse(thread.isAlive());
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

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
            if (!thread.isAlive()) {
                break;
            }
            sleep(1);
        }

        assertFalse(thread.isAlive());
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

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
            if (count[0] > 1) {
                break;
            }
            sleep(1);
        }

        assertTrue(count[0] > 1);
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

        for (int i = 1; i < MAX_WAIT_TIME; i++) {
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

    private Thread runMonitor() throws Exception {
        Thread thread = new Thread(standAloneMonitorThread);
        thread.start();

        return thread;
    }

    private void sleep(long millis) throws InterruptedException {
        TimeUnit.MILLISECONDS.sleep((millis));
    }
}