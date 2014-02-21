package org.bff.javampd.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles volume change eventing.
 *
 * @author bill
 * @since: 2/19/14 9:01 PM
 */
public class VolumeChangeDelegate {
    private List<VolumeChangeListener> volListeners;

    public VolumeChangeDelegate() {
        volListeners = new ArrayList<VolumeChangeListener>();
    }

    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volListeners.add(vcl);
    }

    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volListeners.remove(vcl);
    }

    /**
     * Sends the appropriate {@link VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     *
     * @param volume the new volume
     */
    public synchronized void fireVolumeChangeEvent(Object source, int volume) {
        VolumeChangeEvent vce = new VolumeChangeEvent(source, volume);

        for (VolumeChangeListener vcl : volListeners) {
            vcl.volumeChanged(vce);
        }
    }
}
