/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.data;

import org.bff.javampd.objects.MPDArtist;
import org.bff.javampd.objects.MPDSong;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

/**
 * Song ids must be filled since not reliable
 *
 * @author bill
 */
public class Songs {

    public static final HashMap<MPDArtist, Collection<MPDSong>> SONG_ARTIST_MAP =
            new HashMap<MPDArtist, Collection<MPDSong>>();
    private static List<MPDSong> testSongs;
    public static final HashMap<String, MPDSong> databaseSongMap =
            new HashMap<String, MPDSong>();

    /**
     * @return the testSongs
     */
    public static List<MPDSong> getTestSongs() {
        return testSongs;
    }

    /**
     * @param aTestSongs the testSongs to set
     */
    public static void setTestSongs(List<MPDSong> aTestSongs) {
        testSongs = aTestSongs;
    }

}
