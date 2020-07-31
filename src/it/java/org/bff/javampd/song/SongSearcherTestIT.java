package org.bff.javampd.song;

import org.bff.javampd.BaseTest;
import org.bff.javampd.integrationdata.TestSongs;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SongSearcherTestIT extends BaseTest {

    private SongSearcher songSearcher;

    @BeforeEach
    public void setUp() {
        songSearcher = getMpd().getSongSearcher();
    }

    @Test
    public void testSearch() {
        String albumName = "Album1";

        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + albumName + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs =
                new ArrayList<>(songSearcher.search(SongSearcher.ScopeType.ALBUM, albumName));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    @Ignore
    public void testWindowedSearch() {
        String albumName = "Album1";

        List<MPDSong> testResults = new ArrayList<>();

        for (MPDSong song : TestSongs.getSongs()) {
            if (song.getFile().contains("-" + albumName + "-")) {
                testResults.add(song);
            }
        }

        List<MPDSong> foundSongs =
                new ArrayList<>(songSearcher.search(SongSearcher.ScopeType.ALBUM, albumName, 0, 3));

        compareSongLists(testResults, foundSongs);
    }

    @Test
    public void testFind() {

    }

    @Test
    public void testWindowedFind() {

    }
}