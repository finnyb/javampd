package org.bff.javampd.playlist;

import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.song.MPDSong;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MPDSavedPlaylistTest {
    @Test
    void testHashCode() {
        assertEquals(
                MPDSavedPlaylist.builder("playlist1").build().hashCode(),
                MPDSavedPlaylist.builder("playlist1").build().hashCode());
    }

    @Test
    void testDifferentHashCode() {
        assertNotEquals(MPDSavedPlaylist.builder("playlist1").build().hashCode(),
                MPDSavedPlaylist.builder("playlist2").build().hashCode());
    }

    @Test
    void testEqualsSameObject() {
        MPDSavedPlaylist playlist = MPDSavedPlaylist.builder("playlist1").build();
        assertEquals(playlist, playlist);
    }

    @Test
    void testEqualsSameName() {
        assertEquals(
                MPDSavedPlaylist.builder("playlist1").build(),
                MPDSavedPlaylist.builder("playlist1").build()
        );
    }

    @Test
    void testNotEqualsSameNameDifferentSongs() {
        var songs1 = new ArrayList<MPDSong>();
        var songs2 = new ArrayList<MPDSong>();

        MPDSong mpdSong1 = MPDSong.builder().file("file1").title("song1").build();
        MPDSong mpdSong2 = MPDSong.builder().file("file2").title("song2").build();

        songs1.add(mpdSong1);
        songs2.add(mpdSong2);

        assertNotEquals(
                MPDSavedPlaylist.builder("playlist1").songs(songs1).build(),
                MPDSavedPlaylist.builder("playlist1").songs(songs2).build());
    }

    @Test
    void testNotEquals() {
        assertNotEquals(MPDSavedPlaylist.builder("playlist1").build(),
                MPDSavedPlaylist.builder("playlist2").build()
        );
    }

    @Test
    void testNotEqualsDifferentClasses() {
        assertNotEquals(MPDSavedPlaylist.builder("").build(),
                new MPDArtist(""));
    }
}
