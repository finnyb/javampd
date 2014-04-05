package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDResponseException;

import java.net.InetAddress;

/**
 * @author bill
 * @since: 11/24/13 10:33 AM
 */
public interface Server {
    /**
     * Clears the current error message in the MPD status
     *
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    void clearerror() throws MPDResponseException;

    /**
     * Closes the connection to the MPD server.
     *
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    void close() throws MPDResponseException;

    /**
     * Returns the MPD version running on the server.
     *
     * @return the version of the MPD
     */
    String getVersion() throws MPDResponseException;

    /**
     * Determines if there is a connection to the MPD server.
     *
     * @return true if connected to server , false if not
     */
    boolean isConnected();

    int getPort();

    InetAddress getAddress();

    int getTimeout();

    Database getDatabase();

    Player getPlayer();

    Playlist getPlaylist();

    Admin getAdmin();

    ServerStatistics getServerStatistics();

    ServerStatus getServerStatus();

    StandAloneMonitor getMonitor();
}
