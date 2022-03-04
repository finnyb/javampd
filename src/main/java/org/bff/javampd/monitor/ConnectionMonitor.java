package org.bff.javampd.monitor;

import org.bff.javampd.server.ConnectionChangeListener;
import org.bff.javampd.server.Server;

public interface ConnectionMonitor extends Monitor {

  /**
   * Adds a {@link org.bff.javampd.server.ConnectionChangeListener} to this object to receive {@link
   * org.bff.javampd.server.ConnectionChangeEvent}s.
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

  /**
   * Returns whether the server is connected
   *
   * @return true if the server is connected
   */
  boolean isConnected();
}
