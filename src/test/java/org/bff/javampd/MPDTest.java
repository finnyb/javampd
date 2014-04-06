package org.bff.javampd;

import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDTest {
    @Mock
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private ServerProperties serverProperties;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;

    @InjectMocks
    private MPD mpd;

    @Test
    public void testClearError() throws Exception {
        when(serverProperties.getClearError()).thenReturn(new ServerProperties().getClearError());
        when(mpdCommandExecutor.sendCommand(serverProperties.getClearError())).thenReturn(new ArrayList<String>());

        mpd.clearerror();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(serverProperties.getClearError(), commandArgumentCaptor.getValue());
    }

    @Test
    public void testClose() throws Exception {
        when(serverProperties.getClose()).thenReturn(new ServerProperties().getClose());
        when(mpdCommandExecutor.sendCommand(serverProperties.getClose())).thenReturn(new ArrayList<String>());

        mpd.close();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals(serverProperties.getClose(), commandArgumentCaptor.getValue());
    }

    @Test
    public void testGetVersion() throws Exception {
        when(mpdCommandExecutor.getMPDVersion()).thenReturn("mpdVersion");
        assertEquals("mpdVersion", mpd.getVersion());
    }

    @Test
    public void testIsConnected() throws Exception {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing())).thenReturn(new ArrayList<String>());
        assertTrue(mpd.isConnected());
    }

    @Test
    public void testNotConnected() throws Exception {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing())).thenThrow(new MPDResponseException());
        assertFalse(mpd.isConnected());
    }

    @Test
    public void testAuthenticate() throws Exception {
        when(serverProperties.getPassword()).thenReturn(new ServerProperties().getPassword());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPassword())).thenReturn(new ArrayList<String>());
        mpd.authenticate("thepassword");
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture(), commandArgumentCaptor.capture());

        assertEquals(serverProperties.getPassword(), commandArgumentCaptor.getAllValues().get(0));
        assertEquals("thepassword", commandArgumentCaptor.getAllValues().get(1));
    }

    @Test
    public void testGetServerStatistics() throws Exception {
        assertNotNull(mpd.getServerStatistics());
    }

    @Test
    public void testGetServerStatus() throws Exception {
        assertNotNull(mpd.getServerStatus());
    }

    @Test
    public void testGetPlayer() throws Exception {
        assertNotNull(mpd.getPlayer());
    }

    @Test
    public void testGetPlaylist() throws Exception {
        assertNotNull(mpd.getPlaylist());
    }

    @Test
    public void testGetAdmin() throws Exception {
        assertNotNull(mpd.getAdmin());
    }

    @Test
    public void testGetDatabase() throws Exception {
        assertNotNull(mpd.getDatabase());
    }

    @Test
    public void testGetStandaloneMonitor() throws Exception {
        assertNotNull(mpd.getMonitor());
    }

    @Test
    public void testPort() {
        assertEquals(6600, mpd.getPort());
    }

    @Test
    public void testAddress() throws UnknownHostException {
        assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
    }

    @Test
    public void testTimeout() {
        assertEquals(0, mpd.getTimeout());
    }
}
