package org.bff.javampd.playlist;

import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.command.MPDCommand;
import org.bff.javampd.file.MPDFile;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MPDPlaylistTest {
    @Mock
    private ServerStatus serverStatus;
    @Mock
    private PlaylistProperties playlistProperties;
    @Mock
    private CommandExecutor commandExecutor;
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
    public void testAddPlaylistChangeListener() {
        final boolean[] gotEvent = {true};
        playlist.addPlaylistChangeListener(event -> gotEvent[0] = true);
        playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
        assertTrue(gotEvent[0]);
    }

    @Test
    public void testRemovePlaylistStatusChangedListener() {
        final boolean[] gotEvent = {true};

        PlaylistChangeListener pcl = event -> gotEvent[0] = true;

        playlist.addPlaylistChangeListener(pcl);
        playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
        assertTrue(gotEvent[0]);

        gotEvent[0] = false;
        playlist.removePlaylistStatusChangedListener(pcl);
        playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ARTIST_ADDED);
        assertFalse(gotEvent[0]);
    }

    @Test
    public void testFirePlaylistChangeEvent() {
        final boolean[] gotEvent = {true};
        playlist.addPlaylistChangeListener(event -> gotEvent[0] = true);
        playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.ALBUM_ADDED);
        assertTrue(gotEvent[0]);
    }

    @Test
    public void testFirePlaylistChangeEventSongAdded() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.firePlaylistChangeEvent(PlaylistChangeEvent.Event.SONG_ADDED, "name");
        assertEquals("name", changeEvent[0].getName());
    }

    @Test
    public void testLoadPlaylist() {
        String testPlaylist = "testPlaylist";
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.loadPlaylist(testPlaylist);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertEquals(realPlaylistProperties.getLoad(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testLoadPlaylistM3u() {
        String testPlaylist = "testPlaylist.m3u";
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.loadPlaylist(testPlaylist);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());
        assertEquals(realPlaylistProperties.getLoad(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testAddFileOrDirectory() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.addFileOrDirectory(new MPDFile("test"));

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getAdd(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("test", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.FILE_ADDED, changeEvent[0].getEvent());
    }

    @Test
    public void testClearPlaylist() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.clearPlaylist();

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getClear(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CLEARED, changeEvent[0].getEvent());
    }

    @Test
    public void testDeletePlaylist() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSavedPlaylist savedPlaylist = new MPDSavedPlaylist("testPlaylist");
        playlist.deletePlaylist(savedPlaylist);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getDelete(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_DELETED, changeEvent[0].getEvent());
    }

    @Test
    public void testDeletePlaylistByName() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);
        playlist.deletePlaylist("testPlaylist");

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getDelete(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("testPlaylist", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_DELETED, changeEvent[0].getEvent());
    }

    @Test
    public void testMoveById() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSong song = new MPDSong("test", "test");
        song.setId(3);
        playlist.move(song, 5);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(),
                        integerArgumentCaptor.capture(),
                        integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getMoveId(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
        assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testMoveByPosition() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSong song = new MPDSong("test", "test");
        song.setPosition(3);
        playlist.move(song, 5);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(),
                        integerArgumentCaptor.capture(),
                        integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getMove(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
        assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testShuffle() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        playlist.shuffle();

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getShuffle(), stringArgumentCaptor.getValue());
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testSwapByExplicitId() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSong song1 = new MPDSong("test", "test");
        song1.setId(3);

        playlist.swap(song1, 5);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(),
                        integerArgumentCaptor.capture(),
                        integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getSwapId(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
        assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testSwapById() {
        when(serverStatus.getPlaylistVersion()).thenReturn(1);

        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSong song1 = new MPDSong("test", "test");
        song1.setId(3);
        MPDSong song2 = new MPDSong("test", "test");
        song2.setId(5);

        playlist.swap(song1, song2);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(),
                        integerArgumentCaptor.capture(),
                        integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getSwapId(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
        assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testSwapByPosition() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        MPDSong song1 = new MPDSong("test", "test");
        song1.setPosition(3);
        MPDSong song2 = new MPDSong("test", "test");
        song2.setPosition(5);

        playlist.swap(song1, song2);

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(),
                        integerArgumentCaptor.capture(),
                        integerArgumentCaptor.capture());

        assertEquals(realPlaylistProperties.getSwap(), stringArgumentCaptor.getValue());
        assertEquals((Integer) 3, integerArgumentCaptor.getAllValues().get(0));
        assertEquals((Integer) 5, integerArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0].getEvent());
    }

    @Test
    public void testPlaylistSave() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        boolean saved = playlist.savePlaylist("name");

        verify(commandExecutor)
                .sendCommand(stringArgumentCaptor.capture(), stringArgumentCaptor.capture());

        assertTrue(saved);
        assertEquals(realPlaylistProperties.getSave(), stringArgumentCaptor.getAllValues().get(0));
        assertEquals("name", stringArgumentCaptor.getAllValues().get(1));
        assertEquals(PlaylistChangeEvent.Event.PLAYLIST_SAVED, changeEvent[0].getEvent());
    }

    @Test
    public void testPlaylistSaveNull() {
        final PlaylistChangeEvent[] changeEvent = new PlaylistChangeEvent[1];
        playlist.addPlaylistChangeListener(event -> changeEvent[0] = event);

        boolean saved = playlist.savePlaylist(null);

        assertFalse(saved);
        assertNull(changeEvent[0]);
    }
}