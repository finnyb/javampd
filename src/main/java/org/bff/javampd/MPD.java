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
import java.net.UnknownHostException;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 * <p/>
 * Uses the builder pattern for construction.  Use {@link org.bff.javampd.MPD.Builder#build()}
 * to construct.
 * <p/>
 * Defaults are:
 * <p/>
 * server --> localhost
 * port --> 6600
 * no timeout
 * no password
 *
 * @author Bill
 */
public class MPD implements Server {

    private int port;
    private InetAddress address;
    private String version;
    private int timeout;

    private static final int DEFAULT_PORT = 6600;
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_SERVER = "localhost";

    private ServerProperties serverProperties;
    private CommandExecutor commandExecutor;
    private Database database;
    private Player player;
    private Playlist playlist;
    private Admin admin;
    private ServerStatistics serverStatistics;
    private ServerStatus serverStatus;
    private StandAloneMonitor standAloneMonitor;

    private static final Logger LOGGER = LoggerFactory.getLogger(MPD.class);

    private MPD(Builder builder) throws MPDConnectionException {
        try {
            this.address = InetAddress.getByName(builder.server);
            this.port = builder.port;
            this.timeout = builder.timeout;

            Injector injector = Guice.createInjector(new MPDModule());
            bind(injector);

            if (builder.password != null) {
                authenticate(builder.password);
            }

            bindMonitorAndRelay(injector);
            this.version = commandExecutor.getMPDVersion();
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
        this.commandExecutor.setMpd(this);
    }

    private void bindMonitorAndRelay(Injector injector) {
        this.standAloneMonitor = injector.getInstance(StandAloneMonitor.class);
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
    public String getVersion() {
        return version;
    }

    @Override
    public boolean isConnected() {
        return ping();
    }

    private void authenticate(String password) throws MPDResponseException {
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

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public InetAddress getAddress() {
        return address;
    }

    public void setAddress(InetAddress address) {
        this.address = address;
    }

    @Override
    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
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

    public static class Builder {
        private int port = DEFAULT_PORT;
        private String server = DEFAULT_SERVER;
        private int timeout = DEFAULT_TIMEOUT;
        private String password;

        public Builder server(String server) throws UnknownHostException {
            this.server = server;
            return this;
        }

        public Builder port(int port) {
            this.port = port;
            return this;
        }

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public MPD build() throws MPDConnectionException {
            return new MPD(this);
        }
    }
}