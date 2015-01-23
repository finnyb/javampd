package org.bff.javampd.monitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bff.javampd.MPDException;
import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.player.PlayerBasicChangeEvent;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.player.TrackPositionChangeListener;
import org.bff.javampd.player.VolumeChangeListener;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.MPDErrorListener;
import org.bff.javampd.server.ServerStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * MPDStandAloneMonitor monitors a MPD connection by querying the status and
 * statistics of the MPD server at given getDelay intervals.  As statistics change
 * appropriate events are fired indicating these changes.  If more detailed
 * events are desired attach listeners to the different controllers of a
 * connection.
 *
 * @author Bill
 * @version 1.0
 */
@Singleton
public class MPDStandAloneMonitor
        implements StandAloneMonitor, PlayerBasicChangeListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(MPDStandAloneMonitor.class);

    private ServerStatus serverStatus;
    private OutputMonitor outputMonitor;
    private TrackMonitor trackMonitor;
    private ConnectionMonitor connectionMonitor;
    private VolumeMonitor volumeMonitor;
    private PlayerMonitor playerMonitor;
    private BitrateMonitor bitrateMonitor;
    private PlaylistMonitor playlistMonitor;
    private ErrorMonitor errorMonitor;
    private MonitorProperties monitorProperties;

    private List<ThreadedMonitor> monitors;

    private boolean stopped;

    /**
     * Creates a new instance of MPDStandAloneMonitor using the given getDelay interval
     * for queries.
     */
    @Inject
    MPDStandAloneMonitor(ServerStatus serverStatus,
                         OutputMonitor outputMonitor,
                         TrackMonitor trackMonitor,
                         ConnectionMonitor connectionMonitor,
                         VolumeMonitor volumeMonitor,
                         PlayerMonitor playerMonitor,
                         BitrateMonitor bitrateMonitor,
                         PlaylistMonitor playlistMonitor,
                         ErrorMonitor errorMonitor,
                         MonitorProperties monitorProperties) {
        this.serverStatus = serverStatus;
        this.outputMonitor = outputMonitor;
        this.trackMonitor = trackMonitor;
        this.connectionMonitor = connectionMonitor;
        this.volumeMonitor = volumeMonitor;
        this.playerMonitor = playerMonitor;
        this.bitrateMonitor = bitrateMonitor;
        this.playlistMonitor = playlistMonitor;
        this.errorMonitor = errorMonitor;
        this.monitorProperties = monitorProperties;
        this.monitors = new ArrayList<>();
    }

    private void loadMonitors() {
        monitors.add(new ThreadedMonitor(trackMonitor, monitorProperties.getTrackDelay()));
        monitors.add(new ThreadedMonitor(playerMonitor, monitorProperties.getPlayerDelay()));
        monitors.add(new ThreadedMonitor(errorMonitor, monitorProperties.getErrorDelay()));
        monitors.add(new ThreadedMonitor(playlistMonitor, monitorProperties.getPlaylistDelay()));
        monitors.add(new ThreadedMonitor(connectionMonitor, monitorProperties.getConnectionDelay()));
        monitors.add(new ThreadedMonitor(bitrateMonitor, monitorProperties.getBitrateDelay()));
        monitors.add(new ThreadedMonitor(volumeMonitor, monitorProperties.getVolumeDelay()));
        monitors.add(new ThreadedMonitor(outputMonitor, monitorProperties.getOutputDelay()));
    }

    /**
     * Adds a {@link org.bff.javampd.player.TrackPositionChangeListener} to this object to receive
     * {@link org.bff.javampd.player.TrackPositionChangeEvent}s.
     *
     * @param tpcl the TrackPositionChangeListener to add
     */
    @Override
    public synchronized void addTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackMonitor.addTrackPositionChangeListener(tpcl);
    }

    /**
     * Removes a {@link TrackPositionChangeListener} from this object.
     *
     * @param tpcl the TrackPositionChangeListener to remove
     */
    @Override
    public synchronized void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackMonitor.removeTrackPositionChangeListener(tpcl);
    }

    /**
     * Adds a {@link org.bff.javampd.server.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.server.ConnectionChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListener to add
     */
    @Override
    public synchronized void addConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionMonitor.addConnectionChangeListener(ccl);
    }

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
    @Override
    public synchronized void removeConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionMonitor.removeConnectionChangeListener(ccl);
    }

    @Override
    public synchronized void addPlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerMonitor.addPlayerChangeListener(pcl);
    }

    @Override
    public synchronized void removePlayerChangeListener(PlayerBasicChangeListener pcl) {
        playerMonitor.removePlayerChangeListener(pcl);
    }

    @Override
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volumeMonitor.addVolumeChangeListener(vcl);
    }

    @Override
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volumeMonitor.removeVolumeChangedListener(vcl);
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputMonitor.addOutputChangeListener(vcl);
    }

    @Override
    public synchronized void removeOutputChangedListener(OutputChangeListener vcl) {
        outputMonitor.removeOutputChangedListener(vcl);
    }

    @Override
    public synchronized void addPlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistMonitor.addPlaylistChangeListener(pcl);
    }

    @Override
    public synchronized void removePlaylistStatusChangedListener(PlaylistBasicChangeListener pcl) {
        playlistMonitor.removePlaylistStatusChangedListener(pcl);
    }

    @Override
    public synchronized void addMPDErrorListener(MPDErrorListener el) {
        errorMonitor.addMPDErrorListener(el);
    }

    @Override
    public synchronized void removeMPDErrorListener(MPDErrorListener el) {
        errorMonitor.removeMPDErrorListener(el);
    }

    @Override
    public void run() {
        loadMonitors();
        loadInitialStatus();

        int delay = monitorProperties.getMonitorDelay();
        List<String> response;
        while (!isStopped()) {
            try {
                synchronized (this) {
                    response = new ArrayList<>(serverStatus.getStatus());
                    processResponse(response);
                    for (ThreadedMonitor monitor : monitors) {
                        monitor.checkStatus();
                    }
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
                        TimeUnit.SECONDS.sleep(monitorProperties.getExceptionDelay());
                    } catch (InterruptedException ex) {
                        LOGGER.error("StandAloneMonitor interrupted", ex);
                    }

                    try {
                        connectionMonitor.checkStatus();
                        retry = !connectionMonitor.isConnected();
                    } catch (MPDException e) {
                        LOGGER.error("Error checking connection status.", e);
                    }
                }
            }
        }
    }

    private void loadInitialStatus() {
        try {
            //initial load so no events fired
            List<String> response = new ArrayList<>(serverStatus.getStatus());
            processResponse(response);
        } catch (MPDException ex) {
            LOGGER.error("Problem with initialization", ex);
        }
    }

    @Override
    public void start() {
        setStopped(false);
        Executors.newSingleThreadExecutor().execute(this);
    }

    @Override
    public void stop() {
        setStopped(true);
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    private void setStopped(boolean stopped) {
        this.stopped = stopped;
    }

    private void processResponse(List<String> response) {
        response.forEach(this::processResponseStatus);
    }

    private void processResponseStatus(String line) {
        for (ThreadedMonitor monitor : monitors) {
            monitor.processResponseLine(line);
        }
    }

    @Override
    public void playerBasicChange(PlayerBasicChangeEvent event) {
        if (event.getStatus() == PlayerBasicChangeEvent.Status.PLAYER_STOPPED) {
            processStoppedStatus();
        }
    }

    private void processStoppedStatus() {
        trackMonitor.resetElapsedTime();
        playlistMonitor.playerStopped();
    }

    private class ThreadedMonitor {
        private Monitor monitor;
        private int delay;
        private int count;

        /**
         * Threaded version of {@link Monitor}
         *
         * @param monitor the {@link Monitor}
         * @param delay   The number of seconds to delay before performing the check status.  If your
         *                #checkStatus is expensive this should be a larger number.
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
    }
}