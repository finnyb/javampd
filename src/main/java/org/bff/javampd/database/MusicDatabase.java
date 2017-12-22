package org.bff.javampd.database;

import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.file.FileDatabase;
import org.bff.javampd.genre.GenreDatabase;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.SongDatabase;
import org.bff.javampd.year.DateDatabase;

/**
 * Central location for getting MPD database access
 *
 * @author Bill
 */
public interface MusicDatabase {
    ArtistDatabase getArtistDatabase();

    AlbumDatabase getAlbumDatabase();

    GenreDatabase getGenreDatabase();

    PlaylistDatabase getPlaylistDatabase();

    FileDatabase getFileDatabase();

    DateDatabase getDateDatabase();

    SongDatabase getSongDatabase();
}
