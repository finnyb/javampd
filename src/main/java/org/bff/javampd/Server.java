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
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    void clearerror() throws MPDConnectionException, MPDResponseException;

    /**
     * Closes the connection to the MPD server.
     *
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    void close() throws MPDConnectionException, MPDResponseException;

    /**
     * Returns the MPD version running on the server.
     *
     * @return the version of the MPD
     */
    String getVersion();

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
