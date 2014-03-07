/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mockeddata;

import org.bff.javampd.objects.MPDAlbum;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bill
 */
public class Years {

    public static List<String> years = new ArrayList<>();
    public static final HashMap<String, Collection<MPDAlbum>> YEAR_ALBUM_MAP = new HashMap<>();

    public static final String NULL_YEAR = "No Year";

    public static void addYear(String year) {
        String addYear = year.equals(NULL_YEAR) ? "" : year;
        if (!years.contains(addYear)) {
            years.add(addYear);
        }
    }

    public static void addYear(String year, MPDAlbum album) {
        if (YEAR_ALBUM_MAP.get(year) == null) {
            YEAR_ALBUM_MAP.put(year, new ArrayList<MPDAlbum>());
        }

        if (!YEAR_ALBUM_MAP.get(year).contains(album)) {
            YEAR_ALBUM_MAP.get(year).add(album);
        }
    }
}
