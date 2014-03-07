/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mockeddata;

import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDArtist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bfindeisen
 */
public class Artists {

    public static List<MPDArtist> artists = new ArrayList<>();

    public static final HashMap<MPDArtist, Collection<MPDAlbum>> TEST_ARTIST_ALBUM_MAP =
            new HashMap<>();

    public static MPDArtist addArtist(String artistName) {
        MPDArtist artist = new MPDArtist(artistName);
        if (!artists.contains(artist)) {
            artists.add(artist);
        }

        return artist;
    }

    public static void addAlbum(MPDArtist artist, MPDAlbum album) {
        if (TEST_ARTIST_ALBUM_MAP.get(artist) == null) {
            TEST_ARTIST_ALBUM_MAP.put(artist, new ArrayList<MPDAlbum>());
        }

        if (!TEST_ARTIST_ALBUM_MAP.get(artist).contains(album)) {
            TEST_ARTIST_ALBUM_MAP.get(artist).add(album);
        }
    }
}
