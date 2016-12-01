package org.bff.javampd.playlist;

import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.song.MPDSong;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MPDSavedPlaylistTest {
    @Test
    public void testHashCode() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDSavedPlaylist playlist2 = new MPDSavedPlaylist("playlist1");

        assertEquals(playlist1.hashCode(), playlist2.hashCode());
    }

    @Test
    public void testDifferentHashCode() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDSavedPlaylist playlist2 = new MPDSavedPlaylist("playlist2");

        assertNotEquals(playlist1.hashCode(), playlist2.hashCode());
    }

    @Test
    public void testEqualsSameObject() throws Exception {
        MPDSavedPlaylist playlist = new MPDSavedPlaylist("playlist1");

        assertEquals(playlist, playlist);
    }

    @Test
    public void testEqualsSameName() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDSavedPlaylist playlist2 = new MPDSavedPlaylist("playlist1");

        assertEquals(playlist1, playlist2);
    }

    @Test
    public void testNotEqualsSameNameDifferentSongs() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDSavedPlaylist playlist2 = new MPDSavedPlaylist("playlist1");

        List<MPDSong> songs1 = new ArrayList<>();
        List<MPDSong> songs2 = new ArrayList<>();

        MPDSong mpdSong1 = new MPDSong("song1", "song1");
        MPDSong mpdSong2 = new MPDSong("song2", "song2");

        songs1.add(mpdSong1);
        songs2.add(mpdSong2);

        playlist1.setSongs(songs1);
        playlist2.setSongs(songs2);

        assertNotEquals(playlist1, playlist2);
    }

    @Test
    public void testNotEquals() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDSavedPlaylist playlist2 = new MPDSavedPlaylist("playlist2");

        assertNotEquals(playlist1, playlist2);
    }

    @Test
    public void testNotEqualsDifferentClasses() throws Exception {
        MPDSavedPlaylist playlist1 = new MPDSavedPlaylist("playlist1");
        MPDArtist playlist2 = new MPDArtist("playlist2");

        assertNotEquals(playlist1, playlist2);
    }
}