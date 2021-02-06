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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDServerStatusTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @Mock
    private Clock clock;

    private MPDServerStatus serverStatus;

    private List<String> statusList;

    @BeforeEach
    void setUp() {
        statusList = new ArrayList<>();
        when(clock.min()).thenReturn(LocalDateTime.MIN);
        serverStatus = new MPDServerStatus(properties, commandExecutor, clock);
    }

    @Test
    void testLookupStatus() {
        assertEquals(Status.VOLUME, Status.lookupStatus("volume:"));
    }

    @Test
    void testLookupUnknownStatus() {
        assertEquals(Status.UNKNOWN, Status.lookupStatus("bogus:"));
    }

    @Test
    void testGetPlaylistVersion() {
        String version = "5";
        statusList.add("playlist: " + version);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(version), serverStatus.getPlaylistVersion());
    }

    @Test
    void testInvalidPlaylistVersion() {
        statusList.add("playlist: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    void testEmptyPlaylistVersion() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    void testGetState() {
        String state = "state";
        statusList.add("state: " + state);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(state, serverStatus.getState());
    }

    @Test
    void testGetXFade() {
        String xfade = "5";
        statusList.add("xfade: " + xfade);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(xfade), serverStatus.getXFade());
    }

    @Test
    void testInvalidXFade() {
        statusList.add("xfade: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    void testEmptyXFade() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());
        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    void testGetAudio() {
        String audio = "audio";
        statusList.add("audio: " + audio);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(audio, serverStatus.getAudio());
    }

    @Test
    void testIsError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isError());
    }

    @Test
    void testGetError() {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(error, serverStatus.getError());
    }

    @Test
    void testGetElapsedTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[0]), serverStatus.getElapsedTime());
    }

    @Test
    void testInvalidElapsedTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    void testEmptyElapsedTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    void testElapsedTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    void testTotalTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    void getTotalTime() {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(time.split(":")[1]), serverStatus.getTotalTime());
    }

    @Test
    void testInvalidTotalTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    void testEmptyTotalTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    void testGetBitrate() {
        String bitrate = "5";
        statusList.add("bitrate: " + bitrate);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(bitrate), serverStatus.getBitrate());
    }

    @Test
    void testInvalidBitrate() {
        statusList.add("bitrate: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    void testEmptyBitrate() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    void testGetVolume() {
        String volume = "5";
        statusList.add("volume: " + volume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(Integer.parseInt(volume), serverStatus.getVolume());
    }

    @Test
    void testInvalidVolume() {
        statusList.add("volume: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    void testEmptyVolume() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    void testIsRepeat() {
        String repeat = "1";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isRepeat());
    }

    @Test
    void testIsNotRepeat() {
        String repeat = "0";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isRepeat());
    }

    @Test
    void testIsDatabaseUpdating() {
        String updating = "anything";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isDatabaseUpdating());
    }

    @Test
    void testIsDatabaseUpdatingFalse() {
        String updating = "";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isDatabaseUpdating());
    }

    @Test
    void testIsRandom() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertTrue(serverStatus.isRandom());
    }

    @Test
    void testIsNotRandom() {
        String random = "0";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        assertFalse(serverStatus.isRandom());
    }

    @Test
    void testInsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        when(clock.now()).thenReturn(LocalDateTime.now());

        serverStatus.isRandom();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(1)).sendCommand(properties.getStatus());
    }

    @Test
    void testOutsideDefaultExpiry() {
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
    void testSetExpiry() {
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
    void testForceUpdate() {
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
    void testGetStatus() {
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
