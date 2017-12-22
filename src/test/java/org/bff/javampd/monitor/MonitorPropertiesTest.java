package org.bff.javampd.monitor;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MonitorPropertiesTest {
    private MonitorProperties monitorProperties;

    @Before
    public void setUp() {
        monitorProperties = new MonitorProperties();
    }

    @Test
    public void testGetOutputDelay() throws Exception {
        assertEquals(60, monitorProperties.getOutputDelay());
    }

    @Test
    public void testGetConnectionDelay() throws Exception {
        assertEquals(5, monitorProperties.getConnectionDelay());
    }

    @Test
    public void testGetPlaylistDelay() throws Exception {
        assertEquals(2, monitorProperties.getPlaylistDelay());
    }

    @Test
    public void testGetPlayerDelay() throws Exception {
        assertEquals(0, monitorProperties.getPlayerDelay());
    }

    @Test
    public void testGetErrorDelay() throws Exception {
        assertEquals(0, monitorProperties.getErrorDelay());
    }

    @Test
    public void testGetTrackDelay() throws Exception {
        assertEquals(0, monitorProperties.getTrackDelay());
    }

    @Test
    public void testGetMonitorDelay() throws Exception {
        assertEquals(1, monitorProperties.getMonitorDelay());
    }

    @Test
    public void testGetExceptionDelay() throws Exception {
        assertEquals(5, monitorProperties.getExceptionDelay());
    }
}
