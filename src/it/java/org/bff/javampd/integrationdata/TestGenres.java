package org.bff.javampd.integrationdata;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bfindeisen
 */
public class TestGenres {
    private static List<MPDGenre> genres = new ArrayList<>();
    private static final HashMap<MPDGenre, Collection<MPDAlbum>> GENRE_ALBUM_MAP = new HashMap<>();
    private static final HashMap<MPDGenre, Collection<MPDArtist>> GENRE_ARTIST_MAP = new HashMap<>();

    public static MPDGenre addGenre(String genreName) {
        MPDGenre genre = new MPDGenre(genreName);
        if (!getGenres().contains(genre)) {
            getGenres().add(genre);
        }

        return genre;
    }

    public static void addAlbum(MPDGenre genre, MPDAlbum album) {
        if (GENRE_ALBUM_MAP.get(genre) == null) {
            GENRE_ALBUM_MAP.put(genre, new ArrayList<MPDAlbum>());
        }

        if (!GENRE_ALBUM_MAP.get(genre).contains(album)) {
            GENRE_ALBUM_MAP.get(genre).add(album);
        }
    }

    public static void addArtist(MPDGenre genre, MPDArtist artist) {
        if (GENRE_ARTIST_MAP.get(genre) == null) {
            GENRE_ARTIST_MAP.put(genre, new ArrayList<MPDArtist>());
        }

        if (!GENRE_ARTIST_MAP.get(genre).contains(artist)) {
            GENRE_ARTIST_MAP.get(genre).add(artist);
        }
    }

    public static List<MPDGenre> getGenres() {
        return genres;
    }

    public static Collection<MPDAlbum> getAlbumsForGenre(MPDGenre genre) {
        return GENRE_ALBUM_MAP.get(genre);
    }

    public static Collection<MPDArtist> getArtistsForGenre(MPDGenre genre) {
        return GENRE_ARTIST_MAP.get(genre);
    }
}
