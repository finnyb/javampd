package org.bff.javampd.song;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.bff.javampd.song.SongProcessor.ALBUM_ARTIST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SongProcessorTest {
    @Test
    @DisplayName("looks up processor for different cases")
    void lookupLine() {
        assertAll(
                () -> assertEquals(ALBUM_ARTIST, SongProcessor.lookup("albumartist: Tool")),
                () -> assertEquals(ALBUM_ARTIST, SongProcessor.lookup("ALBUMARTIST: Tool")),
                () -> assertEquals(ALBUM_ARTIST, SongProcessor.lookup("AlbumArtist: Tool"))
        );
    }
}
