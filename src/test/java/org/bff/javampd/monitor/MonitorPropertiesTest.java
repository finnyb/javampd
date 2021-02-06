package org.bff.javampd.monitor;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MonitorPropertiesTest {
    private MonitorProperties monitorProperties;

    @BeforeEach
    void setUp() {
        monitorProperties = new MonitorProperties();
    }

    @Test
    void testGetOutputDelay() {
        assertEquals(60, monitorProperties.getOutputDelay());
    }

    @Test
    void testGetConnectionDelay() {
        assertEquals(5, monitorProperties.getConnectionDelay());
    }

    @Test
    void testGetPlaylistDelay() {
        assertEquals(2, monitorProperties.getPlaylistDelay());
    }

    @Test
    void testGetPlayerDelay() {
        assertEquals(0, monitorProperties.getPlayerDelay());
    }

    @Test
    void testGetErrorDelay() {
        assertEquals(0, monitorProperties.getErrorDelay());
    }

    @Test
    void testGetTrackDelay() {
        assertEquals(0, monitorProperties.getTrackDelay());
    }

    @Test
    void testGetMonitorDelay() {
        assertEquals(1, monitorProperties.getMonitorDelay());
    }

    @Test
    void testGetExceptionDelay() {
        assertEquals(5, monitorProperties.getExceptionDelay());
    }
}
