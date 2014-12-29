package org.bff.javampd.database;

import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.file.FileDatabase;
import org.bff.javampd.genre.GenreDatabase;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.SongDatabase;
import org.bff.javampd.song.SongSearcher;
import org.bff.javampd.year.YearDatabase;

/**
 * Central location for getting MPD database access paths
 *
 * @author Bill
 */
public interface DatabaseManager {
    ArtistDatabase getArtistDatabase();

    AlbumDatabase getAlbumDatabase();

    GenreDatabase getGenreDatabase();

    PlaylistDatabase getPlaylistDatabase();

    FileDatabase getFileDatabase();

    YearDatabase getYearDatabase();

    SongDatabase getSongDatabase();

    SongSearcher getSongSearcher();
}
