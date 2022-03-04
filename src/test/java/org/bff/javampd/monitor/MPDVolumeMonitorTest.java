package org.bff.javampd.monitor;

import static org.junit.jupiter.api.Assertions.*;

import org.bff.javampd.player.VolumeChangeListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MPDVolumeMonitorTest {

  private VolumeMonitor volumeMonitor;

  @BeforeEach
  void setUp() {
    volumeMonitor = new MPDVolumeMonitor();
  }

  @Test
  void testProcessResponseStatus() {
    final int[] volume = {0};
    volumeMonitor.addVolumeChangeListener(event -> volume[0] = event.getVolume());
    volumeMonitor.processResponseStatus("volume: 1");
    volumeMonitor.checkStatus();
    assertEquals(1, volume[0]);
  }

  @Test
  void testProcessResponseStatusSameVolume() {
    final boolean[] eventFired = {false};

    volumeMonitor.addVolumeChangeListener(event -> eventFired[0] = true);
    volumeMonitor.processResponseStatus("volume: 1");
    volumeMonitor.checkStatus();
    assertTrue(eventFired[0]);

    eventFired[0] = false;
    volumeMonitor.processResponseStatus("volume: 1");
    volumeMonitor.checkStatus();
    assertFalse(eventFired[0]);
  }

  @Test
  void testProcessResponseStatusNotVolume() {
    final boolean[] eventFired = {false};

    volumeMonitor.addVolumeChangeListener(event -> eventFired[0] = true);

    volumeMonitor.processResponseStatus("bogus: 1");
    volumeMonitor.checkStatus();
    assertFalse(eventFired[0]);
  }

  @Test
  void testRemoveVolumeChangeListener() {
    final int[] volume = {0};

    VolumeChangeListener volumeChangeListener = event -> volume[0] = event.getVolume();

    volumeMonitor.addVolumeChangeListener(volumeChangeListener);
    volumeMonitor.processResponseStatus("volume: 1");
    volumeMonitor.checkStatus();
    assertEquals(1, volume[0]);

    volumeMonitor.removeVolumeChangeListener(volumeChangeListener);

    volumeMonitor.processResponseStatus("volume: 2");
    assertEquals(1, volume[0]);
  }

  @Test
  void testResetVolume() {
    String line = "volume: 1";

    final boolean[] eventFired = {false};

    volumeMonitor.addVolumeChangeListener(event -> eventFired[0] = true);
    volumeMonitor.processResponseStatus(line);
    volumeMonitor.checkStatus();
    assertTrue(eventFired[0]);

    volumeMonitor.reset();
    eventFired[0] = false;

    volumeMonitor.processResponseStatus(line);
    volumeMonitor.checkStatus();
    assertTrue(eventFired[0]);
  }
}
