package org.bff.javampd.monitor;

import org.bff.javampd.Server;
import org.bff.javampd.events.ConnectionChangeEvent;
import org.bff.javampd.events.ConnectionChangeListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDConnectionMonitorTest {
    @Mock
    private Server server;

    @InjectMocks
    private MPDConnectionMonitor connectionMonitor;
    private boolean success;

    @Before
    public void setUp() {
        success = false;
    }

    @Test
    public void testCheckStatusLostConnection() throws Exception {
        connectionMonitor.addConnectionChangeListener(new ConnectionChangeListener() {

            @Override
            public void connectionChangeEventReceived(ConnectionChangeEvent event) {
                success = true;
            }
        });
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusReConnected() throws Exception {
        when(server.isConnected()).thenReturn(false);
        connectionMonitor.checkStatus();
        connectionMonitor.addConnectionChangeListener(new ConnectionChangeListener() {

            @Override
            public void connectionChangeEventReceived(ConnectionChangeEvent event) {
                success = true;
            }
        });

        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        connectionMonitor.addConnectionChangeListener(new ConnectionChangeListener() {

            @Override
            public void connectionChangeEventReceived(ConnectionChangeEvent event) {
                success = true;
            }
        });
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        ConnectionChangeListener connectionChangeListener = new ConnectionChangeListener() {

            @Override
            public void connectionChangeEventReceived(ConnectionChangeEvent event) {
                success = true;
            }
        };

        connectionMonitor.addConnectionChangeListener(connectionChangeListener);
        connectionMonitor.checkStatus();

        assertTrue(success);

        success = false;

        connectionMonitor.removeConnectionChangeListener(connectionChangeListener);
        when(server.isConnected()).thenReturn(true);
        connectionMonitor.checkStatus();
        assertFalse(success);
    }
}
