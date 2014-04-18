package org.bff.javampd.monitor;

import org.bff.javampd.events.VolumeChangeListener;

public interface VolumeMonitor extends StatusMonitor {
    void addVolumeChangeListener(VolumeChangeListener vcl);

    void removeVolumeChangedListener(VolumeChangeListener vcl);
}
