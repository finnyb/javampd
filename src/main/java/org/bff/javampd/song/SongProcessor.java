package org.bff.javampd.song;

import org.bff.javampd.processor.*;

public enum SongProcessor {
    FILE(new FileTagProcessor()),
    ARTIST(new ArtistTagProcessor()),
    ALBUM(new AlbumTagProcessor()),
    TRACK(new TrackTagProcessor()),
    TITLE(new TitleTagProcessor()),
    DATE(new DateTagProcessor()),
    GENRE(new GenreTagProcessor()),
    COMMENT(new CommentTagProcessor()),
    TIME(new TimeTagProcessor()),
    POS(new PositionTagProcessor()),
    ID(new IdTagProcessor()),
    DISC(new DiscTagProcessor());

    private final transient SongTagResponseProcessor songTagResponseProcessor;

    SongProcessor(SongTagResponseProcessor songTagResponseProcessor) {
        this.songTagResponseProcessor = songTagResponseProcessor;
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
