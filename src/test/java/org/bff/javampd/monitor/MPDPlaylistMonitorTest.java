package org.bff.javampd.monitor;

import org.bff.javampd.playlist.PlaylistBasicChangeEvent;
import org.bff.javampd.playlist.PlaylistBasicChangeListener;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDPlaylistMonitorTest {

    @Mock
    private PlayerMonitor playerMonitor;
    @InjectMocks
    private MPDPlaylistMonitor playlistMonitor;

    @Test
    public void testAddPlaylistChangeListener() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_ADDED);
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
    }

    @Test
    public void testRemovePlaylistChangeListener() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        PlaylistBasicChangeListener playlistChangeListener = event -> changeEvent[0] = event.getEvent();

        playlistMonitor.addPlaylistChangeListener(playlistChangeListener);

        playlistMonitor.firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_ADDED);
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
        playlistMonitor.removePlaylistChangeListener(playlistChangeListener);
        playlistMonitor.firePlaylistChangeEvent(PlaylistBasicChangeEvent.Event.SONG_DELETED);
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
    }

    @Test
    public void testGetSongId() throws Exception {
        playlistMonitor.processResponseStatus("songid: 1");
        assertEquals(1, playlistMonitor.getSongId());
    }

    @Test
    public void testPlayerStopped() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("songid: -1");
        playlistMonitor.playerStopped();
        assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_ENDED, changeEvent[0]);
    }

    @Test
    public void testPlayerNotStopped() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());

        playlistMonitor.processResponseStatus("songid: 1");
        playlistMonitor.playerStopped();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusPlaylistChanged() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("playlist: 1");
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusPlaylistNotChanged() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("playlist: 1");
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0]);

        changeEvent[0] = null;
        playlistMonitor.processResponseStatus("playlist: 1");
        playlistMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusSongAdded() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("playlistlength: 1");
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusSongDeleted() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("playlistlength: 1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus("playlistlength: 0");
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_DELETED, changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusSongUnchanged() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("playlistlength: 1");
        playlistMonitor.checkStatus();
        changeEvent[0] = null;
        playlistMonitor.processResponseStatus("playlistlength: 1");
        playlistMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusSongChanged() throws Exception {
        statusSongChanged("song: 1");
    }

    @Test
    public void testProcessResponseStatusSongChangedById() throws Exception {
        statusSongChanged("songid: 1");
    }

    private void statusSongChanged(String line) {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus(line);
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_CHANGED, changeEvent[0]);
    }

    private void statusSongNoChange(String line) {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus(line);
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
        playlistMonitor.checkStatus();

        changeEvent[0] = null;
        playlistMonitor.processResponseStatus(line);
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
        playlistMonitor.checkStatus();

        assertNull(changeEvent[0]);
    }

    @Test
    public void testProcessResponseStatusSongNoChange() throws Exception {
        statusSongNoChange("song: 1");
    }

    @Test
    public void testProcessResponseStatusSongNoChangeById() throws Exception {
        statusSongNoChange("songid: 1");
    }

    @Test
    public void testProcessResponseStatusPlaylistNothing() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];
        playlistMonitor.processResponseStatus("bogus: 1");
        playlistMonitor.checkStatus();
        assertNull(changeEvent[0]);
    }

    @Test
    public void testSongChangeNotPlaying() throws Exception {
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus("song: 1");
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
        playlistMonitor.checkStatus();

        assertNull(changeEvent[0]);
    }

    @Test
    public void testResetSongId() throws Exception {
        String line = "songid: 1";
        statusSongChanged(line);
        playlistMonitor.reset();
        statusSongChanged(line);
    }

    @Test
    public void testResetSong() throws Exception {
        String line = "song: 1";
        statusSongChanged(line);
        playlistMonitor.reset();
        statusSongChanged(line);

    }

    @Test
    public void testResetPlaylistVersion() throws Exception {
        String line = "playlist: 1";
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus(line);
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0]);

        playlistMonitor.reset();
        changeEvent[0] = null;

        playlistMonitor.processResponseStatus(line);
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED, changeEvent[0]);
    }

    @Test
    public void testResetPlaylistLength() throws Exception {
        String line = "playlistlength: 1";
        final PlaylistBasicChangeEvent.Event[] changeEvent = new PlaylistBasicChangeEvent.Event[1];

        playlistMonitor.addPlaylistChangeListener(event -> changeEvent[0] = event.getEvent());
        playlistMonitor.processResponseStatus(line);
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);

        playlistMonitor.reset();
        changeEvent[0] = null;

        playlistMonitor.processResponseStatus(line);
        playlistMonitor.checkStatus();
        assertEquals(PlaylistBasicChangeEvent.Event.SONG_ADDED, changeEvent[0]);

    }
}