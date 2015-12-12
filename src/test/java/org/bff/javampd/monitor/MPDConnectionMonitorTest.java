package org.bff.javampd.monitor;

import org.bff.javampd.server.ConnectionChangeEvent;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDConnectionMonitorTest {

    @Mock
    private Server server;

    private ConnectionMonitor connectionMonitor;

    @Before
    public void setUp() throws Exception {
        connectionMonitor = new MPDConnectionMonitor();
        connectionMonitor.setServer(server);
    }

    @Test
    public void testAddConnectionChangeListener() throws Exception {
        final ConnectionChangeEvent[] changeEvent = new ConnectionChangeEvent[1];

        connectionMonitor.addConnectionChangeListener(event -> changeEvent[0] = event);
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertEquals(false, changeEvent[0].isConnected());
    }

    @Test
    public void testRemoveConnectionChangeListener() throws Exception {
        final ConnectionChangeEvent[] changeEvent = new ConnectionChangeEvent[1];

        ConnectionChangeListener connectionChangeListener = event -> changeEvent[0] = event;

        connectionMonitor.addConnectionChangeListener(connectionChangeListener);
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertEquals(false, changeEvent[0].isConnected());

        changeEvent[0] = null;
        connectionMonitor.removeConnectionChangeListener(connectionChangeListener);
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testNoChange() throws Exception {
        final ConnectionChangeEvent[] changeEvent = new ConnectionChangeEvent[1];

        connectionMonitor.addConnectionChangeListener(event -> changeEvent[0] = event);
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testIsConnected() throws Exception {
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertEquals(false, connectionMonitor.isConnected());
    }
}