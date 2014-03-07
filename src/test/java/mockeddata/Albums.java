/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mockeddata;

import org.bff.javampd.objects.MPDAlbum;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bfindeisen
 */
public class Albums {

    public static List<MPDAlbum> albums = new ArrayList<>();

    public static MPDAlbum addAlbum(String albumName) {
        MPDAlbum album = new MPDAlbum(albumName);
        if (!albums.contains(album)) {
            albums.add(album);
        }

        return album;
    }
}
