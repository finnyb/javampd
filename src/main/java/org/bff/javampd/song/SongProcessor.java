package org.bff.javampd.song;

import org.bff.javampd.processor.*;

import java.util.HashMap;
import java.util.Map;

public enum SongProcessor {
    FILE(new FileTagProcessor()),
    ARTIST(new ArtistTagProcessor()),
    ALBUM_ARTIST(new AlbumArtistTagProcessor()),
    ALBUM(new AlbumTagProcessor()),
    TRACK(new TrackTagProcessor()),
    TITLE(new TitleTagProcessor()),
    NAME(new NameTagProcessor()),
    DATE(new DateTagProcessor()),
    GENRE(new GenreTagProcessor()),
    COMMENT(new CommentTagProcessor()),
    TIME(new TimeTagProcessor()),
    POS(new PositionTagProcessor()),
    ID(new IdTagProcessor()),
    DISC(new DiscTagProcessor());

    private static final String TERMINATOR = "OK";
    private final transient ResponseProcessor responseProcessor;

    private static final Map<String, SongProcessor> lookup = new HashMap<>();

    static {
        for (SongProcessor s : SongProcessor.values()) {
            lookup.put(s.getProcessor().getPrefix().toLowerCase(), s);
        }
    }

    SongProcessor(ResponseProcessor responseProcessor) {
        this.responseProcessor = responseProcessor;
    }

    public static SongProcessor lookup(String line) {
        return lookup.get(line.substring(0, line.indexOf(":") + 1).toLowerCase());
    }

    public ResponseProcessor getProcessor() {
        return responseProcessor;
    }

    /**
     * Returns the line prefix that delimits songs in the response list
     *
     * @return the prefix that breaks songs in the list
     */
    public static String getDelimitingPrefix() {
        return FILE.getProcessor().getPrefix();
    }

    public static String getTermination() {
        return TERMINATOR;
    }
}
