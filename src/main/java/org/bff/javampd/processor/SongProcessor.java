package org.bff.javampd.processor;

public enum SongProcessor {
    FILE(new FileProcessor()),
    ARTIST(new ArtistProcessor()),
    ALBUM(new AlbumProcessor()),
    TRACK(new TrackProcessor()),
    TITLE(new TitleProcessor()),
    DATE(new DateProcessor()),
    GENRE(new GenreProcessor()),
    COMMENT(new CommentProcessor()),
    TIME(new TimeProcessor()),
    POS(new PositionProcessor()),
    ID(new IdProcessor()),
    DISC(new DiscProcessor());

    private SongResponseProcessor songResponseProcessor;

    SongProcessor(SongResponseProcessor songResponseProcessor) {
        this.songResponseProcessor = songResponseProcessor;
    }

    public SongResponseProcessor getPrefix() {
        return songResponseProcessor;
    }
}
