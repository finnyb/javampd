package org.bff.javampd.integrationdata;

import org.bff.javampd.album.MPDAlbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bill
 */
public class TestYears {

    private static List<String> years = new ArrayList<>();
    private static final HashMap<String, Collection<MPDAlbum>> YEAR_ALBUM_MAP = new HashMap<>();

    public static final String NULL_YEAR = "";

    public static void addYear(String year) {
        String addYear = year.equals(NULL_YEAR) ? "" : year;
        if (!getYears().contains(addYear)) {
            getYears().add(addYear);
        }
    }

    public static void addAlbum(String year, MPDAlbum album) {
        if (YEAR_ALBUM_MAP.get(year) == null) {
            YEAR_ALBUM_MAP.put(year, new ArrayList<MPDAlbum>());
        }

        if (!YEAR_ALBUM_MAP.get(year).contains(album)) {
            YEAR_ALBUM_MAP.get(year).add(album);
        }
    }

    public static List<String> getYears() {
        return years;
    }

    public static Collection<MPDAlbum> getAlbums(String year) {
        return YEAR_ALBUM_MAP.get(year);
    }
}
