package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.player.BitrateChangeEvent;
import org.bff.javampd.player.BitrateChangeListener;
import org.bff.javampd.server.Status;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDBitrateMonitor extends MPDVolumeMonitor implements BitrateMonitor {
    private int oldBitrate;
    private int newBitrate;

    private List<BitrateChangeListener> bitrateListeners;

    MPDBitrateMonitor() {
        bitrateListeners = new ArrayList<>();
    }

    @Override
    public synchronized void addBitrateChangeListener(BitrateChangeListener bcl) {
        bitrateListeners.add(bcl);
    }

    @Override
    public synchronized void removeBitrateChangeListener(BitrateChangeListener bcl) {
        bitrateListeners.remove(bcl);
    }

    @Override
    public void processResponseStatus(String line) {
        super.processResponseStatus(line);
        if (Status.lookupStatus(line) == Status.BITRATE) {
            newBitrate =
                    Integer.parseInt(line.substring(Status.BITRATE.getStatusPrefix().length()).trim());
        }
    }

    @Override
    public void checkStatus() {
        super.checkStatus();
        if (oldBitrate != newBitrate) {
            fireBitrateChangeEvent(new BitrateChangeEvent(this, oldBitrate, newBitrate));
            oldBitrate = newBitrate;
        }
    }

    private void fireBitrateChangeEvent(BitrateChangeEvent bitrateChangeEvent) {
        for (BitrateChangeListener bcl : bitrateListeners) {
            bcl.bitrateChanged(bitrateChangeEvent);
        }
    }

    @Override
    public void reset() {
        oldBitrate = 0;
        newBitrate = 0;
    }
}
