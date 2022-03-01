package org.bff.javampd.album;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.bff.javampd.album.AlbumProcessor.ALBUM_ARTIST;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AlbumProcessorTest {

    @Test
    @DisplayName("looks up processor for different cases")
    void lookupLine() {
        assertAll(
                () -> assertEquals(ALBUM_ARTIST, AlbumProcessor.lookup("albumartist: Tool")),
                () -> assertEquals(ALBUM_ARTIST, AlbumProcessor.lookup("ALBUMARTIST: Tool")),
                () -> assertEquals(ALBUM_ARTIST, AlbumProcessor.lookup("AlbumArtist: Tool"))
        );
    }
}
