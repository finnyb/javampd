package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.monitor.*;
import org.bff.javampd.properties.*;

/**
 * Initializes the DI bindings
 *
 * @author bill
 */
public class MPDModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Admin.class).to(MPDAdmin.class);
        bind(Database.class).to(MPDDatabase.class);
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

        bind(AdminProperties.class);
        bind(DatabaseProperties.class);
        bind(PlayerProperties.class);
        bind(PlaylistProperties.class);
        bind(ResponseProperties.class);
        bind(ServerProperties.class);
    }
}
