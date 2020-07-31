package org.bff.javampd.monitor;

import org.bff.javampd.player.BitrateChangeListener;

public interface BitrateMonitor extends StatusMonitor {
  void addBitrateChangeListener(BitrateChangeListener bcl);

  void removeBitrateChangeListener(BitrateChangeListener bcl);
}
