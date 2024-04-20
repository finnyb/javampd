package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.player.BitrateChangeEvent;
import org.bff.javampd.player.BitrateChangeListener;
import org.bff.javampd.server.Status;

@Singleton
public class MPDBitrateMonitor extends MPDVolumeMonitor implements BitrateMonitor {
  private int oldBitrate;
  private int newBitrate;

  private final List<BitrateChangeListener> bitrateListeners;

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
    if (Status.lookup(line) == Status.BITRATE) {
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
