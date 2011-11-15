/*
 * MPDEventMonitor.java
 *
 * Created on October 11, 2005, 8:48 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd.monitor;

import org.bff.javampd.MPD;
import org.bff.javampd.MPDAdmin;
import org.bff.javampd.MPDPlayer;
import org.bff.javampd.MPDPlaylist;
import org.bff.javampd.events.*;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDEventRelayer is a convenience class for relaying events from
 * {@link MPDPlaylist}, {@link MPDPlayer}, and {@link MPDAdmin}.  There is
 * overhead involved when using this class as an extra set of listeners is
 * maintained.  This class simply registers as a listener to the above classes
 * and relays those events to listeners of this class.  Track position and
 * connection events are also fired at given delays.  If events are desired
 * from directly querying the connection use {@link MPDStandAloneMonitor}.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDEventRelayer
        extends MPDEventMonitor
        implements Runnable,
        PlayerChangeListener,
        PlaylistChangeListener,
        MPDChangeListener,
        OutputChangeListener {

    private final int delay;
    private final MPDPlayer mpdPlayer;
    private final MPDPlaylist mpdPlaylist;
    private final MPDAdmin mpdAdmin;
    private boolean stopped;
    private static final int DEFAULT_DELAY = 1000;
    private List<PlayerChangeListener> playerListeners;
    private List<PlaylistChangeListener> playlistListeners;
    private List<MPDChangeListener> mpdListeners;
    private List<VolumeChangeListener> volListeners;
    private List<OutputChangeListener> outputListeners;

    /**
     * Creates a new instance of MPDEventMonitor using the default delay of 1 second
     * for {@link TrackPositionChangeEvent}s
     *
     * @param mpd the MPD Connection
     */
    public MPDEventRelayer(MPD mpd) {
        this(mpd, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of MPDEventMonitor using the given delay
     * for {@link TrackPositionChangeEvent}s
     *
     * @param mpd   the MPD Connection
     * @param delay the amount of delay in seconds
     */
    public MPDEventRelayer(MPD mpd, int delay) {
        super(mpd);
        this.delay = delay;
        this.mpdPlayer = mpd.getMPDPlayer();
        this.mpdPlaylist = mpd.getMPDPlaylist();
        this.mpdAdmin = mpd.getMPDAdmin();

        this.playerListeners = new ArrayList<PlayerChangeListener>();
        this.playlistListeners = new ArrayList<PlaylistChangeListener>();
        this.mpdListeners = new ArrayList<MPDChangeListener>();
        this.volListeners = new ArrayList<VolumeChangeListener>();
        this.outputListeners = new ArrayList<OutputChangeListener>();
        addListeners();
    }

    private void addListeners() {
        this.mpdPlayer.addPlayerChangeListener(this);
        this.mpdPlaylist.addPlaylistChangeListener(this);
        this.mpdAdmin.addOutputChangeListener(this);
    }

    /**
     * Invoked when a player change event occurs.
     *
     * @param event the event fired
     */
    @Override
    public void playerChanged(PlayerChangeEvent event) {
        firePlayerChangeEvent(event.getId());
    }

    /**
     * Invoked when a mpd administrative change action occurs.
     *
     * @param event the event received
     */
    @Override
    public void mpdChanged(MPDChangeEvent event) {
        fireMPDChangeEvent(event.getId());
    }

    /**
     * Invoked when a mpd playlist change event occurs.
     *
     * @param event the event received
     */
    @Override
    public void playlistChanged(PlaylistChangeEvent event) {
        firePlaylistChangeEvent(event.getId());
    }

    /**
     * Adds a {@link PlayerChangeListener} to this object to receive
     * {@link PlayerChangeEvent}s.
     *
     * @param pcl the PlayerChangeListener to add
     */
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.add(pcl);
    }

    /**
     * Removes a {@link PlayerChangeListener} from this object.
     *
     * @param pcl the PlayerChangeListener to remove
     */
    public synchronized void removePlayerChangedListener(PlayerChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    /**
     * Adds a {@link MPDChangeListener} to this object to receive
     * {@link MPDChangeEvent}s.
     *
     * @param mcl the MPDChangeListener to add
     */
    public synchronized void addMPDChangeListener(MPDChangeListener mcl) {
        mpdListeners.add(mcl);
    }

    /**
     * Removes a {@link MPDChangeListener} from this object.
     *
     * @param mcl the MPDChangeListener to remove
     */
    public synchronized void removeMPDChangedListener(MPDChangeListener mcl) {
        mpdListeners.remove(mcl);
    }

    /**
     * Adds a {@link VolumeChangeListener} to this object to receive
     * {@link VolumeChangeEvent}s.
     *
     * @param vcl the VolumeChangeListener to add
     */
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    /**
     * Removes a {@link VolumeChangeListener} from this object.
     *
     * @param vcl the VolumeChangeListener to remove
     */
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link MPDChangeEvent} to all registered
     * {@link MPDChangeListener}s.
     *
     * @param id the event id to send
     */
    protected synchronized void fireMPDChangeEvent(int id) {
        MPDChangeEvent mce = new MPDChangeEvent(this, id);

        for (MPDChangeListener mcl : mpdListeners) {
            mcl.mpdChanged(mce);
        }
    }

    /**
     * Adds a {@link OutputChangeListener} to this object to receive
     * {@link OutputChangeEvent}s.
     *
     * @param vcl the OutputChangeListener to add
     */
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputListeners.add(vcl);
    }

    /**
     * Removes a {@link OutputChangeListener} from this object.
     *
     * @param vcl the OutputChangeListener to remove
     */
    public synchronized void removeOutputChangedListener(OutputChangeListener vcl) {
        outputListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link OutputChangeEvent} to all registered
     * {@link OutputChangeListener}s.
     *
     * @param event the event id to send
     */
    protected synchronized void fireOutputChangeEvent(OutputChangeEvent event) {
        for (OutputChangeListener ocl : outputListeners) {
            ocl.outputChanged(event);
        }
    }

    /**
     * Sends the appropriate {@link PlayerChangeEvent} to all registered
     * {@link PlayerChangeListener}s.
     *
     * @param id the event id to send
     */
    protected synchronized void firePlayerChangeEvent(int id) {
        PlayerChangeEvent pce = new PlayerChangeEvent(this, id);

        for (PlayerChangeListener pcl : playerListeners) {
            pcl.playerChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param id the event id to send
     */
    protected synchronized void firePlaylistChangeEvent(int id) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, id);

        for (PlaylistChangeListener pcl : playlistListeners) {
            pcl.playlistChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     *
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        VolumeChangeEvent vce = new VolumeChangeEvent(this, volume);

        for (VolumeChangeListener vcl : volListeners) {
            vcl.volumeChanged(vce);
        }
    }

    /**
     * Implements the Runnable run method to monitor the MPD connection.
     */
    @Override
    public void run() {
        while (!isStopped()) {
            try {
                checkTrackPosition(mpdPlayer.getElapsedTime());
                checkConnection();
                try {
                    this.wait(delay);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Starts the monitor by creating and starting a thread using this instance
     * as the Runnable interface.
     */
    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    /**
     * Stops the thread.
     */
    public void stop() {
        this.stopped = true;
    }

    /**
     * Returns true if the monitor is stopped, false if the monitor is still running.
     *
     * @return true if monitor is running, false otherwise false
     */
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void outputChanged(OutputChangeEvent event) {
        fireOutputChangeEvent(event);
    }
}
