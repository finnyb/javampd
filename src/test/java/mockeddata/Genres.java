/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mockeddata;

import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDGenre;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bfindeisen
 */
public class Genres {
    public static List<MPDGenre> genres = new ArrayList<>();
    public static final String NULL_GENRE = "No Genre";
    public static final HashMap<MPDGenre, Collection<MPDAlbum>> GENRE_ALBUM_MAP = new HashMap<>();


    public static MPDGenre addGenre(String genreName) {
        MPDGenre genre = new MPDGenre(genreName);
        if (!genres.contains(genre)) {
            genres.add(genre);
        }

        return genre;
    }

    public static void addGenre(MPDGenre genre, MPDAlbum album) {
        if (GENRE_ALBUM_MAP.get(genre) == null) {
            GENRE_ALBUM_MAP.put(genre, new ArrayList<MPDAlbum>());
        }

        if (!GENRE_ALBUM_MAP.get(genre).contains(album)) {
            GENRE_ALBUM_MAP.get(genre).add(album);
        }
    }
}
