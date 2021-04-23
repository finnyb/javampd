package org.bff.javampd.song;

import org.bff.javampd.processor.*;

import java.util.HashMap;
import java.util.Map;

public enum SongProcessor {
    FILE(new FileTagProcessor()),
    ARTIST(new ArtistTagProcessor()),
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

    private final transient SongTagResponseProcessor songTagResponseProcessor;

    private static final Map<String, SongProcessor> lookup = new HashMap<>();

    static {
        for (SongProcessor s : SongProcessor.values()) {
            lookup.put(s.getProcessor().getPrefix(), s);
        }
    }

    SongProcessor(SongTagResponseProcessor songTagResponseProcessor) {
        this.songTagResponseProcessor = songTagResponseProcessor;
    }

    public static SongProcessor lookup(String line) {
        return lookup.get(line.substring(0, line.indexOf(":") + 1));
    }

    public SongTagResponseProcessor getProcessor() {
        return songTagResponseProcessor;
    }

    /**
     * Returns the line prefix that delimits songs in the response list
     *
     * @return the prefix that breaks songs in the list
     */
    public static String getDelimitingPrefix() {
        return FILE.getProcessor().getPrefix();
    }
}
