/*
 * MPDEventMonitor.java
 *
 * Created on October 13, 2005, 9:04 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd.monitor;

import org.bff.javampd.MPD;
import org.bff.javampd.events.ConnectionChangeEvent;
import org.bff.javampd.events.ConnectionChangeListener;
import org.bff.javampd.events.TrackPositionChangeEvent;
import org.bff.javampd.events.TrackPositionChangeListener;

import java.util.ArrayList;
import java.util.List;

/**
 * MPDEventMonitor is the abstract base class for all event monitors.
 *
 * @author Bill
 */
public abstract class MPDEventMonitor {
    private MPD mpd;
    private long oldPos;
    private boolean connectedState = true;
    private List<TrackPositionChangeListener> trackListeners;
    private List<ConnectionChangeListener> connectionListeners;

    /**
     * Only contructor for this abstract base class.
     *
     * @param mpd a connection to a MPD
     */
    public MPDEventMonitor(MPD mpd) {
        this.mpd = mpd;
        trackListeners = new ArrayList<TrackPositionChangeListener>();
        connectionListeners = new ArrayList<ConnectionChangeListener>();
    }

    /**
     * Adds a {@link TrackPositionChangeListener} to this object to receive
     * {@link TrackPositionChangeEvent}s.
     *
     * @param tpcl the TrackPositionChangeListener to add
     */
    public synchronized void addTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.add(tpcl);
    }

    /**
     * Removes a {@link TrackPositionChangeListener} from this object.
     *
     * @param tpcl the TrackPositionChangeListener to remove
     */
    public synchronized void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.remove(tpcl);
    }

    /**
     * Sends the appropriate {@link TrackPositionChangeEvent} to all registered
     * {@link TrackPositionChangeListener}s.
     *
     * @param newTime the new elapsed time
     */
    protected synchronized void fireTrackPositionChangeEvent(long newTime) {
        TrackPositionChangeEvent tpce = new TrackPositionChangeEvent(this, newTime);

        for (TrackPositionChangeListener tpcl : trackListeners) {
            tpcl.trackPositionChanged(tpce);
        }
    }

    /**
     * Adds a {@link ConnectionChangeListener} to this object to receive
     * {@link ConnectionChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListerner to add
     */
    public synchronized void addConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.add(ccl);
    }

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
    public synchronized void removeConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.remove(ccl);
    }

    /**
     * Sends the appropriate {@link ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     *
     * @param isConnected the connection status
     * @param msg         the message to pass to the exception
     */
    protected synchronized void fireConnectionChangeEvent(boolean isConnected, String msg) {
        ConnectionChangeEvent cce = new ConnectionChangeEvent(this, isConnected, msg);

        for (ConnectionChangeListener ccl : connectionListeners) {
            ccl.connectionChangeEventReceived(cce);
        }
    }

    private int checkConnCount;

    /**
     * Checks the connection status of the MPD.  Fires a {@link ConnectionChangeEvent}
     * if the connection status changes.
     */
    protected final void checkConnection() {
        if (checkConnCount == 3) {
            checkConnCount = 0;

            boolean conn = mpd.isConnected();
            if (connectedState != conn) {
                connectedState = conn;
                fireConnectionChangeEvent(conn, "Connection Changed");
            }
        } else {
            ++checkConnCount;
        }
    }

    /**
     * Checks the track position and fires a {@link TrackPositionChangeEvent} if
     * there has been a change in track position.
     *
     * @param newPos the new elapsed time to check
     */
    protected final void checkTrackPosition(long newPos) {
        if (oldPos != newPos) {
            oldPos = newPos;
            fireTrackPositionChangeEvent(newPos);
        }
    }

    /**
     * Returns the connected state of the connection to the MPD server.
     *
     * @return the connected state
     */
    protected boolean isConnectedState() {
        return this.connectedState;
    }
}
