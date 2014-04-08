package org.bff.javampd;

import org.bff.javampd.properties.ServerProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.InetAddress;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BuilderTest {

    @Mock
    private MPDCommandExecutor mpdCommandExecutor;

    @Mock
    private ServerProperties serverProperties;

    @InjectMocks
    private MPD.Builder mpdBuilder;

    @Captor
    private ArgumentCaptor<String> commandArgumentCaptor;

    private static final int DEFAULT_PORT = 6600;
    private static final int DEFAULT_TIMEOUT = 0;
    private static final String DEFAULT_SERVER = "localhost";

    @Test
    public void testServer() throws Exception {
        MPD mpd = mpdBuilder.server("localhost").build();
        assertEquals(InetAddress.getByName("localhost"), mpd.getAddress());
    }

    @Test
    public void testPort() throws Exception {
        MPD mpd = mpdBuilder.port(8080).build();
        assertEquals(mpd.getPort(), 8080);
    }

    @Test
    public void testTimeout() throws Exception {
        MPD mpd = mpdBuilder.timeout(0).build();
        assertEquals(mpd.getTimeout(), 0);
    }

    @Test
    public void testPassword() throws Exception {
        when(serverProperties.getPassword()).thenReturn(new ServerProperties().getPassword());
        when(mpdCommandExecutor
                .sendCommand(serverProperties.getPassword()))
                .thenReturn(new ArrayList<String>());

        MPD mpd = mpdBuilder.password("thepassword").build();

        verify(mpdCommandExecutor)
                .sendCommand(commandArgumentCaptor.capture(), commandArgumentCaptor.capture());
        assertNotNull(mpd);
        assertEquals(serverProperties.getPassword(), commandArgumentCaptor.getAllValues().get(0));
        assertEquals("thepassword", commandArgumentCaptor.getAllValues().get(1));
    }

    @Test
    public void testBuild() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertNotNull(mpd);
    }

    @Test
    public void testDefaultServer() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertEquals(InetAddress.getByName(DEFAULT_SERVER), mpd.getAddress());
    }

    @Test
    public void testDefaultPort() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertEquals(mpd.getPort(), DEFAULT_PORT);
    }

    @Test
    public void testDefaultTimeout() throws Exception {
        MPD mpd = mpdBuilder.build();
        assertEquals(mpd.getTimeout(), DEFAULT_TIMEOUT);
    }
}
