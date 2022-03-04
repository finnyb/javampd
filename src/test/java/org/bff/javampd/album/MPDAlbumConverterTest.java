package org.bff.javampd.album;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MPDAlbumConverterTest {

  private AlbumConverter converter;

  @BeforeEach
  void before() {
    converter = new MPDAlbumConverter();
  }

  @Test
  @DisplayName("list of one album with single tags")
  void simpleAlbum() {

    var response =
        Arrays.asList(
            "AlbumArtist: Greta Van Fleet",
            "Genre: Rock",
            "Date: 2018",
            "Artist: Greta Van Fleet",
            "Album: Anthem of the Peaceful Army");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    var a = albums.get(0);

    assertAll(
        () -> assertThat(a.getName(), is(equalTo("Anthem of the Peaceful Army"))),
        () -> assertThat(albums.size(), is(equalTo(1))),
        () -> assertThat(a.getAlbumArtist(), is(equalTo("Greta Van Fleet"))),
        () -> assertThat(a.getArtistNames().size(), is(equalTo(1))),
        () -> assertThat(a.getArtistNames().get(0), is(equalTo("Greta Van Fleet"))),
        () -> assertThat(a.getGenres().size(), is(equalTo(1))),
        () -> assertThat(a.getGenres().get(0), is(equalTo("Rock"))),
        () -> assertThat(a.getDates().size(), is(equalTo(1))),
        () -> assertThat(a.getDates().get(0), is(equalTo("2018"))));
  }

  @Test
  @DisplayName("list of one album with single tags")
  void unknownAttribute() {

    var response =
        Arrays.asList(
            "AlbumArtist: Greta Van Fleet",
            "Genre: Rock",
            "Date: 2018",
            "Artist: Greta Van Fleet",
            "Unknown: something unexpected",
            "Album: Anthem of the Peaceful Army");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    var a = albums.get(0);

    assertAll(
        () -> assertThat(a.getName(), is(equalTo("Anthem of the Peaceful Army"))),
        () -> assertThat(albums.size(), is(equalTo(1))),
        () -> assertThat(a.getAlbumArtist(), is(equalTo("Greta Van Fleet"))),
        () -> assertThat(a.getArtistNames().size(), is(equalTo(1))),
        () -> assertThat(a.getArtistNames().get(0), is(equalTo("Greta Van Fleet"))),
        () -> assertThat(a.getGenres().size(), is(equalTo(1))),
        () -> assertThat(a.getGenres().get(0), is(equalTo("Rock"))),
        () -> assertThat(a.getDates().size(), is(equalTo(1))),
        () -> assertThat(a.getDates().get(0), is(equalTo("2018"))));
  }

  @Test
  void clearAttributes() {
    var response =
        Arrays.asList(
            "AlbumArtist: Spiritbox",
            "Genre: Metal",
            "Date: 2021",
            "Artist: Spiritbox",
            "Album: Eternal Blue",
            "Artist: Tool",
            "Album: Lateralus");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    var a = albums.get(1);

    assertAll(
        () -> assertThat(a.getName(), is(equalTo("Lateralus"))),
        () -> assertNull(a.getAlbumArtist()),
        () -> assertThat(a.getArtistNames().size(), is(equalTo(1))),
        () -> assertThat(a.getArtistNames().get(0), is(equalTo("Tool"))),
        () -> assertThat(a.getGenres().size(), is(equalTo(0))),
        () -> assertThat(a.getDates().size(), is(equalTo(0))));
  }

  @Test
  void multipleArtists() {
    var response =
        Arrays.asList(
            "AlbumArtist: Spiritbox",
            "Genre: Metal",
            "Date: 2021",
            "Artist: Spiritbox",
            "Album: Eternal Blue",
            "Artist: Spiritbox feat. Sam Carter",
            "Album: Eternal Blue");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    var a = albums.get(0);

    assertAll(
        () -> assertThat(albums.size(), is(equalTo(1))),
        () -> assertThat(a.getName(), is(equalTo("Eternal Blue"))),
        () -> assertThat(a.getAlbumArtist(), is(equalTo("Spiritbox"))),
        () -> assertThat(a.getArtistNames().size(), is(equalTo(2))),
        () -> assertThat(a.getArtistNames().get(0), is(equalTo("Spiritbox"))),
        () -> assertThat(a.getArtistNames().get(1), is(equalTo("Spiritbox feat. Sam Carter"))),
        () -> assertThat(a.getGenres().size(), is(equalTo(1))),
        () -> assertThat(a.getGenres().get(0), is(equalTo("Metal"))),
        () -> assertThat(a.getDates().size(), is(equalTo(1))),
        () -> assertThat(a.getDates().get(0), is(equalTo("2021"))));
  }
}
