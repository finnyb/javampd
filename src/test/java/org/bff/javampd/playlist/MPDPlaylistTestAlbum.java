package org.bff.javampd.playlist;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MPDPlaylistTestAlbum {
  @Mock
  private SongDatabase songDatabase;

  @Mock
  private ServerStatus serverStatus;

  @Mock
  private PlaylistProperties playlistProperties;

  @Mock
  private CommandExecutor commandExecutor;

  @Mock
  private SongConverter songConverter;

  @InjectMocks
  private MPDPlaylist playlist;

  @Captor
  private ArgumentCaptor<String> stringArgumentCaptor;

  @Captor
  private ArgumentCaptor<Integer> integerArgumentCaptor;

  @Captor
  private ArgumentCaptor<List<MPDCommand>> commandArgumentCaptor;

  private PlaylistProperties realPlaylistProperties;

  @BeforeEach
  public void setup() {
    realPlaylistProperties = new PlaylistProperties();
  }

  @Test
  public void testInsertAlbumByArtist() {
    MPDArtist artist = new MPDArtist("testArtist");
    MPDAlbum album = new MPDAlbum("testAlbum", "testArtist");

    List<MPDSong> songs = new ArrayList<>();
    songs.add(new MPDSong("file1", "testSong1"));
    songs.add(new MPDSong("file2", "testSong2"));
    when(songDatabase.findAlbumByArtist(artist, album)).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertAlbum(artist, album);

    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(
      PlaylistChangeEvent.Event.ALBUM_ADDED,
      changeEvent[0].getEvent()
    );
  }

  @Test
  public void testInsertAlbumByNames() {
    String artist = "testArtist";
    String album = "testAlbum";

    List<MPDSong> songs = new ArrayList<>();
    songs.add(new MPDSong("file1", "testSong1"));
    songs.add(new MPDSong("file2", "testSong2"));
    when(songDatabase.findAlbumByArtist(artist, album)).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertAlbum(artist, album);

    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(
      PlaylistChangeEvent.Event.ALBUM_ADDED,
      changeEvent[0].getEvent()
    );
  }

  @Test
  public void testInsertAlbumByName() {
    String album = "testAlbum";

    List<MPDSong> songs = new ArrayList<>();
    songs.add(new MPDSong("file1", "testSong1"));
    songs.add(new MPDSong("file2", "testSong2"));
    when(songDatabase.findAlbum(album)).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertAlbum(album);

    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(
      PlaylistChangeEvent.Event.ALBUM_ADDED,
      changeEvent[0].getEvent()
    );
  }

  @Test
  public void testInsertAlbumByAlbum() {
    MPDAlbum album = new MPDAlbum("testAlbum", "testArtist");

    List<MPDSong> songs = new ArrayList<>();
    songs.add(new MPDSong("file1", "testSong1"));
    songs.add(new MPDSong("file2", "testSong2"));
    when(songDatabase.findAlbum(album)).thenReturn(songs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.insertAlbum(album);

    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        stringArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(0)
    );
    assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
    assertEquals(
      realPlaylistProperties.getAdd(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
    assertEquals(
      PlaylistChangeEvent.Event.ALBUM_ADDED,
      changeEvent[0].getEvent()
    );
  }

  @Test
  public void testRemoveAlbumByArtist() {
    MPDArtist artist = new MPDArtist("testArtist");
    MPDAlbum album = new MPDAlbum("testAlbum", "testArtist");

    List<MPDSong> mockedSongs = new ArrayList<>();
    MPDSong song1 = new MPDSong("file1", "testSong1");
    song1.setArtistName(artist.getName());
    song1.setAlbumName(album.getName());
    song1.setId(1);
    MPDSong song2 = new MPDSong("file2", "testSong1");
    song2.setArtistName(artist.getName());
    song2.setAlbumName(album.getName());
    song2.setId(2);
    MPDSong song3 = new MPDSong("file3", "testSong1");
    song3.setArtistName("bogus");
    song3.setAlbumName("bogus");

    mockedSongs.add(song1);
    mockedSongs.add(song2);
    mockedSongs.add(song3);

    List<String> response = new ArrayList<>();
    response.add("test");
    when(commandExecutor.sendCommand(realPlaylistProperties.getInfo()))
      .thenReturn(response);
    when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.removeAlbum(artist, album);

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getRemoveId(),
      stringArgumentCaptor.getAllValues().get(1)
    );
    assertEquals(
      (Integer) song1.getId(),
      integerArgumentCaptor.getAllValues().get(0)
    );
    assertEquals(
      realPlaylistProperties.getRemoveId(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals(
      (Integer) song2.getId(),
      integerArgumentCaptor.getAllValues().get(1)
    );
    assertEquals(
      PlaylistChangeEvent.Event.SONG_DELETED,
      changeEvent[0].getEvent()
    );
  }

  @Test
  public void testRemoveAlbumByName() {
    String artist = "testArtist";
    String album = "testAlbum";

    List<MPDSong> mockedSongs = new ArrayList<>();
    MPDSong song1 = new MPDSong("file1", "testSong1");
    song1.setArtistName(artist);
    song1.setAlbumName(album);
    song1.setId(1);
    MPDSong song2 = new MPDSong("file2", "testSong1");
    song2.setArtistName(artist);
    song2.setAlbumName(album);
    song2.setId(2);
    MPDSong song3 = new MPDSong("file3", "testSong1");
    song3.setArtistName("bogus");
    song3.setAlbumName("bogus");

    mockedSongs.add(song1);
    mockedSongs.add(song2);
    mockedSongs.add(song3);

    List<String> response = new ArrayList<>();
    response.add("test");
    when(commandExecutor.sendCommand(realPlaylistProperties.getInfo()))
      .thenReturn(response);
    when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);

    final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
    playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

    playlist.removeAlbum(artist, album);

    verify(commandExecutor).sendCommand(stringArgumentCaptor.capture());
    verify(commandExecutor, times(2))
      .sendCommand(
        stringArgumentCaptor.capture(),
        integerArgumentCaptor.capture()
      );

    assertEquals(
      realPlaylistProperties.getRemoveId(),
      stringArgumentCaptor.getAllValues().get(1)
    );
    assertEquals(
      (Integer) song1.getId(),
      integerArgumentCaptor.getAllValues().get(0)
    );
    assertEquals(
      realPlaylistProperties.getRemoveId(),
      stringArgumentCaptor.getAllValues().get(2)
    );
    assertEquals(
      (Integer) song2.getId(),
      integerArgumentCaptor.getAllValues().get(1)
    );
    assertEquals(
      PlaylistChangeEvent.Event.SONG_DELETED,
      changeEvent[0].getEvent()
    );
  }
}
