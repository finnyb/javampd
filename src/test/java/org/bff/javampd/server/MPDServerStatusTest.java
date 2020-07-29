package org.bff.javampd.server;

import org.bff.javampd.Clock;
import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MPDServerStatusTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @Mock
    private Clock clock;

    private MPDServerStatus serverStatus;

    private List<String> statusList;

    @BeforeEach
    public void setUp() {
        statusList = new ArrayList<>();
        when(clock.min()).thenReturn(LocalDateTime.MIN);
        serverStatus = new MPDServerStatus(properties, commandExecutor, clock);
    }

    @Test
    public void testLookupStatus() {
        assertEquals(Status.VOLUME, Status.lookupStatus("volume:"));
    }

    @Test
    public void testLookupUnknownStatus() {
        assertEquals(Status.UNKNOWN, Status.lookupStatus("bogus:"));
    }

    @Test
    public void testGetPlaylistVersion() {
        String version = "5";
        statusList.add("playlist: " + version);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(version), serverStatus.getPlaylistVersion());
    }

    @Test
    public void testInvalidPlaylistVersion() {
        statusList.add("playlist: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    public void testEmptyPlaylistVersion() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    public void testGetState() {
        String state = "state";
        statusList.add("state: " + state);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(state, serverStatus.getState());
    }

    @Test
    public void testGetXFade() {
        String xfade = "5";
        statusList.add("xfade: " + xfade);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(xfade), serverStatus.getXFade());
    }

    @Test
    public void testInvalidXFade() {
        statusList.add("xfade: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testEmptyXFade() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testGetAudio() {
        String audio = "audio";
        statusList.add("audio: " + audio);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(audio, serverStatus.getAudio());
    }

    @Test
    public void testIsError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isError());
    }

    @Test
    public void testGetError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(error, serverStatus.getError());
    }

    @Test
    public void testGetElapsedTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[0]), serverStatus.getElapsedTime());
    }

    @Test
    public void testInvalidElapsedTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testEmptyElapsedTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testElapsedTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testTotalTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void getTotalTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[1]), serverStatus.getTotalTime());
    }

    @Test
    public void testInvalidTotalTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    public void testEmptyTotalTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    public void testGetBitrate() {
        String bitrate = "5";
        statusList.add("bitrate: " + bitrate);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(bitrate), serverStatus.getBitrate());
    }

    @Test
    public void testInvalidBitrate() {
        statusList.add("bitrate: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    public void testEmptyBitrate() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    public void testGetVolume() {
        String volume = "5";
        statusList.add("volume: " + volume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(volume), serverStatus.getVolume());
    }

    @Test
    public void testInvalidVolume() {
        statusList.add("volume: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    public void testEmptyVolume() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    public void testIsRepeat() {
        String repeat = "1";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isRepeat());
    }

    @Test
    public void testIsNotRepeat() {
        String repeat = "0";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isRepeat());
    }

    @Test
    public void testIsDatabaseUpdating() {
        String updating = "anything";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isDatabaseUpdating());
    }

    @Test
    public void testIsDatabaseUpdatingFalse() {
        String updating = "";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isDatabaseUpdating());
    }

    @Test
    public void testIsRandom() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isRandom());
    }

    @Test
    public void testIsNotRandom() {
        String random = "0";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isRandom());
    }

    @Test
    public void testInsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.isRandom();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(1)).sendCommand(properties.getStatus());
    }

    @Test
    public void testOutsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(5));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    public void testSetExpiry() {
        long interval = 1;
        serverStatus.setExpiryInterval(interval);
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(interval * 2));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    public void testForceUpdate() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        serverStatus.forceUpdate();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    public void testGetStatus() {
        String random = "1";
        String volume = "5";
        statusList.add("volume: " + volume);
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        serverStatus.isRandom();
        serverStatus.forceUpdate();
        serverStatus.isRandom();

        assertEquals(serverStatus.getStatus().size(), statusList.size());
    }
}