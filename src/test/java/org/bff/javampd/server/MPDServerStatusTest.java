package org.bff.javampd.server;

import org.bff.javampd.Clock;
import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import org.joda.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDServerStatusTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @Mock
    private Clock clock;

    private MPDServerStatus serverStatus;

    private List<String> statusList;

    @Before
    public void setUp() throws Exception {
        statusList = new ArrayList<>();
        when(clock.min()).thenReturn(LocalDateTime.fromDateFields(new Date(0)));
        serverStatus = new MPDServerStatus(properties, commandExecutor, clock);

        when(properties.getStats()).thenReturn(new ServerProperties().getStatus());
        when(clock.now()).thenReturn(LocalDateTime.now());
    }

    @Test
    public void testLookupStatus() throws Exception {
        assertEquals(Status.VOLUME, Status.lookupStatus("volume:"));
    }

    @Test
    public void testLookupUnknownStatus() throws Exception {
        assertEquals(Status.UNKNOWN, Status.lookupStatus("bogus:"));
    }

    @Test
    public void testGetPlaylistVersion() throws Exception {
        String version = "5";
        statusList.add("playlist: " + version);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(version), serverStatus.getPlaylistVersion());
    }

    @Test
    public void testInvalidPlaylistVersion() {
        statusList.add("playlist: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    public void testEmptyPlaylistVersion() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getPlaylistVersion());
    }

    @Test
    public void testGetState() throws Exception {
        String state = "state";
        statusList.add("state: " + state);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(state, serverStatus.getState());
    }

    @Test
    public void testGetXFade() throws Exception {
        String xfade = "5";
        statusList.add("xfade: " + xfade);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(xfade), serverStatus.getXFade());
    }

    @Test
    public void testInvalidXFade() {
        statusList.add("xfade: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testEmptyXFade() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getXFade());
    }

    @Test
    public void testGetAudio() throws Exception {
        String audio = "audio";
        statusList.add("audio: " + audio);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(audio, serverStatus.getAudio());
    }

    @Test
    public void testIsError() throws Exception {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertTrue(serverStatus.isError());
    }

    @Test
    public void testGetError() throws Exception {
        String error = "true";
        statusList.add("error: " + error);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(error, serverStatus.getError());
    }

    @Test
    public void testGetElapsedTime() throws Exception {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(time.split(":")[0]), serverStatus.getElapsedTime());
    }

    @Test
    public void testInvalidElapsedTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testEmptyElapsedTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testElapsedTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void testTotalTimeParseException() {
        statusList.add("time: junk:junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getElapsedTime());
    }

    @Test
    public void getTotalTime() throws Exception {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(time.split(":")[1]), serverStatus.getTotalTime());
    }

    @Test
    public void testInvalidTotalTime() {
        statusList.add("time: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    public void testEmptyTotalTime() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getTotalTime());
    }

    @Test
    public void testGetBitrate() throws Exception {
        String bitrate = "5";
        statusList.add("bitrate: " + bitrate);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(bitrate), serverStatus.getBitrate());
    }

    @Test
    public void testInvalidBitrate() {
        statusList.add("bitrate: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    public void testEmptyBitrate() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getBitrate());
    }

    @Test
    public void testGetVolume() {
        String volume = "5";
        statusList.add("volume: " + volume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(volume), serverStatus.getVolume());
    }

    @Test
    public void testInvalidVolume() {
        statusList.add("volume: junk");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    public void testEmptyVolume() {
        statusList.add("junk: 0");
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(0, serverStatus.getVolume());
    }

    @Test
    public void testIsRepeat() {
        String repeat = "1";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertTrue(serverStatus.isRepeat());
    }

    @Test
    public void testIsNotRepeat() {
        String repeat = "0";
        statusList.add("repeat: " + repeat);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertFalse(serverStatus.isRepeat());
    }

    @Test
    public void testIsDatabaseUpdating() {
        String updating = "anything";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertTrue(serverStatus.isDatabaseUpdating());
    }

    @Test
    public void testIsDatabaseUpdatingFalse() {
        String updating = "";
        statusList.add("updating_db: " + updating);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertFalse(serverStatus.isDatabaseUpdating());
    }

    @Test
    public void testIsRandom() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertTrue(serverStatus.isRandom());
    }

    @Test
    public void testIsNotRandom() {
        String random = "0";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertFalse(serverStatus.isRandom());
    }

    @Test
    public void testInsideDefaultExpiry() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        serverStatus.isRandom();
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(1)).sendCommand(properties.getStatus());
    }

    @Test
    public void testOutsideDefaultExpiry() throws InterruptedException {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes(5));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    public void testSetExpiry() throws InterruptedException {
        long interval = 1;
        serverStatus.setExpiryInterval(interval);
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
        serverStatus.isRandom();
        when(clock.now()).thenReturn(LocalDateTime.now().plusMinutes((int) interval * 2));
        serverStatus.isRandom();
        Mockito.verify(commandExecutor, times(2)).sendCommand(properties.getStatus());
    }

    @Test
    public void testForceUpdate() {
        String random = "1";
        statusList.add("random: " + random);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);
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
        serverStatus.isRandom();
        serverStatus.forceUpdate();
        serverStatus.isRandom();

        Collection status = serverStatus.getStatus();

        assertEquals(status.size(), statusList.size());
    }
}