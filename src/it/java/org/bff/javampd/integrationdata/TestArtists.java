package org.bff.javampd.integrationdata;

import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;

import java.util.*;

/**
 * @author bfindeisen
 */
public class TestArtists {
    public static final String NULL_ARTIST = "";

    private static List<MPDArtist> artists = new ArrayList<>();

    public static final Map<MPDArtist, Collection<MPDAlbum>> TEST_ARTIST_ALBUM_MAP =
            new HashMap<>();

    static MPDArtist addArtist(String artistName) {
        MPDArtist artist = new MPDArtist(artistName);
        if (!getArtists().contains(artist)) {
            getArtists().add(artist);
        }

        return artist;
    }

    static void addAlbum(MPDArtist artist, MPDAlbum album) {
        TEST_ARTIST_ALBUM_MAP.computeIfAbsent(artist, k -> new ArrayList<>());

        if (!TEST_ARTIST_ALBUM_MAP.get(artist).contains(album)) {
            TEST_ARTIST_ALBUM_MAP.get(artist).add(album);
        }
    }

    public static List<MPDArtist> getArtists() {
        return artists;
    }
}
