package org.bff.javampd.monitor;

import org.bff.javampd.events.TrackPositionChangeListener;

public interface TrackMonitor extends StatusMonitor {

    void addTrackPositionChangeListener(TrackPositionChangeListener tpcl);

    void removeTrackPositionChangeListener(TrackPositionChangeListener tpcl);

    void resetElapsedTime();
}
