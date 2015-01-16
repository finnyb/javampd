package org.bff.javampd.server;

import org.bff.javampd.BaseTest;
import org.bff.javampd.album.MPDAlbum;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.integrationdata.TestAlbums;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestSongs;
import org.bff.javampd.player.Player;
import org.bff.javampd.playlist.Playlist;
import org.bff.javampd.song.MPDSong;
import org.bff.javampd.statistics.ServerStatistics;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDServerStatisticsIT extends BaseTest {

    private Player player;
    private Playlist playlist;
    private ServerStatistics serverStatistics;

    @Before
    public void setUp() {
        this.player = getMpd().getPlayer();
        this.playlist = getMpd().getPlaylist();
        this.serverStatistics = getMpd().getServerStatistics();
    }

    @Test
    public void testGetPlaytime() throws Exception {
        player.stop();
        List<MPDSong> songs = new ArrayList<>(TestSongs.getSongs());
        playlist.addSong(songs.get(0));
        player.play();
        Thread.sleep(1000);

        player.stop();

        assertTrue(serverStatistics.getPlaytime() > 0);
    }

    @Test
    public void testGetUptime() throws Exception {
        assertTrue(serverStatistics.getUptime() > 0);
    }

    @Test
    public void testAlbumCount() throws Exception {
        Set<String> albumSet = new HashSet<>();
        for (MPDAlbum album : TestAlbums.getAlbums()) {
            //stats does not report blank albums
            if (!TestAlbums.NULL_ALBUM.equals(album.getName())) {
                albumSet.add(album.getName());
            }
        }

        assertEquals(albumSet.size(), serverStatistics.getAlbumCount());
    }

    @Test
    public void testArtistCount() throws Exception {
        Set<MPDArtist> artistSet = new HashSet<>();
        for (MPDArtist artist : TestArtists.getArtists()) {
            //stats does not report blank albums
            if (!TestArtists.NULL_ARTIST.equals(artist.getName())) {
                artistSet.add(artist);
            }
        }

        assertEquals(artistSet.size(), serverStatistics.getArtistCount());
    }

    @Test
    public void testSongCount() throws Exception {
        assertEquals(TestSongs.getSongs().size(), serverStatistics.getSongCount());
    }

    @Test
    public void testGetDatabasePlaytime() throws Exception {
        assertTrue(serverStatistics.getDatabasePlaytime() > 0);
    }

    @Test
    public void testGetDatabaseUpdateTime() throws Exception {
        assertTrue(serverStatistics.getLastUpdateTime() > 0);
    }
}
