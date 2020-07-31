package org.bff.javampd.monitor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.bff.javampd.player.BitrateChangeEvent;
import org.bff.javampd.player.BitrateChangeListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class MPDBitrateMonitorTest {
  private BitrateMonitor bitrateMonitor;

  @BeforeEach
  public void setup() {
    bitrateMonitor = new MPDBitrateMonitor();
  }

  @Test
  public void testAddBitrateChangeListener() {
    final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

    bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
    bitrateMonitor.processResponseStatus("bitrate: 1");
    bitrateMonitor.checkStatus();
    assertEquals(0, changeEvent[0].getOldBitrate());
    assertEquals(1, changeEvent[0].getNewBitrate());
  }

  @Test
  public void testRemoveBitrateChangeListener() {
    final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

    BitrateChangeListener bitrateChangeListener = event ->
      changeEvent[0] = event;

    bitrateMonitor.addBitrateChangeListener(bitrateChangeListener);
    bitrateMonitor.processResponseStatus("bitrate: 1");
    bitrateMonitor.checkStatus();
    assertEquals(0, changeEvent[0].getOldBitrate());
    assertEquals(1, changeEvent[0].getNewBitrate());

    changeEvent[0] = null;
    bitrateMonitor.removeBitrateChangeListener(bitrateChangeListener);
    bitrateMonitor.processResponseStatus("bitrate: 2");
    bitrateMonitor.checkStatus();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testBitrateNoChange() {
    final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

    bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
    bitrateMonitor.processResponseStatus("bitrate: 1");
    bitrateMonitor.checkStatus();
    assertEquals(0, changeEvent[0].getOldBitrate());
    assertEquals(1, changeEvent[0].getNewBitrate());

    changeEvent[0] = null;
    bitrateMonitor.processResponseStatus("bitrate: 1");
    bitrateMonitor.checkStatus();
    assertNull(changeEvent[0]);
  }

  @Test
  public void testResetBitrateChangeListener() {
    String line = "bitrate: 1";

    final BitrateChangeEvent[] changeEvent = new BitrateChangeEvent[1];

    bitrateMonitor.addBitrateChangeListener(event -> changeEvent[0] = event);
    bitrateMonitor.processResponseStatus(line);
    bitrateMonitor.checkStatus();
    assertEquals(0, changeEvent[0].getOldBitrate());
    assertEquals(1, changeEvent[0].getNewBitrate());

    bitrateMonitor.reset();
    changeEvent[0] = null;

    bitrateMonitor.processResponseStatus(line);
    bitrateMonitor.checkStatus();
    assertEquals(0, changeEvent[0].getOldBitrate());
    assertEquals(1, changeEvent[0].getNewBitrate());
  }
}
