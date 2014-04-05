package org.bff.javampd;

import org.bff.javampd.exception.MPDResponseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDTest {
    @Mock
    private MPDCommandExecutor mpdCommandExecutor;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;

    @InjectMocks
    private MPD mpd;

    @Test
    public void testClearError() throws Exception {
        when(mpdCommandExecutor.sendCommand("clearerror")).thenReturn(new ArrayList<String>());

        mpd.clearerror();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals("clearerror", commandArgumentCaptor.getValue());
    }

    @Test
    public void testClose() throws Exception {
        when(mpdCommandExecutor.sendCommand("close")).thenReturn(new ArrayList<String>());

        mpd.close();
        verify(mpdCommandExecutor).sendCommand(commandArgumentCaptor.capture());
        assertEquals("close", commandArgumentCaptor.getValue());
    }

    @Test
    public void testGetVersion() throws Exception {
        when(mpdCommandExecutor.getMPDVersion()).thenReturn("mpdVersion");
        assertEquals("mpdVersion", mpd.getVersion());
    }

    @Test
    public void testIsConnected() throws Exception {
        when(mpdCommandExecutor.sendCommand("ping")).thenReturn(new ArrayList<String>());
        assertTrue(mpd.isConnected());
    }

    @Test
    public void testNotConnected() throws Exception {
        when(mpdCommandExecutor.sendCommand("ping")).thenThrow(new MPDResponseException());
        assertFalse(mpd.isConnected());
    }

    @Test
    public void testGetServerStatistics() throws Exception {
        assertNotNull(mpd.getServerStatistics());
    }

    @Test
    public void testGetServerStatus() throws Exception {
        assertNotNull(mpd.getServerStatus());
    }
}
