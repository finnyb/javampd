package org.bff.javampd.server;

import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MPDTest {

    @Mock
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private ServerProperties serverProperties;

    @InjectMocks
    private MPD.Builder mpdBuilder;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;

    @Test
    public void testClearError() throws Exception {
        when(serverProperties.getClearError()).thenReturn(new ServerProperties().getClearError());

        MPD mpd = mpdBuilder.build();
        mpd.clearError();

        verify(mpdCommandExecutor)
                .sendCommand(commandArgumentCaptor.capture());
        assertEquals(serverProperties.getClearError(), commandArgumentCaptor.getAllValues().get(0));
    }

    @Test
    public void testClose() throws Exception {
        when(serverProperties.getClearError()).thenReturn(new ServerProperties().getClose());

        MPD mpd = mpdBuilder.build();
        mpd.close();

        verify(mpdCommandExecutor)
                .sendCommand(commandArgumentCaptor.capture());
        assertEquals(serverProperties.getClose(), commandArgumentCaptor.getAllValues().get(0));
    }

    @Test
    public void testIsClosed() {
        MPD mpd = mpdBuilder.build();
        assertFalse(mpd.isClosed());
    }

    @Test
    public void testIsNotClosed() {
        MPD mpd = mpdBuilder.build();
        mpd.close();
        assertTrue(mpd.isClosed());
    }

    @Test
    public void testGetVersion() throws Exception {
        String theVersion = "testVersion";
        when(mpdCommandExecutor.getMPDVersion())
                .thenReturn(theVersion);

        MPD mpd = mpdBuilder.build();
        assertEquals(theVersion, mpd.getVersion());
    }

    @Test
    public void testIsConnected() throws Exception {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing()))
                .thenReturn(new ArrayList<>());

        MPD mpd = mpdBuilder.build();
        assertTrue(mpd.isConnected());
    }

    @Test
    public void testIsNotConnected() throws Exception {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing()))
                .thenThrow(new MPDConnectionException());

        MPD mpd = mpdBuilder.build();
        assertFalse(mpd.isConnected());
    }

    @Test
    public void testAuthenticate() throws Exception {
        String password = "password";
        when(serverProperties.getPassword()).thenReturn(new ServerProperties().getPassword());
        when(mpdCommandExecutor
                .sendCommand(serverProperties.getPassword(), password))
                .thenReturn(new ArrayList<>());

        MPD mpd = mpdBuilder.build();
    }

    @Test(expected = MPDConnectionException.class)
    public void testFailedAuthenticate() throws Exception {
        String password = "password";
        when(serverProperties.getPassword()).thenReturn(new ServerProperties().getPassword());
        doThrow(new MPDSecurityException("incorrect password"))
                .when(mpdCommandExecutor)
                .authenticate();

        MPD mpd = mpdBuilder.password(password).build();
    }

    @Test
    public void testGetPort() throws Exception {
        int port = 666;
        MPD mpd = mpdBuilder.port(port).build();

        assertEquals(666, mpd.getPort());
    }

    @Test
    public void testGetDefaultPort() throws Exception {
        MPD mpd = mpdBuilder.build();

        assertEquals(6600, mpd.getPort());
    }

    @Test
    public void testGetAddress() throws Exception {
        String address = "localhost";
        MPD mpd = mpdBuilder.server(address).build();

        assertEquals(InetAddress.getByName(address), mpd.getAddress());
    }

    @Test
    public void testGetDefaultAddress() throws Exception {
        MPD mpd = mpdBuilder.build();

        assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
    }

    @Test
    public void testGetTimeout() throws Exception {
        int timeOut = 666;
        MPD mpd = mpdBuilder.timeout(timeOut).build();

        assertEquals(timeOut, mpd.getTimeout());
    }

    @Test
    public void testGetDefaultTimeout() throws Exception {
        MPD mpd = mpdBuilder.build();

        assertEquals(0, mpd.getTimeout());
    }

    @Test
    public void testGetPlayer() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getPlayer());
    }

    @Test
    public void testGetPlaylist() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getPlaylist());
    }

    @Test
    public void testGetAdmin() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getAdmin());
    }

    @Test
    public void testGetMusicDatabase() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getMusicDatabase());
    }

    @Test
    public void testGetServerStatistics() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getServerStatistics());
    }

    @Test
    public void testGetServerStatus() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getServerStatus());
    }

    @Test
    public void testGetMonitor() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getMonitor());
    }

    @Test
    public void testGetSongSearcher() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getSongSearcher());
    }

    @Test
    public void testCommandExecutor() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getCommandExecutor());
    }
}