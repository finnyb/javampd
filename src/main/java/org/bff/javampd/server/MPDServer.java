package org.bff.javampd.server;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.MPDException;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.art.ArtworkFinder;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.monitor.StandAloneMonitor;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.SongSearcher;
import org.bff.javampd.statistics.ServerStatistics;

@Getter
@NoArgsConstructor
@Slf4j
public abstract class MPDServer implements Server {
    protected ServerProperties serverProperties;
    protected CommandExecutor commandExecutor;
    protected Player player;
    protected Playlist playlist;
    protected Admin admin;
    protected ServerStatistics serverStatistics;
    protected ServerStatus serverStatus;
    protected StandAloneMonitor standAloneMonitor;
    protected MusicDatabase musicDatabase;
    protected SongSearcher songSearcher;
    protected ArtworkFinder artworkFinder;
    protected boolean closed;

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
            log.error("Could not ping MPD", e);
            return false;
        }
        return true;
    }

    public void authenticate(String password) {
        commandExecutor.usePassword(password);
        commandExecutor.authenticate();
    }

    public void setMpd(MPD mpd) {
        this.commandExecutor.setMpd(mpd);
    }

    @Override
    public ArtworkFinder getArtworkFinder() {
        return this.artworkFinder;
    }

    @Override
    public boolean isClosed() {
        return this.closed;
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
    public StandAloneMonitor getStandAloneMonitor() {
        return this.standAloneMonitor;
    }
}
