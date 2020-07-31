package org.bff.javampd;

import com.google.inject.AbstractModule;
import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.album.MPDAlbumDatabase;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtistDatabase;
import org.bff.javampd.database.MPDMusicDatabase;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.file.FileDatabase;
import org.bff.javampd.file.MPDFileDatabase;
import org.bff.javampd.genre.GenreDatabase;
import org.bff.javampd.genre.MPDGenreDatabase;
import org.bff.javampd.playlist.MPDPlaylistDatabase;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.MPDSongDatabase;
import org.bff.javampd.song.MPDSongSearcher;
import org.bff.javampd.song.SongDatabase;
import org.bff.javampd.song.SongSearcher;
import org.bff.javampd.year.DateDatabase;
import org.bff.javampd.year.MPDDateDatabase;

/**
 * Initializes the DI bindings
 *
 * @author bill
 */
public class MPDDatabaseModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(ArtistDatabase.class).to(MPDArtistDatabase.class);
    bind(AlbumDatabase.class).to(MPDAlbumDatabase.class);
    bind(SongDatabase.class).to(MPDSongDatabase.class);
    bind(GenreDatabase.class).to(MPDGenreDatabase.class);
    bind(PlaylistDatabase.class).to(MPDPlaylistDatabase.class);
    bind(FileDatabase.class).to(MPDFileDatabase.class);
    bind(DateDatabase.class).to(MPDDateDatabase.class);
    bind(MusicDatabase.class).to(MPDMusicDatabase.class);
    bind(SongSearcher.class).to(MPDSongSearcher.class);
  }
}
