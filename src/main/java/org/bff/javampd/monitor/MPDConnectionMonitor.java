package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.Server;
import org.bff.javampd.events.ConnectionChangeEvent;
import org.bff.javampd.events.ConnectionChangeListener;
import org.bff.javampd.exception.MPDException;

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
     * Adds a {@link org.bff.javampd.events.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.events.ConnectionChangeEvent}s.
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
     * Sends the appropriate {@link org.bff.javampd.events.ConnectionChangeEvent} to all registered
     * {@link ConnectionChangeListener}s.
     *
     * @param isConnected the connection status
     * @param msg         the message to pass to the exception
     */
    protected synchronized void fireConnectionChangeEvent(boolean isConnected, String msg) {
        ConnectionChangeEvent cce = new ConnectionChangeEvent(this, isConnected, msg);

        for (ConnectionChangeListener ccl : connectionListeners) {
            ccl.connectionChangeEventReceived(cce);
        }
    }

    @Override
    public void checkStatus() throws MPDException {
        boolean conn = this.server.isConnected();
        if (connected != conn) {
            connected = conn;
            fireConnectionChangeEvent(conn, "Connection Changed");
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
