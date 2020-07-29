package org.bff.javampd.monitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MonitorPropertiesTest {
    private MonitorProperties monitorProperties;

    @BeforeEach
    public void setUp() {
        monitorProperties = new MonitorProperties();
    }

    @Test
    public void testGetOutputDelay() {
        assertEquals(60, monitorProperties.getOutputDelay());
    }

    @Test
    public void testGetConnectionDelay() {
        assertEquals(5, monitorProperties.getConnectionDelay());
    }

    @Test
    public void testGetPlaylistDelay() {
        assertEquals(2, monitorProperties.getPlaylistDelay());
    }

    @Test
    public void testGetPlayerDelay() {
        assertEquals(0, monitorProperties.getPlayerDelay());
    }

    @Test
    public void testGetErrorDelay() {
        assertEquals(0, monitorProperties.getErrorDelay());
    }

    @Test
    public void testGetTrackDelay() {
        assertEquals(0, monitorProperties.getTrackDelay());
    }

    @Test
    public void testGetMonitorDelay() {
        assertEquals(1, monitorProperties.getMonitorDelay());
    }

    @Test
    public void testGetExceptionDelay() {
        assertEquals(5, monitorProperties.getExceptionDelay());
    }
}
