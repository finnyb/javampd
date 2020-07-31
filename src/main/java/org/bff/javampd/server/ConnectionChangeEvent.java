package org.bff.javampd.server;

import java.util.EventObject;

/**
 * An event used to identify a change in connection status.
 *
 * @author Bill
 * @version 1.0
 */
public class ConnectionChangeEvent extends EventObject {
  private boolean connected;

  /**
   * Creates a new instance of ConnectionChangeEvent
   *
   * @param source      the object on which the Event initially occurred
   * @param isConnected the connection status
   */
  public ConnectionChangeEvent(Object source, boolean isConnected) {
    super(source);
    this.connected = isConnected;
  }

  /**
   * Returns true if there is a connection with the MPD server.  If there is no
   * connection returns false.
   *
   * @return true if connected; false otherwise
   */
  public boolean isConnected() {
    return connected;
  }
}
