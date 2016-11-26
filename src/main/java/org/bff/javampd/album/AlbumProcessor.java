package org.bff.javampd.album;

import org.bff.javampd.processor.AlbumTagProcessor;
import org.bff.javampd.processor.AlbumTagResponseProcessor;
import org.bff.javampd.processor.ArtistTagProcessor;
import org.bff.javampd.processor.DateTagProcessor;

public enum AlbumProcessor {
    ARTIST(new ArtistTagProcessor()),
    DATE(new DateTagProcessor()),
    ALBUM(new AlbumTagProcessor());

    private final transient AlbumTagResponseProcessor albumTagResponseProcessor;

    AlbumProcessor(AlbumTagResponseProcessor albumTagResponseProcessor) {
        this.albumTagResponseProcessor = albumTagResponseProcessor;
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
