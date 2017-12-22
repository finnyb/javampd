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
    private static final String NAME = "name";
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
        songs = converter.convertResponseToSong(createResponses(true));
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
    public void testName() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getName(), NAME + i);
        }
    }

    @Test
    public void testNameWithoutResponse() {
        songs = converter.convertResponseToSong(createResponses(false));
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getName(), TITLE + i);
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
        List<String> names = converter.getSongFileNameList(createResponses(true));
        assertEquals(COUNT, names.size());
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songs.get(i).getFile(), FILE + i);
        }
    }

    private List<String> createResponses(boolean includeName) {
        List<String> response = new ArrayList<>();

        for (int i = 0; i < COUNT; i++) {
            response.add(new FileTagProcessor().getPrefix() + FILE + i);
            response.add(new ArtistTagProcessor().getPrefix() + ARTIST + i);
            response.add(new AlbumTagProcessor().getPrefix() + ALBUM + i);
            response.add(new TrackTagProcessor().getPrefix() + i);
            if (includeName) {
                response.add(new NameTagProcessor().getPrefix() + NAME + i);
            }
            response.add(new TitleTagProcessor().getPrefix() + TITLE + i);
            response.add(new DateTagProcessor().getPrefix() + DATE + i);
            response.add(new GenreTagProcessor().getPrefix() + GENRE + i);
            response.add(new CommentTagProcessor().getPrefix() + COMMENT + i);
            response.add(new TimeTagProcessor().getPrefix() + i);
            response.add(new PositionTagProcessor().getPrefix() + i);
            response.add(new IdTagProcessor().getPrefix() + i);
            response.add(new DiscTagProcessor().getPrefix() + DISC + i);
        }
        return response;
    }
}