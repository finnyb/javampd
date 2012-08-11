/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.integrationdata;

import org.bff.javampd.objects.MPDAlbum;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * @author bfindeisen
 */
public class Genres {
    private static List<String> testGenres;

    public static final HashMap<String, Collection<MPDAlbum>> GENRE_ALBUM_MAP = new HashMap<String, Collection<MPDAlbum>>();

    /**
     * @return the testGenres
     */
    public static List<String> getTestGenres() {
        return testGenres;
    }

    /**
     * @param aTestGenres the testGenres to set
     */
    public static void setTestGenres(List<String> aTestGenres) {
        testGenres = aTestGenres;
    }
}
