package org.bff.javampd.album;

import org.bff.javampd.BaseTest;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestAlbums;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestGenres;
import org.bff.javampd.integrationdata.TestYears;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MPDAlbumDatabaseIT extends BaseTest {
    private AlbumDatabase albumDatabase;
    private ArtistDatabase artistDatabase;

    @BeforeEach
    public void setUp() {
        this.albumDatabase = getMpd().getMusicDatabase().getAlbumDatabase();
        this.artistDatabase = getMpd().getMusicDatabase().getArtistDatabase();
    }

    @Test
    public void testListAlbumsByArtist() {
        for (MPDArtist artist : TestArtists.getArtists()) {
            compareAlbumLists(TestArtists.TEST_ARTIST_ALBUM_MAP.get(artist),
                    albumDatabase.listAlbumsByArtist(artist));
        }
    }

    @Test
    public void testListAlbumsByGenre() {
        for (MPDGenre genre : TestGenres.getGenres()) {
            compareAlbumLists(TestGenres.getAlbumsForGenre(genre),
                    albumDatabase.listAlbumsByGenre(genre));
        }
    }

    @Test
    public void testListAlbumsByYear() {
        for (String year : TestYears.getYears()) {
            compareAlbumLists(TestYears.getAlbums(year),
                    albumDatabase.listAlbumsByYear(year));
        }
    }

    @Test
    public void testAllAlbums() {

        for (MPDAlbum testAlbum : TestAlbums.getAlbums()) {
            boolean exists = false;
            for (MPDAlbum album : albumDatabase.listAllAlbums()) {
                if (testAlbum.equals(album)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Album " + testAlbum + " does not exist.");
            }
        }
    }

    @Test
    @Ignore
    public void testAllAlbumsWindowed() {
        int start = 0;
        int end = 1;

        List<MPDAlbum> albums = new ArrayList<>(albumDatabase.listAllAlbums(start, end));

        assertEquals(end - start, albums.size());

        for (MPDAlbum testAlbum : albums) {
            assertNotNull(testAlbum.getArtistName());
            assertNotNull(testAlbum.getName());
        }
    }

    @Test
    public void testAllAlbumsByArtist() {

        for (MPDAlbum testAlbum : TestAlbums.getAlbums()) {
            boolean exists = false;
            for (MPDAlbum album : getAllAlBumsByArtist()) {
                if (testAlbum.equals(album)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Album " + testAlbum + " does not exist.");
            }
        }
    }

    @Test
    public void testListAlbumNames() {
        for (MPDAlbum testAlbum : TestAlbums.getAlbums()) {
            boolean exists = false;
            for (String albumName : albumDatabase.listAllAlbumNames()) {
                if (testAlbum.getName().equals(albumName)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Album " + testAlbum + " does not exist.");
            }
        }
    }

    @Test
    public void testFindAlbum() {
        for (MPDAlbum testAlbum : TestAlbums.getAlbums()) {
            boolean exists = false;
            for (MPDAlbum album : albumDatabase.findAlbum(testAlbum.getName())) {
                if (testAlbum.equals(album)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Album " + testAlbum + " does not exist.");
            }
        }
    }

    @Test
    public void testAlbumCountByArtist() {
        Assert.assertEquals(TestAlbums.getAlbums().size(), getAllAlBumsByArtist().size());
    }

    @Test
    public void testAlbumCount() {
        Assert.assertEquals(TestAlbums.getAlbums().size(), albumDatabase.listAllAlbums().size());
    }

    private void compareAlbumLists(Collection<MPDAlbum> testResults, Collection<MPDAlbum> foundAlbums) {

        if (testResults.isEmpty()) {
            Assert.assertTrue("Bad test criteria.  Should have a size of at least 1", false);
        }

        assertEquals(testResults.size(), foundAlbums.size());

        for (MPDAlbum album : testResults) {
            boolean found = false;
            for (MPDAlbum albumDb : foundAlbums) {
                if (album.getArtistName().equals(albumDb.getArtistName()) &&
                        album.getName().equals(albumDb.getName())) {
                    found = true;
                    break;
                }
            }

            assertTrue(found);
        }
    }

    private Collection<MPDAlbum> getAllAlBumsByArtist() {
        List<MPDAlbum> albums = new ArrayList<>();
        for (MPDArtist artist : artistDatabase.listAllArtists()) {
            albums.addAll(albumDatabase.listAlbumsByArtist(artist));
        }
        return albums;
    }
}