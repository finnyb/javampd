package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.admin.Admin;
import org.bff.javampd.admin.MPDAdmin;
import org.bff.javampd.album.AlbumConverter;
import org.bff.javampd.album.MPDAlbumConverter;
import org.bff.javampd.art.ArtworkFinder;
import org.bff.javampd.art.MPDArtworkFinder;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommandExecutor;
import org.bff.javampd.database.MPDTagLister;
import org.bff.javampd.database.TagLister;
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
        bind(TagLister.class).to(MPDTagLister.class);
        bind(SongConverter.class).to(MPDSongConverter.class);
        bind(AlbumConverter.class).to(MPDAlbumConverter.class);
        bind(ArtworkFinder.class).to(MPDArtworkFinder.class);
        bind(Clock.class).to(MPDSystemClock.class);
    }
}
