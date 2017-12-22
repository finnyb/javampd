package org.bff.javampd.album;

import org.bff.javampd.MPDItem;
import org.bff.javampd.artist.MPDConverter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Converts a response from the server to an {@link MPDAlbum}
 *
 * @author bill
 */
public class MPDAlbumConverter extends MPDConverter implements AlbumConverter {

    private static String delimitingPrefix = AlbumProcessor.getDelimitingPrefix();

    @Override
    public List<MPDAlbum> convertResponseToAlbum(List<String> list) {
        List<MPDAlbum> albumList = new ArrayList<>();
        Iterator<String> iterator = list.iterator();

        String line = null;
        while (iterator.hasNext()) {
            if (line == null || (!line.startsWith(delimitingPrefix))) {
                line = iterator.next();
            }

            while (line != null && line.startsWith(delimitingPrefix)) {
                line = processAlbum(line.substring(delimitingPrefix.length()).trim(), iterator, albumList);
            }
        }
        return albumList;
    }

    private String processAlbum(String name, Iterator<String> iterator, List<MPDAlbum> albums) {
        MPDAlbum album = new MPDAlbum(name);
        String line = processItem(album, iterator, delimitingPrefix);
        albums.add(album);

        return line;
    }

    @Override
    public void processLine(MPDItem item, String line) {
        for (AlbumProcessor albumProcessor : AlbumProcessor.values()) {
            albumProcessor.getProcessor().processTag((MPDAlbum) item, line);
        }
    }
}
