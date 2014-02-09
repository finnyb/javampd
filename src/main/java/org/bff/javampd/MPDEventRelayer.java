/*
 * MPDEventMonitor.java
 *
 * Created on October 11, 2005, 8:48 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import com.google.inject.Inject;
import org.bff.javampd.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        implements EventRelayer {

    private final Logger logger = LoggerFactory.getLogger(MPDEventRelayer.class);

    private final int delay;
    private boolean stopped;
    private static final int DEFAULT_DELAY = 1000;
    private List<PlayerChangeListener> playerListeners;
    private List<PlaylistChangeListener> playlistListeners;
    private List<MPDChangeListener> mpdListeners;
    private List<VolumeChangeListener> volListeners;
    private List<OutputChangeListener> outputListeners;

    private Player player;
    private Playlist playlist;
    private Admin admin;

    /**
     * Creates a new instance of MPDEventMonitor using the default delay of 1 second
     * for {@link TrackPositionChangeEvent}s
     */
    @Inject
    MPDEventRelayer(Player player, Playlist playlist, Admin admin) {
        this(player, playlist, admin, DEFAULT_DELAY);
    }

    /**
     * Creates a new instance of MPDEventMonitor using the given delay
     * for {@link TrackPositionChangeEvent}s
     *
     * @param delay the amount of delay in seconds
     */
    MPDEventRelayer(Player player, Playlist playlist, Admin admin, int delay) {
        this.player = player;
        this.playlist = playlist;
        this.admin = admin;
        this.delay = delay;
        this.playerListeners = new ArrayList<PlayerChangeListener>();
        this.playlistListeners = new ArrayList<PlaylistChangeListener>();
        this.mpdListeners = new ArrayList<MPDChangeListener>();
        this.volListeners = new ArrayList<VolumeChangeListener>();
        this.outputListeners = new ArrayList<OutputChangeListener>();
        addListeners();
    }

    private void addListeners() {
        this.player.addPlayerChangeListener(this);
        this.playlist.addPlaylistChangeListener(this);
        this.admin.addOutputChangeListener(this);
    }

    @Override
    public void playerChanged(PlayerChangeEvent event) {
        firePlayerChangeEvent(event.getEvent());
    }

    @Override
    public void mpdChanged(MPDChangeEvent event) {
        fireMPDChangeEvent(event.getEvent());
    }

    @Override
    public void playlistChanged(PlaylistChangeEvent event) {
        firePlaylistChangeEvent(event.getEvent());
    }

    @Override
    public synchronized void addPlayerChangeListener(PlayerChangeListener pcl) {
        playerListeners.add(pcl);
    }

    @Override
    public synchronized void removePlayerChangedListener(PlayerChangeListener pcl) {
        playerListeners.remove(pcl);
    }

    @Override
    public synchronized void addMPDChangeListener(MPDChangeListener mcl) {
        mpdListeners.add(mcl);
    }

    @Override
    public synchronized void removeMPDChangedListener(MPDChangeListener mcl) {
        mpdListeners.remove(mcl);
    }

    @Override
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    @Override
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link MPDChangeEvent} to all registered
     * {@link MPDChangeListener}s.
     *
     * @param event the {@link MPDChangeEvent.Event} to send
     */
    protected synchronized void fireMPDChangeEvent(MPDChangeEvent.Event event) {
        MPDChangeEvent mce = new MPDChangeEvent(this, event);

        for (MPDChangeListener mcl : mpdListeners) {
            mcl.mpdChanged(mce);
        }
    }

    @Override
    public synchronized void addOutputChangeListener(OutputChangeListener vcl) {
        outputListeners.add(vcl);
    }

    @Override
    public synchronized void removeOutputChangedListener(OutputChangeListener vcl) {
        outputListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link OutputChangeEvent} to all registered
     * {@link OutputChangeListener}s.
     *
     * @param event the {@link OutputChangeEvent} to send
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
     * @param event the {@link PlayerChangeEvent.Event} to send
     */
    protected synchronized void firePlayerChangeEvent(PlayerChangeEvent.Event event) {
        PlayerChangeEvent pce = new PlayerChangeEvent(this, event);

        for (PlayerChangeListener pcl : playerListeners) {
            pcl.playerChanged(pce);
        }
    }

    /**
     * Sends the appropriate {@link PlaylistChangeEvent} to all registered
     * {@link PlaylistChangeListener}.
     *
     * @param event the {@link PlaylistChangeEvent.Event} to send
     */
    protected synchronized void firePlaylistChangeEvent(PlaylistChangeEvent.Event event) {
        PlaylistChangeEvent pce = new PlaylistChangeEvent(this, event);

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

    @Override
    public void run() {
        while (!isStopped()) {
            try {
                checkTrackPosition(player.getElapsedTime());
                checkConnection();
                this.wait(delay);
            } catch (Exception e) {
                logger.error("Problem in relayer thread", e);
            }
        }
    }

    @Override
    public void start() {
        Thread th = new Thread(this);
        th.start();
    }

    @Override
    public void stop() {
        this.stopped = true;
    }

    @Override
    public boolean isStopped() {
        return stopped;
    }

    @Override
    public void outputChanged(OutputChangeEvent event) {
        fireOutputChangeEvent(event);
    }
}
