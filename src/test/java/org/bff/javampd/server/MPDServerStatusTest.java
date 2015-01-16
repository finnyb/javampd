package org.bff.javampd.server;

import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDServerStatusTest {

    @Mock
    private MPDCommandExecutor commandExecutor;

    @Mock
    private ServerProperties properties;

    @InjectMocks
    private MPDServerStatus serverStatus;

    private List<String> statusList;

    @Before
    public void setUp() throws Exception {
        statusList = new ArrayList<>();
        when(properties.getStats()).thenReturn(new ServerProperties().getStatus());
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
    public void getTotalTime() throws Exception {
        String time = "5:6";
        statusList.add("time: " + time);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(time.split(":")[1]), serverStatus.getTotalTime());
    }

    @Test
    public void testGetBitrate() throws Exception {
        String bitrate = "5";
        statusList.add("bitrate: " + bitrate);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(bitrate), serverStatus.getBitrate());
    }

    @Test
    public void testGetVolume() {
        String volume = "5";
        statusList.add("volume: " + volume);
        when(commandExecutor.sendCommand(properties.getStatus())).thenReturn(statusList);

        assertEquals(Integer.parseInt(volume), serverStatus.getVolume());
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
}