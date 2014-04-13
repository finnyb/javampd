/*
 * MPDStandAloneMonitor.java
 *
 * Created on October 18, 2005, 10:17 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd.monitor;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.bff.javampd.MPD;
import org.bff.javampd.ServerStatus;
import org.bff.javampd.StandAloneMonitor;
import org.bff.javampd.events.*;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(MPDStandAloneMonitor.class);

    @Inject
    private ServerStatus serverStatus;
    @Inject
    private OutputMonitor outputMonitor;
    @Inject
    private TrackMonitor trackMonitor;
    @Inject
    private ConnectionMonitor connectionMonitor;
    @Inject
    private VolumeMonitor volumeMonitor;
    @Inject
    private PlayerMonitor playerMonitor;
    @Inject
    private BitrateMonitor bitrateMonitor;
    @Inject
    private PlaylistMonitor playlistMonitor;
    @Inject
    private ErrorMonitor errorMonitor;

    private final int delay;
    private boolean stopped;

    private static final int DEFAULT_DELAY = 1000;

    /**
     * Creates a new instance of MPDStandAloneMonitor using the given getDelay interval
     * for queries.
     */
    MPDStandAloneMonitor() {
        this.delay = DEFAULT_DELAY;
    }

    /**
     * Adds a {@link TrackPositionChangeListener} to this object to receive
     * {@link TrackPositionChangeEvent}s.
     *
     * @param tpcl the TrackPositionChangeListener to add
     */
    public synchronized void addTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackMonitor.addTrackPositionChangeListener(tpcl);
    }

    /**
     * Removes a {@link TrackPositionChangeListener} from this object.
     *
     * @param tpcl the TrackPositionChangeListener to remove
     */
    public synchronized void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackMonitor.removeTrackPositionChangeListener(tpcl);
    }

    /**
     * Adds a {@link org.bff.javampd.events.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.events.ConnectionChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListener to add
     */
    public synchronized void addConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionMonitor.addConnectionChangeListener(ccl);
    }

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
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

    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputMonitor.addOutputChangeListener(vcl);
    }

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
        loadInitialStatus();

        List<String> response;
        while (!isStopped()) {

            try {
                synchronized (this) {
                    response = new ArrayList<>(serverStatus.getStatus());
                    processResponse(response);
                    checkPlayer();
                    checkPlaylist();
                    checkTrackPosition();
                    checkVolume();
                    checkBitrate();
                    checkConnection();
                    checkOutputs();
                    errorMonitor.checkStatus();
                    this.wait(delay);
                }
            } catch (InterruptedException ie) {
                LOGGER.error("Thread interrupted", ie);
                setStopped(true);
            } catch (MPDException mpdException) {
                LOGGER.error("Error while checking statuses", mpdException);
                boolean retry = true;

                while (retry) {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        LOGGER.error("StandAloneMonitor interrupted", ex);
                    }

                    try {
                        connectionMonitor.checkStatus();
                    } catch (MPDException e) {
                        LOGGER.error("Error checking connection status.", e);
                    }
                    if (connectionMonitor.isConnected()) {
                        retry = false;
                    }
                }
            }
        }
    }

    private void loadInitialStatus() {
        try {
            //initial load so no events fired
            List<String> response = new ArrayList<String>(serverStatus.getStatus());
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

    private int checkConnectionCount;

    /**
     * Checks the connection status of the {@link MPD}.  Fires a {@link ConnectionChangeEvent}
     * if the connection status changes.
     */
    protected final void checkConnection() throws MPDException {
        if (checkConnectionCount == connectionMonitor.getDelay()) {
            checkConnectionCount = 0;
            connectionMonitor.checkStatus();
        } else {
            ++checkConnectionCount;
        }
    }

    private int checkPositionCount;

    private void checkTrackPosition() throws MPDException {
        if (checkPositionCount == trackMonitor.getDelay()) {
            checkPositionCount = 0;
            trackMonitor.checkStatus();
        } else {
            ++checkPositionCount;
        }
    }

    private int checkPlayerCount;

    private void checkPlayer() throws MPDException {
        if (checkPlayerCount == playerMonitor.getDelay()) {
            checkPlayerCount = 0;
            playerMonitor.checkStatus();
        } else {
            ++checkPlayerCount;
        }
    }

    private int checkBitrateCount;

    private void checkBitrate() throws MPDException {
        if (checkBitrateCount == 7) {
            checkBitrateCount = 0;
            bitrateMonitor.checkStatus();
        } else {
            ++checkBitrateCount;
        }
    }

    private int checkOutputCount;

    /**
     * Checks the connection status of the MPD.  Fires a {@link ConnectionChangeEvent}
     * if the connection status changes.
     *
     * @throws MPDConnectionException if there is a problem with the connection
     * @throws MPDResponseException   if response is an error
     */
    protected final void checkOutputs() throws MPDException {
        if (checkOutputCount == outputMonitor.getDelay()) {
            checkOutputCount = 0;
            outputMonitor.checkStatus();
        } else {
            ++checkOutputCount;
        }
    }

    private int checkPlaylistCount;

    private void checkPlaylist() throws MPDException {
        if (checkPlaylistCount == playlistMonitor.getDelay()) {
            checkPlaylistCount = 0;
            playlistMonitor.checkStatus();
        } else {
            ++checkPlaylistCount;
        }
    }

    private int checkVolumeCount;

    private void checkVolume() throws MPDException {
        if (checkVolumeCount == 5) {
            checkVolumeCount = 0;
            volumeMonitor.checkStatus();
        } else {
            ++checkVolumeCount;
        }
    }


    private void processResponse(List<String> response) {
        for (String line : response) {
            processResponseStatus(line);
        }
    }

    private void processResponseStatus(String line) {
        trackMonitor.processResponseStatus(line);
        trackMonitor.processResponseStatus(line);
        volumeMonitor.processResponseStatus(line);
        playerMonitor.processResponseStatus(line);
        bitrateMonitor.processResponseStatus(line);
        playlistMonitor.processResponseStatus(line);
        errorMonitor.processResponseStatus(line);
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
