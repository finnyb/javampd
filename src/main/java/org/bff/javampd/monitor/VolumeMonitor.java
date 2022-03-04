package org.bff.javampd.monitor;

import org.bff.javampd.player.VolumeChangeListener;

public interface VolumeMonitor extends StatusMonitor {
  void addVolumeChangeListener(VolumeChangeListener vcl);

  void removeVolumeChangeListener(VolumeChangeListener vcl);
}
