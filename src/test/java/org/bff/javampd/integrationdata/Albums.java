/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.integrationdata;

import org.bff.javampd.objects.MPDAlbum;

import java.util.List;

/**
 * @author bfindeisen
 */
public class Albums {

    private static List<MPDAlbum> testAlbums;

    /**
     * @return the testAlbums
     */
    public static List<MPDAlbum> getTestAlbums() {
        return testAlbums;
    }

    /**
     * @param aTestAlbums the testAlbums to set
     */
    public static void setTestAlbums(List<MPDAlbum> aTestAlbums) {
        testAlbums = aTestAlbums;
    }
}
