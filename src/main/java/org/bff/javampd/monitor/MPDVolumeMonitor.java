package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.Status;
import org.bff.javampd.events.VolumeChangeDelegate;
import org.bff.javampd.events.VolumeChangeListener;
import org.bff.javampd.exception.MPDException;

@Singleton
public class MPDVolumeMonitor implements VolumeMonitor {
    private int newVolume;
    private int oldVolume;
    private VolumeChangeDelegate volumeChangeDelegate;

    public MPDVolumeMonitor() {
        this.volumeChangeDelegate = new VolumeChangeDelegate();
    }

    @Override
    public void processResponseStatus(String line) {
        if (Status.lookupStatus(line) == Status.VOLUME) {
            newVolume =
                    Integer.parseInt(line.substring(Status.VOLUME.getStatusPrefix().length()).trim());
        }
    }

    @Override
    public void checkStatus() throws MPDException {
        if (oldVolume != newVolume) {
            fireVolumeChangeEvent(newVolume);
            oldVolume = newVolume;
        }
    }

    @Override
    public synchronized void addVolumeChangeListener(VolumeChangeListener vcl) {
        volumeChangeDelegate.addVolumeChangeListener(vcl);
    }

    @Override
    public synchronized void removeVolumeChangedListener(VolumeChangeListener vcl) {
        volumeChangeDelegate.removeVolumeChangedListener(vcl);
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.events.VolumeChangeEvent} to all registered
     * {@link VolumeChangeListener}.
     *
     * @param volume the new volume
     */
    protected synchronized void fireVolumeChangeEvent(int volume) {
        volumeChangeDelegate.fireVolumeChangeEvent(this, volume);
    }
}
