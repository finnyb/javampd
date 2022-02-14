package org.bff.javampd.server;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MPDTest {

    @Test
    void testGetPort() {
        int port = 666;
        assertEquals(666, MPD.builder().port(port).build().getPort());
    }

    @Test
    void testGetDefaultPort() {
        assertEquals(6600, MPD.builder().build().getPort());
    }

    @Test
    void testGetAddress() throws UnknownHostException {
        String address = "localhost";
        assertEquals(InetAddress.getByName(address), MPD.builder().server(address).build().getAddress());
    }

    @Test
    void testGetDefaultAddress() throws UnknownHostException {
        assertEquals(InetAddress.getByName("localhost"), MPD.builder().build().getAddress());
    }

    @Test
    void testGetTimeout() {
        int timeOut = 666;
        assertEquals(timeOut, MPD.builder().timeout(timeOut).build().getTimeout());
    }

    @Test
    void testGetDefaultTimeout() {
        assertEquals(0, MPD.builder().build().getTimeout());
    }

    @Test
    void testGetPlayer() {
        assertNotNull(MPD.builder().build().getPlayer());
    }

    @Test
    void testGetPlaylist() {
        assertNotNull(MPD.builder().build().getPlaylist());
    }

    @Test
    void testGetAdmin() {
        assertNotNull(MPD.builder().build().getAdmin());
    }

    @Test
    void testGetMusicDatabase() {
        assertNotNull(MPD.builder().build().getMusicDatabase());
    }

    @Test
    void testGetServerStatistics() {
        assertNotNull(MPD.builder().build().getServerStatistics());
    }

    @Test
    void testGetServerStatus() {
        assertNotNull(MPD.builder().build().getServerStatus());
    }

    @Test
    void testGetMonitor() {
        assertNotNull(MPD.builder().build().getStandAloneMonitor());
    }

    @Test
    void testGetSongSearcher() {
        assertNotNull(MPD.builder().build().getSongSearcher());
    }

    @Test
    void testGetArtworkFinder() {
        assertNotNull(MPD.builder().build().getArtworkFinder());
    }

    @Test
    void testDefaultPassword() {
        assertNull(MPD.builder().build().getPassword());
    }
}
