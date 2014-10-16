package org.bff.javampd.database;

import com.google.inject.Inject;
import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.file.FileDatabase;
import org.bff.javampd.genre.GenreDatabase;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.SongDatabase;
import org.bff.javampd.year.YearDatabase;

import javax.inject.Singleton;

@Singleton
public class MPDDatabaseManager implements DatabaseManager {
    private final ArtistDatabase artistDatabase;
    private final AlbumDatabase albumDatabase;
    private final GenreDatabase genreDatabase;
    private final PlaylistDatabase playlistDatabase;
    private final FileDatabase fileDatabase;
    private final YearDatabase yearDatabase;
    private final SongDatabase songDatabase;

    @Inject
    public MPDDatabaseManager(ArtistDatabase artistDatabase,
                              AlbumDatabase albumDatabase,
                              GenreDatabase genreDatabase,
                              PlaylistDatabase playlistDatabase,
                              FileDatabase fileDatabase,
                              YearDatabase yearDatabase,
                              SongDatabase songDatabase) {
        this.artistDatabase = artistDatabase;
        this.albumDatabase = albumDatabase;
        this.genreDatabase = genreDatabase;
        this.playlistDatabase = playlistDatabase;
        this.fileDatabase = fileDatabase;
        this.yearDatabase = yearDatabase;
        this.songDatabase = songDatabase;
    }

    @Override
    public ArtistDatabase getArtistDatabase() {
        return artistDatabase;
    }

    @Override
    public AlbumDatabase getAlbumDatabase() {
        return albumDatabase;
    }

    @Override
    public GenreDatabase getGenreDatabase() {
        return genreDatabase;
    }

    @Override
    public PlaylistDatabase getPlaylistDatabase() {
        return playlistDatabase;
    }

    @Override
    public FileDatabase getFileDatabase() {
        return fileDatabase;
    }

    @Override
    public YearDatabase getYearDatabase() {
        return yearDatabase;
    }

    @Override
    public SongDatabase getSongDatabase() {
        return songDatabase;
    }
}
