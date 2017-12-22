package org.bff.javampd.monitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bff.javampd.output.OutputChangeListener;
import org.bff.javampd.player.*;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.ErrorListener;
import org.bff.javampd.server.ServerStatus;

import java.util.concurrent.Executors;

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

    private StandAloneMonitorThread standAloneMonitorThread;

    private OutputMonitor outputMonitor;
    private ErrorMonitor errorMonitor;
    private ConnectionMonitor connectionMonitor;
    private PlayerMonitor playerMonitor;
    private TrackMonitor trackMonitor;
    private PlaylistMonitor playlistMonitor;

    private MonitorProperties monitorProperties;

    @Inject
    MPDStandAloneMonitor(ServerStatus serverStatus,
                         OutputMonitor outputMonitor,
                         TrackMonitor trackMonitor,
                         ConnectionMonitor connectionMonitor,
                         PlayerMonitor playerMonitor,
                         PlaylistMonitor playlistMonitor,
                         ErrorMonitor errorMonitor) {
        this.monitorProperties = new MonitorProperties();
        this.outputMonitor = outputMonitor;
        this.trackMonitor = trackMonitor;
        this.connectionMonitor = connectionMonitor;
        this.playerMonitor = playerMonitor;
        this.playlistMonitor = playlistMonitor;
        this.errorMonitor = errorMonitor;

        this.standAloneMonitorThread = new StandAloneMonitorThread(serverStatus,
                connectionMonitor,
                monitorProperties.getMonitorDelay(),
                monitorProperties.getExceptionDelay());
        createMonitors();
    }

    private void createMonitors() {
        standAloneMonitorThread.addMonitor(
                new ThreadedMonitor(trackMonitor, monitorProperties.getTrackDelay()),
                new ThreadedMonitor(playerMonitor, monitorProperties.getPlayerDelay()),
                new ThreadedMonitor(errorMonitor, monitorProperties.getErrorDelay()),
                new ThreadedMonitor(playlistMonitor, monitorProperties.getPlaylistDelay()),
                new ThreadedMonitor(connectionMonitor, monitorProperties.getConnectionDelay()),
                new ThreadedMonitor(outputMonitor, monitorProperties.getOutputDelay()));
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
        playerMonitor.addVolumeChangeListener(vcl);
    }

    @Override
    public synchronized void removeVolumeChangeListener(VolumeChangeListener vcl) {
        playerMonitor.removeVolumeChangeListener(vcl);
    }

    @Override
    public synchronized void addBitrateChangeListener(BitrateChangeListener bcl) {
        playerMonitor.addBitrateChangeListener(bcl);
    }

    @Override
    public synchronized void removeBitrateChangeListener(BitrateChangeListener bcl) {
        playerMonitor.removeBitrateChangeListener(bcl);
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputMonitor.addOutputChangeListener(vcl);
    }

    @Override
    public synchronized void removeOutputChangeListener(OutputChangeListener vcl) {
        outputMonitor.removeOutputChangeListener(vcl);
    }

    @Override
    public synchronized void addPlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistMonitor.addPlaylistChangeListener(pcl);
    }

    @Override
    public synchronized void removePlaylistChangeListener(PlaylistBasicChangeListener pcl) {
        playlistMonitor.removePlaylistChangeListener(pcl);
    }

    @Override
    public synchronized void addErrorListener(ErrorListener el) {
        errorMonitor.addErrorListener(el);
    }

    @Override
    public synchronized void removeErrorListener(ErrorListener el) {
        errorMonitor.removeErrorListener(el);
    }

    @Override
    public void start() {
        Executors.newSingleThreadExecutor().execute(this.standAloneMonitorThread);
    }

    @Override
    public void stop() {
        this.standAloneMonitorThread.setStopped(true);
    }

    @Override
    public boolean isDone() {
        return this.standAloneMonitorThread.isDone();
    }

    @Override
    public boolean isLoaded() {
        return this.standAloneMonitorThread.isInitialized();
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
}