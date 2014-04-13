package org.bff.javampd.monitor;

import org.bff.javampd.Server;
import org.bff.javampd.events.ConnectionChangeListener;

public interface ConnectionMonitor extends Monitor {

    /**
     * Adds a {@link org.bff.javampd.events.ConnectionChangeListener} to this object to receive
     * {@link org.bff.javampd.events.ConnectionChangeEvent}s.
     *
     * @param ccl the ConnectionChangeListener to add
     */
    void addConnectionChangeListener(ConnectionChangeListener ccl);

    /**
     * Removes a {@link ConnectionChangeListener} from this object.
     *
     * @param ccl the ConnectionChangeListener to remove
     */
    void removeConnectionChangeListener(ConnectionChangeListener ccl);

    /**
     * Set the server to monitor connectivity on
     *
     * @param server server for monitoring
     */
    void setServer(Server server);

    boolean isConnected();
}
