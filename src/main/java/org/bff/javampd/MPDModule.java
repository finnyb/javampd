package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.admin.MPDAdmin;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.database.MPDTagLister;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.monitor.*;
import org.bff.javampd.player.MPDPlayer;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.MPDPlaylist;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.server.MPDServerStatus;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSongConverter;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.statistics.MPDServerStatistics;
import org.bff.javampd.statistics.ServerStatistics;

/**
 * Initializes the DI bindings
 *
 * @author bill
 */
public class MPDModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Admin.class).to(MPDAdmin.class);
        bind(Player.class).to(MPDPlayer.class);
        bind(Playlist.class).to(MPDPlaylist.class);
        bind(ServerStatus.class).to(MPDServerStatus.class);
        bind(ServerStatistics.class).to(MPDServerStatistics.class);
        bind(Player.class).to(MPDPlayer.class);
        bind(CommandExecutor.class).to(MPDCommandExecutor.class);
        bind(StandAloneMonitor.class).to(MPDStandAloneMonitor.class);
        bind(OutputMonitor.class).to(MPDOutputMonitor.class);
        bind(TrackMonitor.class).to(MPDTrackMonitor.class);
        bind(ConnectionMonitor.class).to(MPDConnectionMonitor.class);
        bind(VolumeMonitor.class).to(MPDVolumeMonitor.class);
        bind(PlayerMonitor.class).to(MPDPlayerMonitor.class);
        bind(BitrateMonitor.class).to(MPDBitrateMonitor.class);
        bind(PlaylistMonitor.class).to(MPDPlaylistMonitor.class);
        bind(ErrorMonitor.class).to(MPDErrorMonitor.class);
        bind(TagLister.class).to(MPDTagLister.class);
        bind(SongConverter.class).to(MPDSongConverter.class);
    }
}
