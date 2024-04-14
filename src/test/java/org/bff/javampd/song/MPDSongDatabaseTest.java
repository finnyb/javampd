package org.bff.javampd.song;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MPDSongDatabaseTest {
  private SongDatabase songDatabase;
  private SongSearcher mockedSongSearcher;

  @Captor private ArgumentCaptor<String> argumentCaptor;
  @Captor private ArgumentCaptor<SongSearcher.ScopeType> scopeCaptor;

  @BeforeEach
  void setup() {
    mockedSongSearcher = mock(SongSearcher.class);
    songDatabase = new MPDSongDatabase(mockedSongSearcher);
  }

  @Test
  @DisplayName("Finding album from album object")
  void testFindAlbum() {
    String testAlbumName = "testAlbumName";

    when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
        .thenReturn(Collections.singletonList(MPDSong.builder().build()));

    songDatabase.findAlbum(MPDAlbum.builder(testAlbumName).build());

    verify(mockedSongSearcher).find(scopeCaptor.capture(), argumentCaptor.capture());
    assertAll(
        () -> assertEquals(testAlbumName, argumentCaptor.getValue()),
        () -> assertEquals(SongSearcher.ScopeType.ALBUM, scopeCaptor.getValue()));
  }

  @Test
  @DisplayName("Finding album directly by name")
  void testFindAlbumByName() {
    String testAlbumName = "testAlbumName";

    when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
        .thenReturn(Collections.singletonList(MPDSong.builder().build()));

    songDatabase.findAlbum(testAlbumName);

    verify(mockedSongSearcher).find(scopeCaptor.capture(), argumentCaptor.capture());
    assertAll(
        () -> assertEquals(testAlbumName, argumentCaptor.getValue()),
        () -> assertEquals(SongSearcher.ScopeType.ALBUM, scopeCaptor.getValue()));
  }

  @Test
  @DisplayName("Finding songs from album and artist")
  void testFindAlbumByArtist() {
    String testAlbumName = "testAlbumName";
    String testArtistName = "testArtistName";

    when(mockedSongSearcher.find(SongSearcher.ScopeType.ALBUM, testAlbumName))
        .thenReturn(
            List.of(
                MPDSong.builder().artistName(testArtistName).build(), MPDSong.builder().build()));

    var songs =
        new ArrayList<>(
            songDatabase.findAlbumByArtist(
                new MPDArtist(testArtistName), MPDAlbum.builder(testAlbumName).build()));

    verify(mockedSongSearcher).find(scopeCaptor.capture(), argumentCaptor.capture());
    assertAll(
        () -> assertEquals(testAlbumName, argumentCaptor.getValue()),
        () -> assertEquals(SongSearcher.ScopeType.ALBUM, scopeCaptor.getValue()),
        () -> assertEquals(1, songs.size()),
        () -> assertEquals(testArtistName, songs.getFirst().getArtistName()));
  }
}
