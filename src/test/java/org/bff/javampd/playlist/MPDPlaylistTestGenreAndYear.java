package org.bff.javampd.playlist;

import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.SongConverter;
import org.bff.javampd.song.SongDatabase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDPlaylistTestGenreAndYear {

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

    @Before
    public void setup() {
        realPlaylistProperties = new PlaylistProperties();
    }

    @Test
    public void testInsertGenre() throws Exception {
        MPDGenre genre = new MPDGenre("testGenre");

        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("file1", "testSong1"));
        songs.add(new MPDSong("file2", "testSong2"));

        when(songDatabase.findGenre(genre.getName())).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertGenre(genre);

        when(playlistProperties.getAdd()).thenReturn(realPlaylistProperties.getAdd());

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.GENRE_ADDED, changeEvent[0].getEvent());
    }

    @Test
    public void testInsertGenreByName() throws Exception {
        String genre = "testGenre";

        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("file1", "testSong1"));
        songs.add(new MPDSong("file2", "testSong2"));
        when(songDatabase.findGenre(genre)).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertGenre(genre);

        when(playlistProperties.getAdd()).thenReturn(realPlaylistProperties.getAdd());

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.GENRE_ADDED, changeEvent[0].getEvent());
    }

    @Test
    public void testRemoveGenre() throws Exception {
        MPDGenre genre = new MPDGenre("testGenre");

        List<MPDSong> mockedSongs = new ArrayList<>();
        MPDSong song1 = new MPDSong("file1", "testSong1");
        song1.setGenre(genre.getName());
        song1.setId(1);
        MPDSong song2 = new MPDSong("file2", "testSong1");
        song2.setGenre(genre.getName());
        song2.setId(2);
        MPDSong song3 = new MPDSong("file3", "testSong1");
        song3.setGenre("bogus");

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        when(playlistProperties.getInfo()).thenReturn(realPlaylistProperties.getInfo());

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);


        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeGenre(genre);

        when(playlistProperties.getRemoveId()).thenReturn(realPlaylistProperties.getRemoveId());
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
    public void testRemoveGenreByName() throws Exception {
        String genre = "testGenre";

        List<MPDSong> mockedSongs = new ArrayList<>();
        MPDSong song1 = new MPDSong("file1", "testSong1");
        song1.setGenre(genre);
        song1.setId(1);
        MPDSong song2 = new MPDSong("file2", "testSong1");
        song2.setGenre(genre);
        song2.setId(2);
        MPDSong song3 = new MPDSong("file3", "testSong1");
        song3.setGenre("bogus");

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        when(playlistProperties.getInfo()).thenReturn(realPlaylistProperties.getInfo());

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);


        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeGenre(genre);

        when(playlistProperties.getRemoveId()).thenReturn(realPlaylistProperties.getRemoveId());
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
    public void testInsertYear() throws Exception {
        String year = "testYear";

        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("file1", "testSong1"));
        songs.add(new MPDSong("file2", "testSong2"));
        when(songDatabase.findYear(year)).thenReturn(songs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.insertYear(year);

        when(playlistProperties.getAdd()).thenReturn(realPlaylistProperties.getAdd());

        verify(commandExecutor, times(2))
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("file1", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(2));
        assertEquals("file2", stringArgumentCaptor.getAllValues().get(3));
        assertEquals(PlaylistChangeEvent.Event.YEAR_ADDED, changeEvent[0].getEvent());
    }

    @Test
    public void testRemoveYear() throws Exception {
        String year = "testYear";

        List<MPDSong> mockedSongs = new ArrayList<>();
        MPDSong song1 = new MPDSong("file1", "testSong1");
        song1.setYear(year);
        song1.setId(1);
        MPDSong song2 = new MPDSong("file2", "testSong1");
        song2.setYear(year);
        song2.setId(2);
        MPDSong song3 = new MPDSong("file3", "testSong1");
        song3.setYear("bogus");

        mockedSongs.add(song1);
        mockedSongs.add(song2);
        mockedSongs.add(song3);

        when(playlistProperties.getInfo()).thenReturn(realPlaylistProperties.getInfo());

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);


        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.removeYear(year);

        when(playlistProperties.getRemoveId()).thenReturn(realPlaylistProperties.getRemoveId());
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