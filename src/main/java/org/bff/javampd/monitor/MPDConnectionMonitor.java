package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.server.ConnectionChangeEvent;
import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.Server;

import java.util.ArrayList;
import java.util.List;

@Singleton
public class MPDConnectionMonitor implements ConnectionMonitor {
    private List<ConnectionChangeListener> connectionListeners;
    private Server server;
    private boolean connected = true;

    public MPDConnectionMonitor() {
        this.connectionListeners = new ArrayList<>();
    }

    /**
     * Adds a {@link org.bff.javampd.server.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.server.ConnectionChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListener to add
     */
    @Override
    public synchronized void addConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.add(ccl);
    }

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
    @Override
    public synchronized void removeConnectionChangeListener(ConnectionChangeListener ccl) {
        connectionListeners.remove(ccl);
    }

    /**
     * Sends the appropriate {@link org.bff.javampd.server.ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     *
     * @param isConnected the connection status
     */
    protected synchronized void fireConnectionChangeEvent(boolean isConnected) {
        ConnectionChangeEvent cce = new ConnectionChangeEvent(this, isConnected);

        for (ConnectionChangeListener ccl : connectionListeners) {
            ccl.connectionChangeEventReceived(cce);
        }
    }

    @Override
    public void checkStatus() {
        boolean conn = this.server.isConnected();
        if (connected != conn) {
            connected = conn;
            fireConnectionChangeEvent(conn);
        }
    }

    @Override
    public void setServer(Server server) {
        this.server = server;
    }

    @Override
    public boolean isConnected() {
        return this.connected;
    }
}
