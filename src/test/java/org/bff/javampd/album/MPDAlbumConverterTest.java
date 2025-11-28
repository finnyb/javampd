package org.bff.javampd.album;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

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
            "AlbumArtist: Faith No More",
            "Genre: Alternative Metal",
            "Date: 1989",
            "Artist: Faith No More",
            "Album: The Real Thing",
            "Date: 1997-06-03",
            "Artist: Faith No More",
            "Album: Album of the Year",
            "Date: 1999",
            "Artist: Faith No More",
            "Album: Angel Dust",
            "Album: King for a Day... Fool for a Lifetime");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    assertThat(albums.size(), is(4));
    for (MPDAlbum a : albums) {
      assertAll(
          () -> assertThat(a.getAlbumArtist(), is("Faith No More")),
          () -> assertThat(a.getArtistNames().size(), is(1)),
          () -> assertThat(a.getArtistNames().get(0), is("Faith No More")),
          () -> assertThat(a.getGenres().size(), is(1)),
          () -> assertThat(a.getGenres().get(0), is("Alternative Metal")),
          () -> assertThat(a.getDates().size(), is(1)));
    }
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

  @Test
  void multipleAlbums_FromSameGroupOf_AlbumArtist_Genre_Date_Artist() {
    var response =
        Arrays.asList(
            "AlbumArtist: Black Sabbath",
            "Genre: Metal",
            "Date: 1970",
            "Artist: Black Sabbath",
            "Album: Black Sabbath",
            "Album: Paranoid");

    var albums = new ArrayList<>(converter.convertResponseToAlbum(response));
    var a1 = albums.get(0);
    var a2 = albums.get(1);

    assertAll(
        () -> assertThat(a1.getAlbumArtist(), is("Black Sabbath")),
        () -> assertThat(a1.getArtistNames().get(0), is("Black Sabbath")),
        () -> assertThat(a1.getGenres().get(0), is("Metal")),
        () -> assertThat(a1.getDates().get(0), is("1970")),
        () -> assertThat(a1.getName(), is("Black Sabbath")),
        () -> assertThat(a2.getAlbumArtist(), is(a1.getAlbumArtist())),
        () -> assertThat(a2.getArtistNames(), is(a1.getArtistNames())),
        () -> assertThat(a2.getGenres(), is(a1.getGenres())),
        () -> assertThat(a2.getDates(), is(a1.getDates())),
        () -> assertThat(a2.getName(), is("Paranoid")));
  }
}
