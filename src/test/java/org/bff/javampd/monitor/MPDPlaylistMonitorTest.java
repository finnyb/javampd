package org.bff.javampd.monitor;

import org.bff.javampd.events.PlaylistBasicChangeEvent;
import org.bff.javampd.events.PlaylistBasicChangeListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDPlaylistMonitorTest {
    @Mock
    private MPDPlayerMonitor playerMonitor;

    @InjectMocks
    private MPDPlaylistMonitor playlistMonitor;

    private boolean success;

    private static final String RESPONSE = "playlist: ";
    private static final String RESPONSE_LENGTH = "playlistlength: ";
    private static final String RESPONSE_SONG = "song: ";
    private static final String RESPONSE_SONG_ID = "songid: ";

    @Before
    public void setUp() {
        success = false;
    }

    @Test
    public void testCheckStatusChanged() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.PLAYLIST_CHANGED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
        playlistMonitor.processResponseStatus(RESPONSE + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE + "2");
        playlistMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusEnded() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.PLAYLIST_ENDED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
        playlistMonitor.processResponseStatus(RESPONSE_SONG_ID + "-1");
        playlistMonitor.playerStopped();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusSongAdded() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.SONG_ADDED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
        playlistMonitor.processResponseStatus(RESPONSE_LENGTH + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE_LENGTH + "2");
        playlistMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckSongDeleted() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.SONG_DELETED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_STOPPED);
        playlistMonitor.processResponseStatus(RESPONSE_LENGTH + "2");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE_LENGTH + "1");
        playlistMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusSongChanged() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.SONG_CHANGED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
        playlistMonitor.processResponseStatus(RESPONSE_SONG + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE_SONG + "2");
        playlistMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusSongChangedById() throws Exception {
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = event.getEvent() == PlaylistBasicChangeEvent.Event.SONG_CHANGED;
            }
        });
        when(playerMonitor.getStatus()).thenReturn(PlayerStatus.STATUS_PLAYING);
        playlistMonitor.processResponseStatus(RESPONSE_SONG_ID + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE_SONG_ID + "2");
        playlistMonitor.checkStatus();
        assertTrue(success);
    }

    @Test
    public void testCheckStatusNoEvent() throws Exception {
        playlistMonitor.processResponseStatus(RESPONSE + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.addPlaylistChangeListener(new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = true;
            }
        });
        playlistMonitor.checkStatus();
        assertFalse(success);
    }

    @Test
    public void testRemoveListener() throws Exception {
        PlaylistBasicChangeListener playlistBasicChangeListener = new PlaylistBasicChangeListener() {

            @Override
            public void playlistBasicChange(PlaylistBasicChangeEvent event) {
                success = true;
            }
        };

        playlistMonitor.addPlaylistChangeListener(playlistBasicChangeListener);
        playlistMonitor.processResponseStatus(RESPONSE + "1");
        playlistMonitor.checkStatus();
        playlistMonitor.processResponseStatus(RESPONSE + "2");
        playlistMonitor.checkStatus();

        assertTrue(success);

        success = false;

        playlistMonitor.removePlaylistStatusChangedListener(playlistBasicChangeListener);
        playlistMonitor.processResponseStatus(RESPONSE + "1");
        playlistMonitor.checkStatus();
        assertFalse(success);
    }
}
