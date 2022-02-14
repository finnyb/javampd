package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.player.TrackPositionChangeEvent;
import org.bff.javampd.player.TrackPositionChangeListener;
import org.bff.javampd.server.Status;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDTrackMonitor implements TrackMonitor {
    private List<TrackPositionChangeListener> trackListeners;
    private long oldPos;
    private long elapsedTime;

    MPDTrackMonitor() {
        this.trackListeners = new ArrayList<>();
    }

    @Override
    public void checkStatus() {
        checkTrackPosition(elapsedTime);
    }

    @Override
    public void processResponseStatus(String line) {
        if (Status.lookup(line) == Status.TIME) {
            elapsedTime =
                    Long.parseLong(line.substring(Status.TIME.getStatusPrefix().length()).trim().split(":")[0]);
        }
    }

    @Override
    public void reset() {
        oldPos = 0;
        elapsedTime = 0;
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
     * {@link org.bff.javampd.player.TrackPositionChangeEvent}s.
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
        oldPos = 0;
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.player.TrackPositionChangeEvent} to all registered
     * {@link TrackPositionChangeListener}s.
     *
     * @param newTime the new elapsed time
     */
    protected synchronized void fireTrackPositionChangeEvent(long newTime) {
        var tpce = new TrackPositionChangeEvent(this, newTime);

        for (TrackPositionChangeListener tpcl : trackListeners) {
            tpcl.trackPositionChanged(tpce);
        }
    }
}
