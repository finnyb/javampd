package org.bff.javampd.monitor;

import org.bff.javampd.MPDException;
import org.bff.javampd.server.ServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class StandAloneMonitorThread implements Runnable {
    private static final Logger LOGGER = LoggerFactory.getLogger(StandAloneMonitorThread.class);

    private List<ThreadedMonitor> monitors;
    private ServerStatus serverStatus;
    private ConnectionMonitor connectionMonitor;

    private int delay;
    private int exceptionDelay;
    private boolean stopped;

    /**
     * Creates the monitor thread with the given delay seconds.
     *
     * @param serverStatus      server status
     * @param connectionMonitor connection monitor
     * @param delay             the number of seconds between each poll
     * @param exceptionDelay    the number of seconds to wait should an error occur
     */
    public StandAloneMonitorThread(ServerStatus serverStatus,
                                   ConnectionMonitor connectionMonitor,
                                   int delay,
                                   int exceptionDelay) {
        this.serverStatus = serverStatus;
        this.connectionMonitor = connectionMonitor;
        this.delay = delay;
        this.exceptionDelay = exceptionDelay;
        this.monitors = new ArrayList<>();
    }

    /**
     * Adds the {@link ThreadedMonitor}s only if it doesnt already exist in the monitor list
     *
     * @param monitors the {@link ThreadedMonitor}s to add
     */
    public void addMonitor(ThreadedMonitor... monitors) {
        for (ThreadedMonitor monitor : monitors) {
            if (!this.monitors.contains(monitor)) {
                this.monitors.add(monitor);
            }
        }
    }

    /**
     * Removes the {@link ThreadedMonitor} from the list of monitors
     *
     * @param monitor the {@link ThreadedMonitor} to remove
     */
    public synchronized void removeMonitor(ThreadedMonitor monitor) {
        this.monitors.remove(monitor);
    }

    @Override
    public void run() {
        this.stopped = false;
        loadInitialStatus();

        List<String> response;
        while (!isStopped()) {
            try {
                synchronized (this) {
                    response = new ArrayList<>(serverStatus.getStatus());
                    processResponse(response);
                    monitors.forEach(ThreadedMonitor::checkStatus);
                }
                TimeUnit.SECONDS.sleep(delay);
            } catch (InterruptedException ie) {
                LOGGER.error("Thread interrupted", ie);
                setStopped(true);
            } catch (MPDException mpdException) {
                LOGGER.error("Error while checking statuses", mpdException);
                boolean retry = true;

                while (retry) {
                    try {
                        TimeUnit.SECONDS.sleep(this.exceptionDelay);
                    } catch (InterruptedException ex) {
                        LOGGER.error("StandAloneMonitor interrupted", ex);
                    }

                    try {
                        connectionMonitor.checkStatus();
                        retry = !connectionMonitor.isConnected();
                    } catch (MPDException e) {
                        LOGGER.error("Error checking connection status.", e);
                        throw e;
                    }
                }
            }
        }
    }

    private void processResponse(List<String> response) {
        response.forEach(this::processResponseStatus);
    }

    private void processResponseStatus(String line) {
        for (ThreadedMonitor monitor : monitors) {
            monitor.processResponseLine(line);
        }
    }

    private void loadInitialStatus() {
        try {
            //initial load so no events fired
            List<String> response = new ArrayList<>(serverStatus.getStatus());
            processResponse(response);
        } catch (MPDException ex) {
            LOGGER.error("Problem with initialization", ex);
            throw ex;
        }
    }

    public boolean isStopped() {
        return stopped;
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
