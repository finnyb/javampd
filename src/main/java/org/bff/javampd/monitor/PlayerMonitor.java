package org.bff.javampd.monitor;

import org.bff.javampd.player.BitrateChangeListener;
import org.bff.javampd.player.PlayerBasicChangeListener;
import org.bff.javampd.player.VolumeChangeListener;

public interface PlayerMonitor extends StatusMonitor {
  void addPlayerChangeListener(PlayerBasicChangeListener pcl);

  void removePlayerChangeListener(PlayerBasicChangeListener pcl);

  void addBitrateChangeListener(BitrateChangeListener bcl);

  void removeBitrateChangeListener(BitrateChangeListener bcl);

  void addVolumeChangeListener(VolumeChangeListener vcl);

  void removeVolumeChangeListener(VolumeChangeListener vcl);

  PlayerStatus getStatus();
}
