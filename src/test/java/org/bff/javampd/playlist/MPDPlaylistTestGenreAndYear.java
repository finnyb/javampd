package org.bff.javampd.playlist;

import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.genre.MPDGenre;
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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MPDPlaylistTestGenreAndYear {

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
    @Mock
    private PlaylistSongConverter playlistSongConverter;
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
    void setup() {
        realPlaylistProperties = new PlaylistProperties();
    }

    @Test
    void testInsertGenre() {
        MPDGenre genre = new MPDGenre("testGenre");

        List<MPDSong> songs = new ArrayList<>();
        songs.add(MPDSong.builder().file("file1").title("testSong1").build());
        songs.add(MPDSong.builder().file("file2").title("testSong2").build());

        when(songDatabase.findGenre(genre.getName())).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertGenre(genre);

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.GENRE_ADDED, changeEvent[0].getEvent());
    }

    @Test
    void testInsertGenreByName() {
        String genre = "testGenre";

        List<MPDSong> songs = new ArrayList<>();
        songs.add(MPDSong.builder().file("file1").title("testSong1").build());
        songs.add(MPDSong.builder().file("file2").title("testSong2").build());
        when(songDatabase.findGenre(genre)).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertGenre(genre);

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.GENRE_ADDED, changeEvent[0].getEvent());
    }

    @Test
    void testRemoveGenre() {
        MPDGenre genre = new MPDGenre("testGenre");

        var mockedSongs = new ArrayList<MPDPlaylistSong>();
        MPDPlaylistSong song1 = MPDPlaylistSong.builder()
                .file("file1")
                .title("testSong1")
                .genre(genre.getName())
                .id(1)
                .build();

        MPDPlaylistSong song2 = MPDPlaylistSong.builder()
                .file("file2")
                .title("testSong1")
                .genre(genre.getName())
                .id(2)
                .build();

        MPDPlaylistSong song3 = MPDPlaylistSong.builder()
                .file("file3")
                .title("testSong1")
                .genre("bogus")
                .build();

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(playlistSongConverter.convertResponseToSongs(response)).thenReturn(mockedSongs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeGenre(genre);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture());
        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(1));
        assertEquals((Integer) song1.getId(), integerArgumentCaptor.getAllValues().get(0));
        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals((Integer) song2.getId(), integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }

    @Test
    void testRemoveGenreByName() {
        String genre = "testGenre";

        var mockedSongs = new ArrayList<MPDPlaylistSong>();
        MPDPlaylistSong song1 = MPDPlaylistSong.builder()
                .file("file1")
                .title("testSong1")
                .genre(genre)
                .id(1)
                .build();

        MPDPlaylistSong song2 = MPDPlaylistSong.builder()
                .file("file2")
                .title("testSong1")
                .genre(genre)
                .id(2)
                .build();

        MPDPlaylistSong song3 = MPDPlaylistSong.builder()
                .file("file3")
                .title("testSong1")
                .genre("bogus")
                .build();

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(playlistSongConverter.convertResponseToSongs(response)).thenReturn(mockedSongs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeGenre(genre);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture());
        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(1));
        assertEquals((Integer) song1.getId(), integerArgumentCaptor.getAllValues().get(0));
        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals((Integer) song2.getId(), integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }

    @Test
    void testInsertYear() {
        String year = "testYear";

        List<MPDSong> songs = new ArrayList<>();
        songs.add(MPDSong.builder().file("file1").title("testSong1").build());
        songs.add(MPDSong.builder().file("file2").title("testSong2").build());
        when(songDatabase.findYear(year)).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertYear(year);

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.YEAR_ADDED, changeEvent[0].getEvent());
    }

    @Test
    void testRemoveYear() {
        String year = "testYear";

        var mockedSongs = new ArrayList<MPDPlaylistSong>();
        MPDPlaylistSong song1 = MPDPlaylistSong.builder()
                .file("file1")
                .title("testSong1")
                .date(year)
                .id(1)
                .build();

        MPDPlaylistSong song2 = MPDPlaylistSong.builder()
                .file("file2")
                .title("testSong1")
                .date(year)
                .id(2)
                .build();

        MPDPlaylistSong song3 = MPDPlaylistSong.builder()
                .file("file3")
                .title("testSong1")
                .date("bogus")
                .build();

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(playlistSongConverter.convertResponseToSongs(response)).thenReturn(mockedSongs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeYear(year);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture());
        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(1));
        assertEquals((Integer) song1.getId(), integerArgumentCaptor.getAllValues().get(0));
        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals((Integer) song2.getId(), integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }
}
