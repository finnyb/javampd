package org.bff.javampd.album;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Converts a response from the server to an {@link MPDAlbum}
 *
 * @author bill
 */
@Slf4j
public class MPDAlbumConverter implements AlbumConverter {

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
        var album = MPDAlbum.builder(name).build();

        String line = null;
        if (iterator.hasNext()) {
            line = iterator.next();
            while (!line.startsWith(delimitingPrefix)) {
                processLine(album, line);
                if (!iterator.hasNext()) {
                    break;
                }
                line = iterator.next();
            }
        }

        albums.add(album);

        return line;
    }

    public void processLine(MPDAlbum album, String line) {
        var albumProcessor = AlbumProcessor.lookup(line);
        if (albumProcessor != null) {
            albumProcessor.getProcessor().processTag(album, line);
        } else {
            log.warn("Processor not found - {}", line);
        }
    }
}
