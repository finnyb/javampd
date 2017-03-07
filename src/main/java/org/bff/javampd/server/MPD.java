/*
 * MPD.java
 *
 * Created on September 27, 2005, 1:34 PM
 */
package org.bff.javampd.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import org.bff.javampd.MPDDatabaseModule;
import org.bff.javampd.MPDException;
import org.bff.javampd.MPDModule;
import org.bff.javampd.MPDMonitorModule;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.art.ArtworkFinder;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.monitor.ConnectionMonitor;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.SongSearcher;
import org.bff.javampd.statistics.ServerStatistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 * <p>
 * Uses the builder pattern for construction.  Use {@link MPD.Builder#build()}
 * to construct.
 * <p>
 * Defaults are:
 * <p>
 * server - localhost
 * port - 6600
 * no timeout
 * no password
 *
 * @author Bill
 */
@Singleton
public class MPD implements Server {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPD.class);

    private int port;
    private InetAddress address;
    private String password;
    private int timeout;
    private boolean closed;

    private static final int DEFAULT_PORT = 6600;
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_SERVER = "localhost";

    private final ServerProperties serverProperties;
    private final CommandExecutor commandExecutor;

    private final Player player;
    private final Playlist playlist;
    private final Admin admin;
    private final ServerStatistics serverStatistics;
    private final ServerStatus serverStatus;
    private final StandAloneMonitor standAloneMonitor;
    private final MusicDatabase musicDatabase;
    private final SongSearcher songSearcher;
    private final ArtworkFinder artworkFinder;

    private MPD(Builder builder) {
        try {
            this.address = InetAddress.getByName(builder.server);
            this.password = builder.password;
            this.port = builder.port;
            this.timeout = builder.timeout;
            this.serverProperties = builder.serverProperties;
            this.commandExecutor = builder.commandExecutor;
            this.player = builder.player;
            this.playlist = builder.playlist;
            this.admin = builder.admin;
            this.serverStatistics = builder.serverStatistics;
            this.serverStatus = builder.serverStatus;
            this.standAloneMonitor = builder.standAloneMonitor;
            this.songSearcher = builder.songSearcher;
            this.musicDatabase = builder.musicDatabase;
            this.artworkFinder = builder.artworkFinder;

            this.commandExecutor.setMpd(this);
            authenticate();
        } catch (Exception e) {
            LOGGER.error("Error creating mpd instance to server {} on port {}", this.address, this.port, e);
            throw new MPDConnectionException(e);
        }
    }

    private void authenticate() {
        if (usingPassword()) {
            commandExecutor.usePassword(this.password);
            commandExecutor.authenticate();
        }
    }

    @Override
    public void clearError() {
        commandExecutor.sendCommand(serverProperties.getClearError());
    }

    @Override
    public void close() {
        try {
            commandExecutor.sendCommand(serverProperties.getClose());
            this.closed = true;
        } finally {
            commandExecutor.close();
        }
    }

    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public String getVersion() {
        return commandExecutor.getMPDVersion();
    }

    @Override
    public boolean isConnected() {
        return ping();
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
    public MusicDatabase getMusicDatabase() {
        return this.musicDatabase;
    }

    @Override
    public SongSearcher getSongSearcher() {
        return this.songSearcher;
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

    @Override
    public CommandExecutor getCommandExecutor() {
        return this.commandExecutor;
    }

    private boolean usingPassword() {
        return this.password != null || "".equals(this.password);
    }

    @Override
    public ArtworkFinder getArtworkFinder() {
        return this.artworkFinder;
    }

    public static class Builder {
        private int port = DEFAULT_PORT;
        private String server = DEFAULT_SERVER;
        private int timeout = DEFAULT_TIMEOUT;
        private String password;
        private ServerProperties serverProperties;
        private CommandExecutor commandExecutor;
        private Player player;
        private Playlist playlist;
        private Admin admin;
        private ServerStatistics serverStatistics;
        private ServerStatus serverStatus;
        private StandAloneMonitor standAloneMonitor;
        private MusicDatabase musicDatabase;
        private Injector injector;
        private SongSearcher songSearcher;
        private ArtworkFinder artworkFinder;

        public Builder() {
            injector = Guice.createInjector(new MPDModule(), new MPDDatabaseModule(), new MPDMonitorModule());
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

        public MPD build() {
            MPD mpd = new MPD(this);
            injector.getInstance(ConnectionMonitor.class).setServer(mpd);
            return mpd;
        }

        private void bind(Injector injector) {
            this.serverProperties = injector.getInstance(ServerProperties.class);
            this.player = injector.getInstance(Player.class);
            this.playlist = injector.getInstance(Playlist.class);
            this.admin = injector.getInstance(Admin.class);
            this.serverStatistics = injector.getInstance(ServerStatistics.class);
            this.serverStatus = injector.getInstance(ServerStatus.class);
            this.musicDatabase = injector.getInstance(MusicDatabase.class);
            this.songSearcher = injector.getInstance(SongSearcher.class);
            this.commandExecutor = injector.getInstance(CommandExecutor.class);
            this.artworkFinder = injector.getInstance(ArtworkFinder.class);
        }

        private void bindMonitorAndRelay(Injector injector) {
            this.standAloneMonitor = injector.getInstance(StandAloneMonitor.class);
        }
    }
}