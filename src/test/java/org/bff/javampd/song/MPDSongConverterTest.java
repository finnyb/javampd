package org.bff.javampd.song;

import org.bff.javampd.processor.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MPDSongConverterTest {

    private static final String FILE = "file";
    private static final String ARTIST = "artist";
    private static final String ALBUM = "album";
    private static final String TITLE = "title";
    private static final String DATE = "199";
    private static final String GENRE = "file";
    private static final String COMMENT = "comment";
    private static final String DISC = "disc";
    private static final int COUNT = 3;

    private SongConverter converter;
    private List<MPDSong> songs;

    @Before
    public void before() {
        converter = new MPDSongConverter();
        songs = converter.convertResponseToSong(createResponses());
    }

    @Test
    public void testArtist() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getArtistName(), ARTIST + i);
        }
    }

    @Test
    public void testAlbum() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getAlbumName(), ALBUM + i);
        }
    }

    @Test
    public void testTrack() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getTrack(), i);
        }
    }

    @Test
    public void testTitle() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getTitle(), TITLE + i);
        }
    }

    @Test
    public void testDate() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getYear(), DATE + i);
        }
    }

    @Test
    public void testGenre() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getGenre(), GENRE + i);
        }
    }

    @Test
    public void testComment() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getComment(), COMMENT + i);
        }
    }

    @Test
    public void testTime() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getLength(), i);
        }
    }

    @Test
    public void testPosition() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getPosition(), i);
        }
    }

    @Test
    public void testId() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getId(), i);
        }
    }

    @Test
    public void testDisc() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getDiscNumber(), DISC + i);
        }
    }

    @Test
    public void testFile() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getFile(), FILE + i);
        }
    }

    @Test
    public void testGetSongFileNameList() throws Exception {
        List<String> names = converter.getSongFileNameList(createResponses());
        assertEquals(COUNT, names.size());
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getFile(), FILE + i);
        }
    }

    private List<String> createResponses() {
        List<String> response = new ArrayList<>();

        for (int i = 0; i < COUNT; i++) {
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
}