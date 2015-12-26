package org.bff.javampd.playlist;

import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
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
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDPlaylistTestSong {

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
    public void testAddSong() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(new MPDSong("test", "test"));

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("test", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.SONG_ADDED, changeEvent[0].getEvent());
    }

    @Test
    public void testAddSongNoEvent() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
        when(serverStatus.getPlaylistVersion()).thenReturn(-1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(new MPDSong("test", "test"), false);

        assertNull(changeEvent[0]);
    }

    @Test
    public void testAddSongs() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("test1", "test1"));
        songs.add(new MPDSong("test2", "test2"));

        when(playlistProperties.getAdd()).thenReturn(realPlaylistProperties.getAdd());

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
    public void testAddSongsNoEvent() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
        when(serverStatus.getPlaylistVersion()).thenReturn(-1);
        when(playlistProperties.getAdd()).thenReturn(realPlaylistProperties.getAdd());

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addSong(new MPDSong("test", "test"), false);

        assertNull(changeEvent[0]);
    }

    @Test
    public void testRemoveSong() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
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
    public void testRemoveSongById() throws Exception {
        when(playlistProperties.getLoad()).thenReturn(realPlaylistProperties.getLoad());
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
    public void testGetCurrentSong() throws Exception {
        when(playlistProperties.getCurrentSong()).thenReturn(realPlaylistProperties.getCurrentSong());
        List<String> response = new ArrayList<>();
        response.add("test");
        when(commandExecutor.sendCommand(realPlaylistProperties.getCurrentSong())).thenReturn(response);
        List<MPDSong> songs = new ArrayList<>();
        songs.add(new MPDSong("test", "test"));
        when(songConverter.convertResponseToSong(response)).thenReturn(songs);

        assertEquals("test", playlist.getCurrentSong().getName());
    }

    @Test
    public void testListSongs() throws Exception {
        List<MPDSong> mockedSongs = new ArrayList<>();
        mockedSongs.add(new MPDSong("file1", "testSong1"));
        mockedSongs.add(new MPDSong("file2", "testSong2"));

        when(playlistProperties.getInfo()).thenReturn(realPlaylistProperties.getInfo());

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