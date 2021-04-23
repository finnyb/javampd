package org.bff.javampd.album;

import org.bff.javampd.processor.AlbumTagProcessor;
import org.bff.javampd.processor.ArtistTagProcessor;
import org.bff.javampd.processor.DateTagProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MPDAlbumConverterTest {

    private static final String ALBUM = "album";
    private static final String ARTIST = "artist";
    private static final int COUNT = 3;

    private AlbumConverter converter;
    private List<MPDAlbum> albums;

    @BeforeEach
    void before() {
        converter = new MPDAlbumConverter();
        albums = converter.convertResponseToAlbum(createResponses());
    }

    @Test
    void testArtist() {
        assertThat("", is(equalTo(albums.get(0).getArtistName())));
        for (int i = 1; i < COUNT + 1; i++) {
            assertThat(albums.get(i).getArtistName(), is(equalTo(ARTIST + (i - 1))));
        }
    }

    @Test
    void testAlbum() {
        assertThat(albums.get(0).getName(), is(equalTo(ALBUM)));
        for (int i = 1; i < COUNT + 1; i++) {
            assertThat(albums.get(i).getName(), is(equalTo(ALBUM + (i - 1))));
        }
    }

    @Test
    void testAlbumSingleTag() {
        List<String> albumResponse = new ArrayList<>();
        albumResponse.add("Album: " + ALBUM);
        albumResponse.add("Album: " + ALBUM + "1");
        albums = converter.convertResponseToAlbum(albumResponse);

        assertThat(2, is(equalTo(albums.size())));
        assertThat(albums.get(0).getName(), is(equalTo(ALBUM)));
        assertThat(albums.get(1).getName(), is(equalTo(ALBUM + "1")));
    }

    @Test
    void testAlbumSingleTagSingleAlbum() {
        List<String> albumResponse = new ArrayList<>();
        albumResponse.add("Album: " + ALBUM);
        albums = converter.convertResponseToAlbum(albumResponse);

        assertThat(1, is(equalTo(albums.size())));
        assertThat(albums.get(0).getName(), is(equalTo(ALBUM)));
    }

    @Test
    void testUnknownResponse() {
        List<String> response = createResponses();
        response.add("unknown: I dont know");

        assertDoesNotThrow(() -> this.converter.convertResponseToAlbum(response));
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
