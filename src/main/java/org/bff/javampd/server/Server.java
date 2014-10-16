package org.bff.javampd.server;

import org.bff.javampd.admin.Admin;
import org.bff.javampd.database.DatabaseManager;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.statistics.ServerStatistics;

import java.net.InetAddress;

/**
 * @author bill
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

    Player getPlayer();

    Playlist getPlaylist();

    Admin getAdmin();

    DatabaseManager getDatabaseManager();

    ServerStatistics getServerStatistics();

    ServerStatus getServerStatus();

    StandAloneMonitor getMonitor();
}
