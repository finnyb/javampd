/*
 * MPD.java
 *
 * Created on September 27, 2005, 1:34 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */
package org.bff.javampd;

import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPD extends CommandExecutor {

    private final Logger logger = LoggerFactory.getLogger(MPD.class);
    private final ServerProperties commandProperties;
    private int port;
    private InetAddress serverAddress;
    private MPDPlayer mpdPlayer;
    private MPDPlaylist mpdPlaylist;
    private MPDDatabase mpdDatabase;
    private MPDServerStatus mpdServerStatus;
    private MPDServerStatistics mpdServerStatistics;
    private MPDEventRelayer mpdEventRelayer;
    private MPDAdmin mpdAdmin;
    private String version;
    private int timeout;

    private static final int MPD_DEFAULT_PORT = 6600;
    private static final String DEFAULT_MPD_SERVER = "localhost";

    /**
     * Establishes a new mpd instance using default server values
     * of localhost, port 6600 with no user or password
     *
     * @throws MPDConnectionException        if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD() throws MPDConnectionException, UnknownHostException {
        this(DEFAULT_MPD_SERVER);
    }

    /**
     * Creates a new instance of MPD without authentication using the
     * default MPD port of 6600
     *
     * @param server the MPD Server
     * @throws MPDConnectionException        if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server) throws UnknownHostException, MPDConnectionException {
        this(server, MPD_DEFAULT_PORT);
    }

    /**
     * Creates a new instance of MPD without authentication
     *
     * @param server the MPD Server
     * @param port   the port MPD is listening on
     * @throws MPDConnectionException        if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port) throws UnknownHostException, MPDConnectionException {
        this(server, port, null);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server   the MPD server
     * @param port     the port MPD is listening on
     * @param password the password to authenticate with
     * @throws MPDConnectionException        if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, String password) throws UnknownHostException, MPDConnectionException {
        this(server, port, password, 0);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server  the MPD server
     * @param port    the port MPD is listening on
     * @param timeout the amount of time in milliseconds to wait for the MPD connection
     * @throws MPDConnectionException        if there is a problem sending the command to the server
     * @throws java.net.UnknownHostException If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, int timeout) throws UnknownHostException, MPDConnectionException {
        this(server, port, null, timeout);
    }

    /**
     * Creates a new instance of MPD with authentication.  The password
     * is used to gain access to the commands setup by the MPD administrator.
     * Please note the password is sent plain text.
     *
     * @param server   the MPD server
     * @param port     the port MPD is listening on
     * @param password the password to authenticate with
     * @param timeout  the amount of time in milliseconds to wait for the MPD connection
     * @throws MPDConnectionException if there is a problem sending the command to the server
     * @throws UnknownHostException   If the host name used for the server is unknown to dns
     */
    public MPD(String server, int port, String password, int timeout) throws UnknownHostException, MPDConnectionException {
        try {
            this.serverAddress = InetAddress.getByName(server);
            this.port = port;
            this.timeout = timeout;
            this.commandProperties = new ServerProperties();

            setMpd(this);

            this.version = getMPDVersion();

            if (password != null) {
                authenticate(password);
            }
        } catch (Exception e) {
            throw new MPDConnectionException(e);
        }
    }

    /**
     * Clears the current error message in the MPD status
     *
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public void clearerror() throws MPDConnectionException, MPDResponseException {
        sendMPDCommand(commandProperties.getClearError());
    }

    /**
     * Closes the connection to the MPD server.
     *
     * @throws MPDResponseException   if the MPD response generates an error
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public void close() throws MPDConnectionException, MPDResponseException {
        sendMPDCommand(commandProperties.getClose());
    }

    /**
     * Returns the MPD version running on the server.
     *
     * @return the version of the MPD
     */
    public String getVersion() {
        return version;
    }

    /**
     * Returns a {@link MPDPlayer} using this class as the connection
     * for the player.  Multiple calls to this method will return
     * the same {@link MPDPlayer} class.
     *
     * @return the mpd player
     */
    public synchronized MPDPlayer getMPDPlayer() {
        if (mpdPlayer == null) {
            mpdPlayer = new MPDPlayer(this);
        }
        return mpdPlayer;
    }

    /**
     * Returns a {@link MPDPlaylist} using this class as the connection
     * for the playlist.  Multiple calls to this method will return
     * the same {@link MPDPlaylist} class.
     *
     * @return the mpd player
     */
    public synchronized MPDPlaylist getMPDPlaylist() {
        if (mpdPlaylist == null) {
            mpdPlaylist = new MPDPlaylist(this);
        }
        return mpdPlaylist;
    }

    /**
     * Returns a {@link MPDDatabase} using this class as the connection
     * for the database.  Multiple calls to this method will return
     * the same {@link MPDDatabase} class.
     *
     * @return the mpd player
     */
    public synchronized MPDDatabase getMPDDatabase() {
        if (mpdDatabase == null) {
            mpdDatabase = new MPDDatabase(this);
        }
        return mpdDatabase;
    }

    /**
     * Returns a {@link MPDServerStatus} using this class as the connection
     * Multiple calls to this method will return the same {@link MPDServerStatus} class.
     *
     * @return the {@link MPDServerStatus}
     */
    public synchronized MPDServerStatus getMPDServerStatus() {
        if (mpdServerStatus == null) {
            mpdServerStatus = new MPDServerStatus(this);
        }
        return mpdServerStatus;
    }

    /**
     * Returns a {@link MPDServerStatistics} using this class as the connection
     * Multiple calls to this method will return the same {@link MPDServerStatistics} class.
     *
     * @return the {@link MPDServerStatistics}
     */
    public MPDServerStatistics getMPDServerStatistics() {
        if (mpdServerStatistics == null) {
            mpdServerStatistics = new MPDServerStatistics(this);
        }
        return mpdServerStatistics;
    }

    /**
     * Returns a {@link MPDAdmin} using this class as the connection
     * for the administrator.  Multiple calls to this method will return
     * the same {@link MPDAdmin} class.
     *
     * @return the mpd player
     */
    public synchronized MPDAdmin getMPDAdmin() {
        if (mpdAdmin == null) {
            mpdAdmin = new MPDAdmin(this);
        }
        return mpdAdmin;
    }

    /**
     * Returns a {@link MPDEventRelayer} using this class as the connection.
     * Multiple calls to this method will return
     * the same {@link MPDEventRelayer} class.
     *
     * @return the MPDEventRelayer
     */
    public synchronized MPDEventRelayer getMPDEventRelayer() {
        if (mpdEventRelayer == null) {
            mpdEventRelayer = new MPDEventRelayer(this);
        }
        return mpdEventRelayer;
    }

    /**
     * Determines if there is a connection to the MPD server.
     *
     * @return true if connected to server , false if not
     */
    public boolean isConnected() {
        if (ping()) {
            return true;
        } else {
            return false;
        }
    }

    private void authenticate(String password) throws MPDConnectionException, MPDResponseException {
        sendMPDCommand(commandProperties.getPassword(), password);
    }

    private boolean ping() {
        try {
            sendMPDCommand(commandProperties.getPing());
        } catch (MPDException e) {
            return false;
        }
        return true;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(InetAddress serverAddress) {
        this.serverAddress = serverAddress;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
}