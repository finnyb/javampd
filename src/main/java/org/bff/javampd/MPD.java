/*
 * MPD.java
 *
 * Created on September 27, 2005, 1:34 PM
 */
package org.bff.javampd;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bff.javampd.exception.MPDConnectionException;
import org.bff.javampd.exception.MPDException;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 *
 * @author Bill
 */
public class MPD implements Server {

    private int port;
    private InetAddress address;
    private int timeout;

    private static final int MPD_DEFAULT_PORT = 6600;
    private static final String DEFAULT_MPD_SERVER = "localhost";

    private ServerProperties serverProperties;
    private CommandExecutor commandExecutor;
    private Database database;
    private Player player;
    private Playlist playlist;
    private Admin admin;
    private ServerStatistics serverStatistics;
    private ServerStatus serverStatus;
    private StandAloneMonitor standAloneMonitor;
    private EventRelayer eventRelayer;

    private static final Logger LOGGER = LoggerFactory.getLogger(MPD.class);

    /**
     * Establishes a new mpd instance using default server values
     * of localhost, port 6600 with no user or password
     *
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public MPD() throws MPDConnectionException {
        this(DEFAULT_MPD_SERVER);
    }

    /**
     * Creates a new instance of MPD without authentication using the
     * default MPD port of 6600
     *
     * @param server the MPD Server
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public MPD(String server) throws MPDConnectionException {
        this(server, MPD_DEFAULT_PORT);
    }

    /**
     * Creates a new instance of MPD without authentication
     *
     * @param server the MPD Server
     * @param port   the port MPD is listening on
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public MPD(String server, int port) throws MPDConnectionException {
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
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public MPD(String server, int port, String password) throws MPDConnectionException {
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
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    public MPD(String server, int port, int timeout) throws MPDConnectionException {
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
     */
    public MPD(String server, int port, String password, int timeout) throws MPDConnectionException {
        try {
            this.address = InetAddress.getByName(server);
            this.port = port;
            this.timeout = timeout;

            Injector injector = Guice.createInjector(new MPDModule());
            bind(injector);
            this.commandExecutor.setMpd(this);

            if (password != null) {
                authenticate(password);
            }

            bindMonitorAndRelay(injector);
        } catch (Exception e) {
            throw new MPDConnectionException(e);
        }
    }

    /**
     * Performs dependency injection
     *
     * @throws IOException            if there is a problem connecting to the server
     * @throws MPDConnectionException if there is a problem sending the command to the server
     */
    private void bind(Injector injector) throws MPDConnectionException {
        this.serverProperties = injector.getInstance(ServerProperties.class);
        this.database = injector.getInstance(Database.class);
        this.player = injector.getInstance(Player.class);
        this.playlist = injector.getInstance(Playlist.class);
        this.admin = injector.getInstance(Admin.class);
        this.serverStatistics = injector.getInstance(ServerStatistics.class);
        this.serverStatus = injector.getInstance(ServerStatus.class);

        this.commandExecutor = injector.getInstance(CommandExecutor.class);
    }

    private void bindMonitorAndRelay(Injector injector) {
        this.standAloneMonitor = injector.getInstance(StandAloneMonitor.class);
        this.standAloneMonitor.setServer(this);
        this.eventRelayer = injector.getInstance(EventRelayer.class);
        this.eventRelayer.setServer(this);
    }

    @Override
    public void clearerror() throws MPDResponseException {
        commandExecutor.sendCommand(serverProperties.getClearError());
    }

    @Override
    public void close() throws MPDResponseException {
        commandExecutor.sendCommand(serverProperties.getClose());
    }

    @Override
    public String getVersion() throws MPDResponseException {
        return commandExecutor.getMPDVersion();
    }

    @Override
    public boolean isConnected() {
        return ping();
    }

    public void authenticate(String password) throws MPDResponseException {
        commandExecutor.sendCommand(serverProperties.getPassword(), password);
    }

    private boolean ping() {
        try {
            commandExecutor.sendCommand(serverProperties.getPing());
        } catch (MPDException e) {
            LOGGER.error("Could not ping MPD", e);
            return false;
        }
        return true;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public InetAddress getAddress() {
        return address;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    @Override
    public Database getDatabase() {
        return this.database;
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Playlist getPlaylist() {
        return this.playlist;
    }

    @Override
    public Admin getAdmin() {
        return this.admin;
    }

    @Override
    public ServerStatistics getServerStatistics() {
        return this.serverStatistics;
    }

    @Override
    public ServerStatus getServerStatus() {
        return this.serverStatus;
    }

    @Override
    public StandAloneMonitor getMonitor() {
        return this.standAloneMonitor;
    }
}