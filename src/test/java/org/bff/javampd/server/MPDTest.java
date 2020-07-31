package org.bff.javampd.server;

import org.bff.javampd.command.MPDCommandExecutor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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
    public void testClearError() {
        when(serverProperties.getClearError()).thenReturn(new ServerProperties().getClearError());

        MPD mpd = mpdBuilder.build();
        mpd.clearError();

        verify(mpdCommandExecutor)
                .sendCommand(commandArgumentCaptor.capture());
        assertEquals(serverProperties.getClearError(), commandArgumentCaptor.getAllValues().get(0));
    }

    @Test
    public void testClose() {
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
    public void testGetVersion() {
        String theVersion = "testVersion";
        when(mpdCommandExecutor.getMPDVersion())
                .thenReturn(theVersion);

        MPD mpd = mpdBuilder.build();
        assertEquals(theVersion, mpd.getVersion());
    }

    @Test
    public void testIsConnected() {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing()))
                .thenReturn(new ArrayList<>());

        MPD mpd = mpdBuilder.build();
        assertTrue(mpd.isConnected());
    }

    @Test
    public void testIsNotConnected() {
        when(serverProperties.getPing()).thenReturn(new ServerProperties().getPing());
        when(mpdCommandExecutor.sendCommand(serverProperties.getPing()))
                .thenThrow(new MPDConnectionException());

        MPD mpd = mpdBuilder.build();
        assertFalse(mpd.isConnected());
    }

    @Test
    public void testAuthenticate() {
        mpdBuilder.build();
    }

    @Test
    public void testFailedAuthenticate() {
        String password = "password";
        doThrow(new MPDSecurityException("incorrect password"))
                .when(mpdCommandExecutor)
                .authenticate();

        assertThrows(MPDConnectionException.class, () -> mpdBuilder.password(password).build());
    }

    @Test
    public void testGetPort() {
        int port = 666;
        MPD mpd = mpdBuilder.port(port).build();

        assertEquals(666, mpd.getPort());
    }

    @Test
    public void testGetDefaultPort() {
        MPD mpd = mpdBuilder.build();

        assertEquals(6600, mpd.getPort());
    }

    @Test
    public void testGetAddress() throws UnknownHostException {
        String address = "localhost";
        MPD mpd = mpdBuilder.server(address).build();

        assertEquals(InetAddress.getByName(address), mpd.getAddress());
    }

    @Test
    public void testGetDefaultAddress() throws UnknownHostException {
        MPD mpd = mpdBuilder.build();

        assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
    }

    @Test
    public void testGetTimeout() {
        int timeOut = 666;
        MPD mpd = mpdBuilder.timeout(timeOut).build();

        assertEquals(timeOut, mpd.getTimeout());
    }

    @Test
    public void testGetDefaultTimeout() {
        MPD mpd = mpdBuilder.build();

        assertEquals(0, mpd.getTimeout());
    }

    @Test
    public void testGetPlayer() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getPlayer());
    }

    @Test
    public void testGetPlaylist() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getPlaylist());
    }

    @Test
    public void testGetAdmin() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getAdmin());
    }

    @Test
    public void testGetMusicDatabase() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getMusicDatabase());
    }

    @Test
    public void testGetServerStatistics() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getServerStatistics());
    }

    @Test
    public void testGetServerStatus() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getServerStatus());
    }

    @Test
    public void testGetMonitor() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getMonitor());
    }

    @Test
    public void testGetSongSearcher() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getSongSearcher());
    }

    @Test
    public void testCommandExecutor() {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd.getCommandExecutor());
    }
}