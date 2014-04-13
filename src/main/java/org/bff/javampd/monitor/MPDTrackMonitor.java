package org.bff.javampd.monitor;

import org.bff.javampd.Status;
import org.bff.javampd.events.TrackPositionChangeEvent;
import org.bff.javampd.events.TrackPositionChangeListener;
import org.bff.javampd.exception.MPDException;

import java.util.ArrayList;
import java.util.List;

public class MPDTrackMonitor implements TrackMonitor {
    private List<TrackPositionChangeListener> trackListeners;
    private long oldPos;
    private long elapsedTime;

    public MPDTrackMonitor() {
        this.trackListeners = new ArrayList<>();
    }

    @Override
    public int getDelay() {
        return 0;
    }

    @Override
    public void checkStatus() throws MPDException {
        checkTrackPosition(elapsedTime);
    }

    @Override
    public void processResponseStatus(String line) {
        if (Status.lookupStatus(line) == Status.TIME) {
            elapsedTime =
                    Long.parseLong(line.substring(Status.TIME.getStatusPrefix().length()).trim());
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
     * Adds a {@link TrackPositionChangeListener} to this object to receive
     * {@link org.bff.javampd.events.TrackPositionChangeEvent}s.
     *
     * @param tpcl the TrackPositionChangeListener to add
     */
    @Override
    public synchronized void addTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.add(tpcl);
    }

    /**
     * Removes a {@link TrackPositionChangeListener} from this object.
     *
     * @param tpcl the TrackPositionChangeListener to remove
     */
    @Override
    public synchronized void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl) {
        trackListeners.remove(tpcl);
    }

    @Override
    public void resetElapsedTime() {
        elapsedTime = 0;
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.events.TrackPositionChangeEvent} to all registered
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
}
