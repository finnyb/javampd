package org.bff.javampd.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import lombok.Builder;
import lombok.Getter;
import org.bff.javampd.MPDDatabaseModule;
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
import java.util.Optional;

/**
 * MPD represents a connection to a MPD server.  The commands
 * are maintained in a properties file called mpd.properties.
 * <p>
 * Uses the builder pattern for construction.
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
@Getter
@Singleton
public class MPD extends MPDServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPD.class);

    private int port = 6600;
    private String server = "localhost";

    private final int timeout;
    private final String password;
    private final InetAddress address;

    @Builder
    public MPD(String server,
               Integer port,
               int timeout,
               String password) {
        this.server = Optional.ofNullable(server).orElse(this.server);
        this.port = Optional.ofNullable(port).orElse(this.port);
        this.timeout = timeout;
        this.password = password;

        try {
            this.address = InetAddress.getByName(this.server);
            init();
            authenticate();
        } catch (Exception e) {
            throw new MPDConnectionException(e);
        }
    }

    void init() {
        var injector = Guice.createInjector(new MPDModule(),
                new MPDDatabaseModule(),
                new MPDMonitorModule());

        bind(injector);
        setMpd(this);
        authenticate();
        injector.getInstance(ConnectionMonitor.class).setServer(this);
    }

    private void bind(Injector injector) {
        serverProperties = injector.getInstance(ServerProperties.class);
        player = injector.getInstance(Player.class);
        playlist = injector.getInstance(Playlist.class);
        admin = injector.getInstance(Admin.class);
        serverStatistics = injector.getInstance(ServerStatistics.class);
        serverStatus = injector.getInstance(ServerStatus.class);
        musicDatabase = injector.getInstance(MusicDatabase.class);
        songSearcher = injector.getInstance(SongSearcher.class);
        commandExecutor = injector.getInstance(CommandExecutor.class);
        artworkFinder = injector.getInstance(ArtworkFinder.class);
        standAloneMonitor = injector.getInstance(StandAloneMonitor.class);
    }

    void authenticate() {
        if (usingPassword()) {
            super.authenticate(this.password);
        }
    }

    private boolean usingPassword() {
        return this.password != null && !"".equals(this.password);
    }
}
