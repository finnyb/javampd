package org.bff.javampd.album;

import org.bff.javampd.processor.AlbumTagProcessor;
import org.bff.javampd.processor.ArtistTagProcessor;
import org.bff.javampd.processor.DateTagProcessor;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MPDAlbumConverterTest {

    private static final String ALBUM = "album";
    private static final String ARTIST = "artist";
    private static final int COUNT = 3;

    private AlbumConverter converter;
    private List<MPDAlbum> albums;

    @Before
    public void before() {
        converter = new MPDAlbumConverter();
        albums = converter.convertResponseToAlbum(createResponses());
    }

    @Test
    public void testArtist() {
        assertEquals("", albums.get(0).getArtistName());
        for (int i = 1; i < COUNT + 1; i++) {
            assertEquals(albums.get(i).getArtistName(), ARTIST + (i - 1));
        }
    }

    @Test
    public void testAlbum() {
        assertEquals(albums.get(0).getName(), ALBUM);
        for (int i = 1; i < COUNT + 1; i++) {
            assertEquals(albums.get(i).getName(), ALBUM + (i - 1));
        }
    }

    @Test
    public void testAlbumSingleTag() {
        List<String> albumResponse = new ArrayList<>();
        albumResponse.add("Album: " + ALBUM);
        albumResponse.add("Album: " + ALBUM + "1");
        albums = converter.convertResponseToAlbum(albumResponse);

        assertEquals(2, albums.size());
        assertEquals(albums.get(0).getName(), ALBUM);
        assertEquals(albums.get(1).getName(), ALBUM + "1");
    }

    @Test
    public void testAlbumSingleTagSingleAlbum() {
        List<String> albumResponse = new ArrayList<>();
        albumResponse.add("Album: " + ALBUM);
        albums = converter.convertResponseToAlbum(albumResponse);

        assertEquals(1, albums.size());
        assertEquals(albums.get(0).getName(), ALBUM);
    }

    private List<String> createResponses() {
        List<String> response = new ArrayList<>();

        response.add(new AlbumTagProcessor().getPrefix() + ALBUM);
        for (int i = 0; i < COUNT; i++) {
            response.add(new AlbumTagProcessor().getPrefix() + ALBUM + i);
            response.add(new ArtistTagProcessor().getPrefix() + ARTIST + i);
            response.add(new DateTagProcessor().getPrefix() + 1990 + i);
        }
        return response;
    }
}