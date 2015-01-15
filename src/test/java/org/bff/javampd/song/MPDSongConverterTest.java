package org.bff.javampd.song;

import org.bff.javampd.SongTestHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class MPDSongConverterTest {

    private static final int COUNT = 3;

    private SongConverter converter;
    private List<MPDSong> songs;
    private SongTestHelper songTestHelper;

    @Before
    public void before() {
        songTestHelper = new SongTestHelper();
        converter = new MPDSongConverter();
        songs = converter.convertResponseToSong(songTestHelper.createResponses(COUNT));
    }

    @Test
    public void testArtist() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.ARTIST + i, songs.get(i).getArtistName());
        }
    }

    @Test
    public void testAlbum() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.ALBUM + i, songs.get(i).getAlbumName());
        }
    }

    @Test
    public void testTrack() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(i, songs.get(i).getTrack());
        }
    }

    @Test
    public void testTitle() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.TITLE + i, songs.get(i).getTitle());
        }
    }

    @Test
    public void testDate() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.DATE + i, songs.get(i).getYear());
        }
    }

    @Test
    public void testGenre() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.GENRE + i, songs.get(i).getGenre());
        }
    }

    @Test
    public void testComment() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.COMMENT + i, songs.get(i).getComment());
        }
    }

    @Test
    public void testTime() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(i, songs.get(i).getLength());
        }
    }

    @Test
    public void testPosition() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(i, songs.get(i).getPosition());
        }
    }

    @Test
    public void testId() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(i, songs.get(i).getId());
        }
    }

    @Test
    public void testDisc() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.DISC + i, songs.get(i).getDiscNumber());
        }
    }

    @Test
    public void testFile() {
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.FILE + i, songs.get(i).getFile());
        }
    }

    @Test
    public void testGetSongFileNameList() throws Exception {
        List<String> names = converter
                .getSongFileNameList(songTestHelper.createResponses(COUNT));

        assertEquals(COUNT, names.size());
        for (int i = 0; i < COUNT; i++) {
            assertEquals(songTestHelper.FILE + i, songs.get(i).getFile());
        }
    }
}