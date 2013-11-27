package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.properties.*;

/**
 * Initializes the DI bindings
 *
 * @author bill
 */
public class MPDModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(Server.class).to(MPD.class);
        bind(Admin.class).to(MPDAdmin.class);
        bind(Database.class).to(MPDDatabase.class);
        bind(Player.class).to(MPDPlayer.class);
        bind(Playlist.class).to(MPDPlaylist.class);
        bind(ServerStatus.class).to(MPDServerStatus.class);
        bind(ServerStatistics.class).to(MPDServerStatistics.class);
        bind(Player.class).to(MPDPlayer.class);
        bind(EventRelayer.class).to(MPDEventRelayer.class);
        bind(CommandExecutor.class).to(MPDCommandExecutor.class);
        bind(StandAloneMonitor.class).to(MPDStandAloneMonitor.class);

        bind(AdminProperties.class);
        bind(DatabaseProperties.class);
        bind(PlayerProperties.class);
        bind(PlaylistProperties.class);
        bind(ResponseProperties.class);
        bind(ServerProperties.class);
    }
}
