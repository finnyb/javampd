package org.bff.javampd.database;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.bff.javampd.album.AlbumDatabase;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.file.FileDatabase;
import org.bff.javampd.genre.GenreDatabase;
import org.bff.javampd.playlist.PlaylistDatabase;
import org.bff.javampd.song.SongDatabase;
import org.bff.javampd.song.SongSearcher;
import org.bff.javampd.year.DateDatabase;

public class MPDMusicDatabase implements MusicDatabase {
    private final ArtistDatabase artistDatabase;
    private final AlbumDatabase albumDatabase;
    private final GenreDatabase genreDatabase;
    private final PlaylistDatabase playlistDatabase;
    private final FileDatabase fileDatabase;
    private final DateDatabase dateDatabase;
    private final SongDatabase songDatabase;
    private final SongSearcher songSearcher;

    @Inject
    public MPDMusicDatabase(Injector injector) {
        this.artistDatabase = injector.getInstance(ArtistDatabase.class);
        this.albumDatabase = injector.getInstance(AlbumDatabase.class);
        this.genreDatabase = injector.getInstance(GenreDatabase.class);
        this.playlistDatabase = injector.getInstance(PlaylistDatabase.class);
        this.fileDatabase = injector.getInstance(FileDatabase.class);
        this.dateDatabase = injector.getInstance(DateDatabase.class);
        this.songDatabase = injector.getInstance(SongDatabase.class);
        this.songSearcher = injector.getInstance(SongSearcher.class);
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
    public DateDatabase getDateDatabase() {
        return dateDatabase;
    }

    @Override
    public SongDatabase getSongDatabase() {
        return songDatabase;
    }

    @Override
    public SongSearcher getSongSearcher() {
        return songSearcher;
    }
}
