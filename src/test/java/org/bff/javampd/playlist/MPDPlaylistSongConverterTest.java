package org.bff.javampd.playlist;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MPDPlaylistSongConverterTest {
    private PlaylistSongConverter playlistSongConverter;

    @BeforeEach
    void setUp() {
        playlistSongConverter = new MPDPlaylistSongConverter();
    }

    @Test
    void defaults() {
        var s = playlistSongConverter.convertResponseToSongs(List.of("file: Tool/10,000 Days/01 Vicarious.flac")).get(0);
        assertAll(
                () -> assertEquals(-1, s.getId()),
                () -> assertEquals(-1, s.getPosition()),
                () -> assertNull(s.getArtistName()),
                () -> assertNull(s.getAlbumArtist()),
                () -> assertNull(s.getAlbumName()),
                () -> assertNull(s.getTrack()),
                () -> assertNull(s.getName()),
                () -> assertNull(s.getTitle()),
                () -> assertNull(s.getDate()),
                () -> assertNull(s.getGenre()),
                () -> assertNull(s.getComment()),
                () -> assertEquals(-1, s.getLength()),
                () -> assertNull(s.getDiscNumber()),
                () -> assertEquals("Tool/10,000 Days/01 Vicarious.flac", s.getFile())
        );
    }

    @Test
    void multiple() {
        var s = playlistSongConverter.convertResponseToSongs(multipleResponse());
        assertEquals(2, s.size());
    }

    @Test
    void badId() {
        var s = playlistSongConverter.convertResponseToSongs(Arrays.asList(
                "file: Tool/10,000 Days/01 Vicarious.flac",
                "id: unparseable"
        )).get(0);
        assertEquals(-1, s.getId());
    }

    @Test
    void badPosition() {
        var s = playlistSongConverter.convertResponseToSongs(Arrays.asList(
                "file: Tool/10,000 Days/01 Vicarious.flac",
                "id: unparseable"
        )).get(0);

        assertEquals(-1, s.getPosition());
    }

    private List<String> multipleResponse() {
        return Arrays.asList(
                "file: Tool/Undertow/03-Sober.flac",
                "Last-Modified: 2022-02-19T12:52:00Z",
                "MUSICBRAINZ_RELEASETRACKID: a519bd9b-1301-3d15-acbe-34ef764c5de3",
                "MUSICBRAINZ_WORKID: e8b1d47b-2657-3e97-a706-cac4e7d2e3cb",
                "Album: Undertow",
                "AlbumArtist: Tool",
                "AlbumArtistSort: Tool",
                "Artist: Tool",
                "ArtistSort: Tool",
                "Disc: 1",
                "Disc: 1",
                "Genre: Hard Rock",
                "Label: Volcano Records",
                "MUSICBRAINZ_ALBUMARTISTID: 66fc5bf8-daa4-4241-b378-9bc9077939d2",
                "MUSICBRAINZ_ALBUMID: e4a9bee3-fa02-3dc3-85f6-8042062d9a5f",
                "MUSICBRAINZ_ARTISTID: 66fc5bf8-daa4-4241-b378-9bc9077939d2",
                "MUSICBRAINZ_TRACKID: 54db4149-862c-49c9-9224-be1314d57780",
                "OriginalDate: 1993-04-06",
                "Title: Sober",
                "Date: 1993",
                "Track: 3",
                "Time: 307",
                "duration: 306.733",
                "Pos: 0",
                "Id: 67",
                "file: Spiritbox/Eternal Blue/11-Circle With Me.mp3",
                "Last-Modified: 2022-02-19T12:51:00Z",
                "Artist: Spiritbox",
                "AlbumArtist: Spiritbox",
                "ArtistSort: Spiritbox",
                "AlbumArtistSort: Spiritbox",
                "AlbumArtistSort: Spiritbox",
                "Title: Circle With Me",
                "Album: Eternal Blue",
                "Track: 11",
                "Date: 2021",
                "OriginalDate: 2021-09-17",
                "Genre: Metal",
                "Composer: Daniel Braunstein, Bill Crook, Michael Stringer",
                "Disc: 1",
                "Label: Rise Records",
                "MUSICBRAINZ_ALBUMID: 312248cc-12a5-46f4-8f9c-893df55deccf",
                "MUSICBRAINZ_ARTISTID: 9c935736-7530-41e4-b776-1dbcf534c061",
                "MUSICBRAINZ_ALBUMARTISTID: 9c935736-7530-41e4-b776-1dbcf534c061",
                "MUSICBRAINZ_RELEASETRACKID: a04d27df-9213-4c53-a481-3f4eba397b93",
                "MUSICBRAINZ_TRACKID: 5d74244a-d193-44e6-a78c-28446abf2904",
                "Time: 234",
                "duration: 233.926",
                "Pos: 1",
                "Id: 68",
                "OK"
        );
    }
}
