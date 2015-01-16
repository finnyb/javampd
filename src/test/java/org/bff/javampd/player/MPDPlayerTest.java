package org.bff.javampd.player;

import org.bff.javampd.MPDException;
import org.bff.javampd.SongTestHelper;
import org.bff.javampd.audioinfo.MPDAudioInfo;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.MPDConnectionException;
import org.bff.javampd.server.ServerStatus;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.song.MPDSongConverter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDPlayerTest {

    @Mock
    private ServerStatus serverStatus;
    @Mock
    private CommandExecutor commandExecutor;
    @Captor
    private ArgumentCaptor<String> commandCaptorString;
    @Captor
    private ArgumentCaptor<Integer> commandCaptorInteger;

    private PlayerProperties playerProperties;

    private SongTestHelper songTestHelper;

    private MPDPlayer player;

    @Before
    public void before() {
        songTestHelper = new SongTestHelper();
        playerProperties = new PlayerProperties();
        player = new MPDPlayer(serverStatus,
                playerProperties,
                commandExecutor,
                new MPDSongConverter());
    }

    @Test
    public void testGetCurrentSong() throws Exception {
        when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
                .thenReturn(songTestHelper.createResponse());

        songTestHelper.assertEquality(player.getCurrentSong());
    }

    @Test
    public void testGetCurrentSongNone() throws Exception {
        when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
                .thenReturn(new ArrayList<>());

        assertNull(player.getCurrentSong());
    }

    @Test(expected = MPDException.class)
    public void testGetCurrentSongResponseException() throws Exception {
        when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
                .thenThrow(new MPDConnectionException());

        player.getCurrentSong();
    }

    @Test(expected = RuntimeException.class)
    public void testGetCurrentSongGeneralException() throws Exception {
        when(commandExecutor.sendCommand(playerProperties.getCurrentSong()))
                .thenThrow(new RuntimeException());

        player.getCurrentSong();
    }

    @Test
    public void testAddPlayerChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        PlayerChangeListener pcl = event -> gotEvent.set(true);

        player.addPlayerChangeListener(pcl);
        player.firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testRemovePlayerChangedListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        PlayerChangeListener pcl = event -> gotEvent.set(true);

        player.addPlayerChangeListener(pcl);
        player.firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
        assertTrue(gotEvent.get());

        gotEvent.set(false);
        player.removePlayerChangedListener(pcl);
        player.firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
        assertFalse(gotEvent.get());
    }

    @Test
    public void testFirePlayerChangeEvent() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        PlayerChangeListener pcl = event -> gotEvent.set(true);

        player.addPlayerChangeListener(pcl);
        player.firePlayerChangeEvent(PlayerChangeEvent.Event.PLAYER_NEXT);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testAddVolumeChangeListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        VolumeChangeListener vcl = event -> gotEvent.set(true);

        player.addVolumeChangeListener(vcl);
        player.fireVolumeChangeEvent(1);
        assertTrue(gotEvent.get());
    }

    @Test
    public void testRemoveVolumeChangedListener() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        VolumeChangeListener vcl = event -> gotEvent.set(true);

        player.addVolumeChangeListener(vcl);
        player.fireVolumeChangeEvent(1);
        assertTrue(gotEvent.get());

        gotEvent.set(false);
        player.removeVolumeChangedListener(vcl);
        player.fireVolumeChangeEvent(1);
        assertFalse(gotEvent.get());
    }

    @Test
    public void testFireVolumeChangeEvent() throws Exception {
        AtomicBoolean gotEvent = new AtomicBoolean(false);

        VolumeChangeListener vcl = event -> gotEvent.set(true);

        player.addVolumeChangeListener(vcl);
        player.fireVolumeChangeEvent(1);
        assertTrue(gotEvent.get());
    }


    @Test
    public void testPlay() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getPlay()))
                .thenReturn(new ArrayList<>());

        player.play();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture());
        assertEquals(playerProperties.getPlay(), commandCaptorString.getValue());
    }

    @Test
    public void testPlayId() throws Exception {
        int id = 666;

        when(commandExecutor
                .sendCommand(playerProperties.getPlayId(), id))
                .thenReturn(new ArrayList<>());

        MPDSong song = new MPDSong("file", "title");
        song.setId(id);
        player.playId(song);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());

        assertEquals(playerProperties.getPlayId(), commandCaptorString.getValue());
        assertEquals(id, (int) commandCaptorInteger.getValue());
    }

    @Test
    public void testSeek() throws Exception {
        int id = 666;
        long seekTime = 667;

        player = new MPDPlayer(serverStatus,
                playerProperties,
                commandExecutor,
                new MPDSongConverter()) {

            @Override
            public MPDSong getCurrentSong() {
                MPDSong song = new MPDSong("file", "title");
                song.setId(id);
                song.setLength(Integer.MAX_VALUE);

                return song;
            }
        };

        when(commandExecutor
                .sendCommand(playerProperties.getPlay()))
                .thenReturn(new ArrayList<>());

        player.seek(seekTime);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorString.capture(),
                        commandCaptorString.capture());

        assertEquals(playerProperties.getSeekId(), commandCaptorString.getAllValues().get(0));
        assertEquals(Integer.toString(id), commandCaptorString.getAllValues().get(1));
        assertEquals(Long.toString(seekTime), commandCaptorString.getAllValues().get(2));
    }

    @Test
    public void testSeekId() throws Exception {
        int id = 666;
        long seekTime = 667;

        when(commandExecutor
                .sendCommand(playerProperties.getSeekId(),
                        Integer.toString(id),
                        Long.toString(seekTime)))
                .thenReturn(new ArrayList<>());

        MPDSong song = new MPDSong("file", "title");
        song.setId(id);
        song.setLength(Integer.MAX_VALUE);
        player.seekId(song, seekTime);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorString.capture(),
                        commandCaptorString.capture());
        assertEquals(playerProperties.getSeekId(), commandCaptorString.getAllValues().get(0));
        assertEquals(Integer.toString(id), commandCaptorString.getAllValues().get(1));
        assertEquals(Long.toString(seekTime), commandCaptorString.getAllValues().get(2));
    }


    @Test
    public void testStop() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getStop()))
                .thenReturn(new ArrayList<>());

        player.stop();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture());
        assertEquals(playerProperties.getStop(), commandCaptorString.getValue());
    }

    @Test
    public void testPause() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getPause()))
                .thenReturn(new ArrayList<>());

        player.pause();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture());
        assertEquals(playerProperties.getPause(), commandCaptorString.getValue());
    }


    @Test
    public void testPlayNext() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getNext()))
                .thenReturn(new ArrayList<>());

        player.playNext();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture());
        assertEquals(playerProperties.getNext(), commandCaptorString.getValue());
    }

    @Test
    public void testPlayPrev() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getPrevious()))
                .thenReturn(new ArrayList<>());

        player.playPrev();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture());
        assertEquals(playerProperties.getPrevious(), commandCaptorString.getValue());
    }

    @Test
    public void testMute() throws Exception {
        when(commandExecutor
                .sendCommand(playerProperties.getSetVolume()))
                .thenReturn(new ArrayList<>());

        player.mute();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());
        assertEquals(playerProperties.getSetVolume(), commandCaptorString.getValue());
        assertEquals(0, (int) commandCaptorInteger.getValue());
    }

    @Test
    public void testUnMute() throws Exception {
        int oldVolume = 50;
        when(commandExecutor
                .sendCommand(playerProperties.getSetVolume()))
                .thenReturn(new ArrayList<>());
        when(serverStatus.getVolume()).thenReturn(oldVolume);

        player.mute();
        player.unMute();

        verify(commandExecutor, times(2))
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());

        assertEquals(playerProperties.getSetVolume(), commandCaptorString.getValue());
        assertEquals(oldVolume, (int) commandCaptorInteger.getValue());
    }

    @Test
    public void testGetVolume() throws Exception {
        int volume = 50;
        when(serverStatus.getVolume()).thenReturn(volume);

        assertEquals(volume, player.getVolume());
    }

    @Test
    public void testSetVolume() throws Exception {
        int volume = 50;
        when(commandExecutor
                .sendCommand(playerProperties.getSetVolume()))
                .thenReturn(new ArrayList<>());

        player.setVolume(volume);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());
        assertEquals(playerProperties.getSetVolume(), commandCaptorString.getValue());
        assertEquals(volume, (int) commandCaptorInteger.getValue());
    }


    @Test
    public void testVolumeTooLow() {
        player.setVolume(-1);
        verify(commandExecutor, never())
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());
    }

    @Test
    public void testVolumeTooHigh() {
        player.setVolume(101);
        verify(commandExecutor, never())
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());
    }

    @Test
    public void testGetBitrate() throws Exception {
        int bitrate = 50;
        when(serverStatus.getBitrate()).thenReturn(bitrate);

        assertEquals(bitrate, player.getBitrate());
    }

    @Test
    public void testIsRepeat() throws Exception {
        when(serverStatus.isRepeat()).thenReturn(true);
        assertTrue(player.isRepeat());
    }

    @Test
    public void testSetRepeatOn() throws Exception {
        testRepeat(true);
    }

    @Test
    public void testSetRepeatOff() throws Exception {
        testRepeat(false);
    }

    private void testRepeat(boolean shouldRepeat) throws Exception {
        String repeatParam = shouldRepeat ? "1" : "0";
        when(commandExecutor
                .sendCommand(playerProperties.getRepeat(), repeatParam))
                .thenReturn(new ArrayList<>());

        player.setRepeat(shouldRepeat);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorString.capture());
        assertEquals(playerProperties.getRepeat(), commandCaptorString.getAllValues().get(0));
        assertEquals(repeatParam, commandCaptorString.getAllValues().get(1));
    }

    @Test
    public void testIsRandom() throws Exception {
        when(serverStatus.isRandom()).thenReturn(true);
        assertTrue(player.isRandom());
    }

    @Test
    public void testRandomizePlay() throws Exception {
        String randomParam = "1";

        when(commandExecutor
                .sendCommand(playerProperties.getRandom(), randomParam))
                .thenReturn(new ArrayList<>());

        player.randomizePlay();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorString.capture());
        assertEquals(playerProperties.getRandom(), commandCaptorString.getAllValues().get(0));
        assertEquals(randomParam, commandCaptorString.getAllValues().get(1));
    }

    @Test
    public void testUnRandomizePlay() throws Exception {
        String randomParam = "0";

        when(commandExecutor
                .sendCommand(playerProperties.getRandom(), randomParam))
                .thenReturn(new ArrayList<>());

        player.unRandomizePlay();

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorString.capture());
        assertEquals(playerProperties.getRandom(), commandCaptorString.getAllValues().get(0));
        assertEquals(randomParam, commandCaptorString.getAllValues().get(1));
    }

    @Test
    public void testGetXFade() throws Exception {
        int xFade = 666;
        when(serverStatus.getXFade()).thenReturn(xFade);

        assertEquals(xFade, player.getXFade());
    }

    @Test
    public void testSetXFade() throws Exception {
        int xFade = 666;

        when(commandExecutor
                .sendCommand(playerProperties.getXFade(), xFade))
                .thenReturn(new ArrayList<>());

        player.setXFade(xFade);

        verify(commandExecutor)
                .sendCommand(commandCaptorString.capture(),
                        commandCaptorInteger.capture());
        assertEquals(playerProperties.getXFade(), commandCaptorString.getValue());
        assertEquals(xFade, (int) commandCaptorInteger.getValue());
    }

    @Test
    public void testGetElapsedTime() throws Exception {
        long elapsedTime = 666;
        when(serverStatus.getElapsedTime()).thenReturn(elapsedTime);

        assertEquals(elapsedTime, player.getElapsedTime());
    }

    @Test
    public void testGetTotalTime() throws Exception {
        long totalTime = 666;
        when(serverStatus.getTotalTime()).thenReturn(totalTime);

        assertEquals(totalTime, player.getTotalTime());
    }

    @Test
    public void testGetAudioDetails() throws Exception {
        int sample = 1;
        int bitrate = 2;
        int channels = 3;

        when(serverStatus.getAudio()).thenReturn(sample + ":" + bitrate + ":" + channels);

        MPDAudioInfo details = player.getAudioDetails();

        assertEquals(details.getBits(), bitrate);
        assertEquals(details.getSampleRate(), sample);
        assertEquals(details.getChannels(), channels);
    }

    @Test
    public void testGetStatusPlaying() throws Exception {
        testStatus(Player.Status.STATUS_PLAYING);
    }

    @Test
    public void testGetStatusPaused() throws Exception {
        testStatus(Player.Status.STATUS_PAUSED);
    }

    @Test
    public void testGetStatusStopped() throws Exception {
        testStatus(Player.Status.STATUS_STOPPED);
    }

    private void testStatus(Player.Status status) throws Exception {
        when(serverStatus.getState()).thenReturn(status.getPrefix());

        assertEquals(status, player.getStatus());
    }
}