package org.bff.javampd.player;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles volume change eventing.
 *
 * @author bill
 */
public class VolumeChangeDelegate {
    private List<VolumeChangeListener> volListeners;

    public VolumeChangeDelegate() {
        volListeners = new ArrayList<>();
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
     * @param source source of event
     * @param volume the new volume
     */
    public void fireVolumeChangeEvent(Object source, int volume) {
        final VolumeChangeListener[] arr;
        synchronized (this) {
            arr = volListeners.toArray(new VolumeChangeListener[0]);
        }

        VolumeChangeEvent vce = new VolumeChangeEvent(source, volume);
        for (VolumeChangeListener vcl : arr) {
            vcl.volumeChanged(vce);
        }
    }
}
