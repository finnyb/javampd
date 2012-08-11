/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.integrationdata;

import org.bff.javampd.objects.MPDAlbum;
import org.bff.javampd.objects.MPDArtist;

import java.util.Collection;
import java.util.HashMap;

/**
 * @author bfindeisen
 */
public class Artists {

    private static Collection<MPDArtist> testArtists;

    public static final HashMap<MPDArtist, Collection<MPDAlbum>> TEST_ARTIST_ALBUM_MAP =
            new HashMap<MPDArtist, Collection<MPDAlbum>>();

    /**
     * @return the testArtists
     */
    public static Collection<MPDArtist> getTestArtists() {
        return testArtists;
    }

    /**
     * @param aTestArtists the testArtists to set
     */
    public static void setTestArtists(Collection<MPDArtist> aTestArtists) {
        testArtists = aTestArtists;
    }
}
