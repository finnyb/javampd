package org.bff.javampd;

import org.bff.javampd.processor.*;
import org.bff.javampd.song.MPDSong;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * @author bill
 * @since: 1/15/15 6:52 AM
 */
public class SongTestHelper {

    public static final String FILE = "file";
    public static final String ARTIST = "artist";
    public static final String ALBUM = "album";
    public static final String TITLE = "title";
    public static final String DATE = "199";
    public static final String GENRE = "file";
    public static final String COMMENT = "comment";
    public static final String DISC = "disc";

    public List<String> createResponseWithId(int id) {
        List<String> response = createResponses(1);
        response.add(new IdProcessor().getPrefix() + id);

        return response;
    }

    public List<String> createResponse() {
        return createResponses(1);
    }

    public List<String> createResponses(int count) {
        List<String> response = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            response.add(new FileProcessor().getPrefix() + FILE + i);
            response.add(new ArtistProcessor().getPrefix() + ARTIST + i);
            response.add(new AlbumProcessor().getPrefix() + ALBUM + i);
            response.add(new TrackProcessor().getPrefix() + i);
            response.add(new TitleProcessor().getPrefix() + TITLE + i);
            response.add(new DateProcessor().getPrefix() + DATE + i);
            response.add(new GenreProcessor().getPrefix() + GENRE + i);
            response.add(new CommentProcessor().getPrefix() + COMMENT + i);
            response.add(new TimeProcessor().getPrefix() + i);
            response.add(new PositionProcessor().getPrefix() + i);
            response.add(new IdProcessor().getPrefix() + i);
            response.add(new DiscProcessor().getPrefix() + DISC + i);
        }
        return response;
    }

    public void assertEquality(MPDSong song) {
        assertEqualities(song, 0);
    }

    public void assertEqualities(MPDSong song, int index) {
        assertEquals(index, song.getId());
        assertEquals(ARTIST + index, song.getArtistName());
        assertEquals(ARTIST + index, song.getArtistName());
        assertEquals(ALBUM + index, song.getAlbumName());
        assertEquals(index, song.getTrack());
        assertEquals(TITLE + index, song.getTitle());
        assertEquals(DATE + index, song.getYear());
        assertEquals(GENRE + index, song.getGenre());
        assertEquals(COMMENT + index, song.getComment());
        assertEquals(index, song.getLength());
        assertEquals(index, song.getPosition());
        assertEquals(index, song.getId());
        assertEquals(DISC + index, song.getDiscNumber());
        assertEquals(FILE + index, song.getFile());
    }
}
