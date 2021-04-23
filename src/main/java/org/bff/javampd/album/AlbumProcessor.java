package org.bff.javampd.album;

import org.bff.javampd.processor.*;

import java.util.HashMap;
import java.util.Map;

public enum AlbumProcessor {
    ARTIST(new ArtistTagProcessor()),
    DATE(new DateTagProcessor()),
    ALBUM(new AlbumTagProcessor()),
    GENRE(new GenreTagProcessor());

    private final transient AlbumTagResponseProcessor albumTagResponseProcessor;
    private static final Map<String, AlbumProcessor> lookup = new HashMap<>();

    static {
        for (AlbumProcessor a : AlbumProcessor.values()) {
            lookup.put(a.getProcessor().getPrefix(), a);
        }
    }

    AlbumProcessor(AlbumTagResponseProcessor albumTagResponseProcessor) {
        this.albumTagResponseProcessor = albumTagResponseProcessor;
    }

    public static AlbumProcessor lookup(String line) {
        return lookup.get(line.substring(0, line.indexOf(":") + 1));
    }

    public AlbumTagResponseProcessor getProcessor() {
        return this.albumTagResponseProcessor;
    }

    /**
     * Returns the line prefix that delimits songs in the response list
     *
     * @return the prefix that breaks songs in the list
     */
    public static String getDelimitingPrefix() {
        return ALBUM.getProcessor().getPrefix();
    }
}
