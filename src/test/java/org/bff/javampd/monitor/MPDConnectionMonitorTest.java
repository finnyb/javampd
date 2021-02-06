package org.bff.javampd.monitor;

import org.bff.javampd.server.ConnectionChangeEvent;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.Server;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDConnectionMonitorTest {

    @Mock
    private Server server;

    private ConnectionMonitor connectionMonitor;

    @BeforeEach
    void setUp() {
        connectionMonitor = new MPDConnectionMonitor();
        connectionMonitor.setServer(server);
    }

    @Test
    void testAddConnectionChangeListener() {
        final ConnectionChangeEvent[] changeEvent = new ConnectionChangeEvent[1];

        connectionMonitor.addConnectionChangeListener(event -> changeEvent[0] = event);
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertEquals(false, changeEvent[0].isConnected());
    }

    @Test
    void testRemoveConnectionChangeListener() {
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
    void testNoChange() {
        final ConnectionChangeEvent[] changeEvent = new ConnectionChangeEvent[1];

        connectionMonitor.addConnectionChangeListener(event -> changeEvent[0] = event);
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    void testIsConnected() {
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertEquals(false, connectionMonitor.isConnected());
    }
}
