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
import org.bff.javampd.monitor.ConnectionMonitor;
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
 *
 *  server --> localhost
 *  port --> 6600
 *  no timeout
 *  no password
 *
 * @author Bill
 */
public class MPD implements Server {

    private int port;
    private InetAddress address;
    private int timeout;

    private static final int DEFAULT_PORT = 6600;
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_SERVER = "localhost";

    private final ServerProperties serverProperties;
    private final CommandExecutor commandExecutor;
    private final Database database;
    private final Player player;
    private final Playlist playlist;
    private final Admin admin;
    private final ServerStatistics serverStatistics;
    private final ServerStatus serverStatus;
    private final StandAloneMonitor standAloneMonitor;

    private static final Logger LOGGER = LoggerFactory.getLogger(MPD.class);

    private MPD(Builder builder) throws MPDConnectionException {
        try {
            this.address = InetAddress.getByName(builder.server);
            this.port = builder.port;
            this.timeout = builder.timeout;
            this.serverProperties = builder.serverProperties;
            this.commandExecutor = builder.commandExecutor;
            this.database = builder.database;
            this.player = builder.player;
            this.playlist = builder.playlist;
            this.admin = builder.admin;
            this.serverStatistics = builder.serverStatistics;
            this.serverStatus = builder.serverStatus;
            this.standAloneMonitor = builder.standAloneMonitor;

            this.commandExecutor.setMpd(this);

            if (builder.password != null) {
                authenticate(builder.password);
            }
        } catch (Exception e) {
            throw new MPDConnectionException(e);
        }
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

    public static class Builder {
        private int port = DEFAULT_PORT;
        private String server = DEFAULT_SERVER;
        private int timeout = DEFAULT_TIMEOUT;
        private String password;
        private ServerProperties serverProperties;
        private CommandExecutor commandExecutor;
        private Database database;
        private Player player;
        private Playlist playlist;
        private Admin admin;
        private ServerStatistics serverStatistics;
        private ServerStatus serverStatus;
        private StandAloneMonitor standAloneMonitor;
        private Injector injector;
        
        public Builder() {
            injector = Guice.createInjector(new MPDModule());
            bind(injector);
            bindMonitorAndRelay(injector);

        }
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
            MPD mpd = new MPD(this);
            injector.getInstance(ConnectionMonitor.class).setServer(mpd);
            return mpd;
        }

        /**
         * Performs dependency injection
         *
         * @throws IOException            if there is a problem connecting to the server
         * @throws MPDConnectionException if there is a problem sending the command to the server
         */
        private void bind(Injector injector) {
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
        }
    }
}