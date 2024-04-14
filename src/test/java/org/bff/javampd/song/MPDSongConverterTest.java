package org.bff.javampd.song;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MPDSongConverterTest {

  private SongConverter converter;

  @BeforeEach
  void before() {
    converter = new MPDSongConverter();
  }

  @Test
  void defaults() {
    var s =
        converter
            .convertResponseToSongs(List.of("file: Tool/10,000 Days/01 Vicarious.flac"))
            .getFirst();
    assertAll(
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
        () -> assertEquals("Tool/10,000 Days/01 Vicarious.flac", s.getFile()));
  }

  @Test
  void terminationNull() {
    assertEquals(
        "Tool/10,000 Days/01 Vicarious.flac",
        converter
            .convertResponseToSongs(List.of("file: Tool/10,000 Days/01 Vicarious.flac"))
            .getFirst()
            .getFile());
  }

  @Test
  void single() {
    var s = converter.convertResponseToSongs(createSingleResponse()).getFirst();
    assertAll(
        () -> assertEquals("Tool", s.getArtistName()),
        () -> assertEquals("Tool", s.getAlbumArtist()),
        () -> assertEquals("10,000 Days", s.getAlbumName()),
        () -> assertEquals("1", s.getTrack()),
        () -> assertEquals("Vicarious", s.getName()),
        () -> assertEquals("Vicarious", s.getTitle()),
        () -> assertEquals("2006-04-28", s.getDate()),
        () -> assertEquals("Hard Rock", s.getGenre()),
        () -> assertEquals("JavaMPD comment", s.getComment()),
        () -> assertEquals(427, s.getLength()),
        () -> assertEquals("1", s.getDiscNumber()),
        () -> assertEquals("Tool/10,000 Days/01 Vicarious.flac", s.getFile()));
  }

  @Test
  void clearAttributes() {
    var songs =
        converter.convertResponseToSongs(
            Arrays.asList(
                "file: Tool/10,000 Days/01 Vicarious.flac",
                "Album: 10,000 Days",
                "AlbumArtist: Tool",
                "Genre: Hard Rock",
                "Date: 2006",
                "Track: 1",
                """
                file: Greta Van Fleet/Anthem of the Peaceful Army/03-When the\
                 Curtain Falls.flac\
                """,
                "Artist: Greta Van Fleet",
                "Album: Anthem of the Peaceful Army",
                "Track: 3"));
    var s = songs.get(1);
    assertAll(
        () -> assertNull(s.getDate()),
        () -> assertNull(s.getGenre()),
        () -> assertNull(s.getTagMap().get("Genre")));
  }

  @Test
  void multipleFirst() {
    var s = new ArrayList<>(converter.convertResponseToSongs(createMultipleResponse()));
    var s1 = s.getFirst();

    assertAll(
        () -> assertEquals("Breaking Benjamin", s1.getArtistName()),
        () -> assertEquals("Breaking Benjamin", s1.getAlbumArtist()),
        () -> assertEquals("Ember", s1.getAlbumName()),
        () -> assertEquals("3", s1.getTrack()),
        () -> assertEquals("Red Cold River", s1.getName()),
        () -> assertEquals("Red Cold River", s1.getTitle()),
        () -> assertEquals("2018", s1.getDate()),
        () -> assertEquals("Alternative Metal", s1.getGenre()),
        () -> assertEquals(201, s1.getLength()),
        () -> assertEquals("1", s1.getDiscNumber()),
        () -> assertEquals("Breaking Benjamin/Ember/03. Red Cold River.flac", s1.getFile()));
  }

  @Test
  void multipleMiddle() {
    var s = new ArrayList<>(converter.convertResponseToSongs(createMultipleResponse()));
    var s2 = s.get(1);

    assertAll(
        () -> assertEquals("Breaking Benjamin", s2.getArtistName()),
        () -> assertNull(s2.getAlbumArtist()),
        () -> assertEquals("We Are Not Alone", s2.getAlbumName()),
        () -> assertEquals("1", s2.getTrack()),
        () -> assertEquals("So Cold", s2.getName()),
        () -> assertEquals("So Cold", s2.getTitle()),
        () -> assertEquals("2004", s2.getDate()),
        () -> assertNull(s2.getGenre()),
        () -> assertEquals(273, s2.getLength()),
        () -> assertNull(s2.getDiscNumber()),
        () -> assertEquals("Breaking Benjamin/We Are Not Alone/01-So Cold.flac", s2.getFile()));
  }

  @Test
  void multipleLast() {
    var s = new ArrayList<>(converter.convertResponseToSongs(createMultipleResponse()));
    var s3 = s.get(2);

    assertAll(
        () -> assertEquals("Greta Van Fleet", s3.getArtistName()),
        () -> assertEquals("Greta Van Fleet", s3.getAlbumArtist()),
        () -> assertEquals("Anthem of the Peaceful Army", s3.getAlbumName()),
        () -> assertEquals("2", s3.getTrack()),
        () -> assertEquals("The Cold Wind", s3.getName()),
        () -> assertEquals("The Cold Wind", s3.getTitle()),
        () -> assertEquals("2018", s3.getDate()),
        () -> assertEquals("Rock", s3.getGenre()),
        () -> assertEquals(197, s3.getLength()),
        () -> assertEquals("1", s3.getDiscNumber()),
        () ->
            assertEquals(
                "Greta Van Fleet/Anthem of the Peaceful Army/02 The Cold Wind.flac", s3.getFile()));
  }

  @Test
  void tagMap() {
    var s = converter.convertResponseToSongs(createSingleResponse()).getFirst();
    var m = s.getTagMap();

    assertAll(
        () -> assertEquals(24, m.size()),
        () -> assertEquals("Tool", m.get("AlbumArtist").getFirst()));
  }

  @Test
  @DisplayName("multiple instances of the same tag")
  void tagMapMultipleTags() {
    var s = converter.convertResponseToSongs(createSingleResponse()).getFirst();
    var p = s.getTagMap().get("Performer");

    assertAll(
        () -> assertEquals(4, p.size()),
        () -> assertEquals("Danny Carey (membranophone)", p.getFirst()),
        () -> assertEquals("Justin Chancellor (bass guitar)", p.get(1)),
        () -> assertEquals("Adam Jones (guitar)", p.get(2)),
        () -> assertEquals("Maynard James Keenan (lead vocals)", p.get(3)));
  }

  @Test
  void tagMapNotTagLine() {
    var songs =
        converter.convertResponseToSongs(
            Arrays.asList("file: Tool/10,000 Days/01 Vicarious.flac", "NotATag"));
    var s = songs.getFirst();
    assertEquals(0, s.getTagMap().size());
  }

  @Test
  void fileNameList() {
    var files =
        this.converter.getSongFileNameList(
            Arrays.asList(
                "file: Tool/10,000 Days/01 Vicarious.flac",
                """
                File: Greta Van Fleet/Anthem of the Peaceful Army/02 The Cold\
                 Wind.flac\
                """));

    assertAll(
        () -> assertEquals("Tool/10,000 Days/01 Vicarious.flac", files.getFirst()),
        () ->
            assertEquals(
                "Greta Van Fleet/Anthem of the Peaceful Army/02 The Cold Wind.flac", files.get(1)));
  }

  @Test
  void singleSongUnknownResponse() {
    var response = new ArrayList<>(createSingleResponse());
    response.add("unknown: I dont know");
    assertDoesNotThrow(() -> this.converter.convertResponseToSongs(response));
  }

  @Test
  void emptyResponse() {
    assertEquals(0, this.converter.convertResponseToSongs(new ArrayList<>()).size());
  }

  private List<String> createSingleResponse() {
    return Arrays.asList(
        "file: Tool/10,000 Days/01 Vicarious.flac",
        "Last-Modified: 2022-02-19T12:52:00Z",
        "Format: 44100:16:2",
        "Time: 427",
        "duration: 426.680",
        "Performer: Danny Carey (membranophone)",
        "Performer: Justin Chancellor (bass guitar)",
        "Performer: Adam Jones (guitar)",
        "Performer: Maynard James Keenan (lead vocals)",
        "MUSICBRAINZ_RELEASETRACKID: 73735e2e-5d72-4453-8545-d1e55f2c17ae",
        "MUSICBRAINZ_WORKID: 1a1872d9-04cb-4c35-8b83-33eb76d8a45a",
        "Album: 10,000 Days",
        "AlbumArtist: Tool",
        "AlbumArtistSort: Tool",
        "Artist: Tool",
        "ArtistSort: Tool",
        "Disc: 1",
        "Genre: Hard Rock",
        "Label: Tool Dissectional",
        "MUSICBRAINZ_ALBUMARTISTID: 66fc5bf8-daa4-4241-b378-9bc9077939d2",
        "MUSICBRAINZ_ALBUMID: 287a7dee-5c59-4bae-9972-b806d8fcb8ed",
        "MUSICBRAINZ_ARTISTID: 66fc5bf8-daa4-4241-b378-9bc9077939d2",
        "MUSICBRAINZ_TRACKID: a48c9643-d98b-4043-9b24-be04eee0e807",
        "OriginalDate: 2006-04-28",
        "Title: Vicarious",
        "Date: 2006",
        "Date: 2006-04-28",
        "Comment: JavaMPD comment",
        "Track: 1",
        "ok");
  }

  private List<String> createMultipleResponse() {
    return Arrays.asList(
        "file: Breaking Benjamin/Ember/03. Red Cold River.flac",
        "Last-Modified: 2022-02-19T12:50:00Z",
        "Format: 44100:16:2",
        "Time: 201",
        "duration: 200.960",
        "Album: Ember",
        "Artist: Breaking Benjamin",
        "AlbumArtist: Breaking Benjamin",
        "Disc: 1",
        "Genre: Alternative Metal",
        "Title: Red Cold River",
        "Date: 2018",
        "Track: 3",
        "file: Breaking Benjamin/We Are Not Alone/01-So Cold.flac",
        "Last-Modified: 2022-02-19T12:50:00Z",
        "Format: 44100:16:2",
        "Time: 273",
        "duration: 273.293",
        "Album: We Are Not Alone",
        "Artist: Breaking Benjamin",
        "Title: So Cold",
        "Date: 2004",
        "Track: 1",
        "file: Greta Van Fleet/Anthem of the Peaceful Army/02 The Cold Wind.flac",
        "Last-Modified: 2022-02-19T12:56:00Z",
        "Format: 44100:16:2",
        "Time: 197",
        "duration: 196.546",
        "MUSICBRAINZ_RELEASETRACKID: 0081de95-3d88-43b3-9bb4-ff0fde825556",
        "AlbumArtistSort: Greta Van Fleet",
        "ArtistSort: Greta Van Fleet",
        "Disc: 1",
        "Label: Republic Records",
        "MUSICBRAINZ_ALBUMARTISTID: 0be22557-d8c7-4706-a531-625c4c570162",
        "MUSICBRAINZ_ALBUMID: 87fc4a33-8cea-4d1f-a00b-ca02791cc288",
        "MUSICBRAINZ_ARTISTID: 0be22557-d8c7-4706-a531-625c4c570162",
        "MUSICBRAINZ_TRACKID: f4872a53-bf91-47ff-8115-b9af8e0d9398",
        "OriginalDate: 2018-10-19",
        "Title: The Cold Wind",
        "Artist: Greta Van Fleet",
        "Album: Anthem of the Peaceful Army",
        "Genre: Rock",
        "AlbumArtist: Greta Van Fleet",
        "Disc: 1",
        "Date: 2018",
        "Track: 2",
        "OK");
  }
}
