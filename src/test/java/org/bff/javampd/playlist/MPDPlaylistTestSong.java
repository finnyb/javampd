package org.bff.javampd.playlist;

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

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDPlaylistTestSong {

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
    void setup() {
        realPlaylistProperties = new PlaylistProperties();
    }

    @Test
    void testAddSong() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        MPDSong mpdSong = new MPDSong("test", "test");

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(mpdSong);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals(mpdSong.getFile(), stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_ADDED, changeEvent[0].getEvent());
        assertEquals(mpdSong.getFile(), changeEvent[0].getName());
    }

    @Test
    void testAddSongNoEvent() {
        when(serverStatus.getPlaylistVersion()).thenReturn(-1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(new MPDSong("test", "test"), false);

        assertNull(changeEvent[0]);
    }

    @Test
    void testAddSongFile() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        MPDSong mpdSong = new MPDSong("testFile", "test");

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(mpdSong.getFile());

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals(mpdSong.getFile(), stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_ADDED, changeEvent[0].getEvent());
        assertEquals(mpdSong.getFile(), changeEvent[0].getName());
    }

    @Test
    void testAddSongFileNoEvent() {
        when(serverStatus.getPlaylistVersion()).thenReturn(-1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong("testFile", false);

        assertNull(changeEvent[0]);
    }

    @Test
    void testAddSongs() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("test1", "test1"));
        songs.add(new MPDSong("test2", "test2"));

        playlist.addSongs(songs);

        verify(commandExecutor)
                .sendCommands(commandArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), commandArgumentCaptor.getAllValues().get(0).get(0).getCommand());
        assertEquals("test1", commandArgumentCaptor.getAllValues().get(0).get(0).getParams().get(0));
        assertEquals(realPlaylistProperties.getAdd(), commandArgumentCaptor.getAllValues().get(0).get(1).getCommand());
        assertEquals("test2", commandArgumentCaptor.getAllValues().get(0).get(1).getParams().get(0));
        assertEquals(PlaylistChangeEvent.Event.MULTIPLE_SONGS_ADDED, changeEvent[0].getEvent());
    }

    @Test
    void testAddSongsNoEvent() {
        when(serverStatus.getPlaylistVersion()).thenReturn(-1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(new MPDSong("test", "test"), false);

        assertNull(changeEvent[0]);
    }

    @Test
    void testRemoveSong() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        MPDSong mpdSong = new MPDSong("test", "test");
        mpdSong.setPosition(5);
        playlist.removeSong(mpdSong);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemove(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 5, integerArgumentCaptor.getValue());
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }

    @Test
    void testRemoveSongByPosition() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        int position = 5;

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        MPDSong mpdSong = new MPDSong("test", "test");
        mpdSong.setPosition(position);
        playlist.removeSong(position);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemove(), stringArgumentCaptor.getValue());
        assertEquals((Integer) position, integerArgumentCaptor.getValue());
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }

    @Test
    void testRemoveSongByBadPosition() {
        int position = -1;

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        MPDSong mpdSong = new MPDSong("test", "test");
        mpdSong.setPosition(position);
        playlist.removeSong(position);

        assertNull(changeEvent[0]);
    }

    @Test
    void testRemoveSongById() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        MPDSong song = new MPDSong("test", "test");
        song.setId(5);
        playlist.removeSong(song);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getRemoveId(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 5, integerArgumentCaptor.getValue());
        assertEquals(PlaylistChangeEvent.Event.SONG_DELETED, changeEvent[0].getEvent());
    }

    @Test
    void testGetCurrentSong() {
        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getCurrentSong())).thenReturn(response);
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("test", "test"));
        when(songConverter.convertResponseToSong(response)).thenReturn(songs);

        assertEquals("test", playlist.getCurrentSong().getName());
    }

    @Test
    void testListSongs() {
        List<MPDSong> mockedSongs = new ArrayList<>();
        mockedSongs.add(new MPDSong("file1", "testSong1"));
        mockedSongs.add(new MPDSong("file2", "testSong2"));

        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getInfo())).thenReturn(response);
        when(songConverter.convertResponseToSong(response)).thenReturn(mockedSongs);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        List<MPDSong> songs = playlist.getSongList();

        assertEquals(mockedSongs.size(), songs.size());
        assertEquals(mockedSongs.get(0), songs.get(0));
        assertEquals(mockedSongs.get(1), songs.get(1));
    }
}
