package org.bff.javampd.integrationdata;

import org.bff.javampd.album.MPDAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class TestAlbums {

    private static List<MPDAlbum> albums = new ArrayList<>();
    public static final String NULL_ALBUM = "";

    public static MPDAlbum addAlbum(String albumName,
                                    String artistName,
                                    String date) {
        MPDAlbum album = new MPDAlbum(albumName);
        album.setArtistName(artistName);
        album.setDate(date);

        if (!getAlbums().contains(album)) {
            getAlbums().add(album);
        }

        return album;
    }

    public static List<MPDAlbum> getAlbums() {
        return albums;
    }
}
