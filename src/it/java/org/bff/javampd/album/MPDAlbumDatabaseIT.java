package org.bff.javampd.album;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.artist.ArtistDatabase;
import org.bff.javampd.artist.MPDArtist;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestAlbums;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestGenres;
import org.bff.javampd.integrationdata.TestYears;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MPDAlbumDatabaseIT extends BaseTest {
    private AlbumDatabase albumDatabase;
    private ArtistDatabase artistDatabase;

    @Before
    public void setUp() throws MPDException {
        this.albumDatabase = getMpd().getDatabaseManager().getAlbumDatabase();
        this.artistDatabase = getMpd().getDatabaseManager().getArtistDatabase();
    }

    @Test
    public void testListAlbumsByArtist() throws Exception {
        for (MPDArtist artist : TestArtists.getArtists()) {
            compareAlbumLists(TestArtists.TEST_ARTIST_ALBUM_MAP.get(artist),
                    albumDatabase.listAlbumsByArtist(artist));
        }
    }

    @Test
    public void testListAlbumsByGenre() throws Exception {
        for (MPDGenre genre : TestGenres.getGenres()) {
            compareAlbumLists(TestGenres.getAlbumsForGenre(genre),
                    albumDatabase.listAlbumsByGenre(genre));
        }
    }

    @Test
    public void testListAlbumsByYear() throws Exception {
        for (String year : TestYears.getYears()) {
            compareAlbumLists(TestYears.getAlbums(year),
                    albumDatabase.listAlbumsByYear(year));
        }
    }

    @Test
    public void testAllAlbums() throws MPDDatabaseException {

        for (MPDAlbum testAlbum : TestAlbums.getAlbums()) {
            boolean exists = false;
            for (MPDAlbum album : getAllAlBums()) {
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
    public void testListAlbumNames() throws MPDDatabaseException {
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
    public void testFindAlbum() throws MPDDatabaseException {
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
    public void testAlbumCount() throws MPDException {
        Assert.assertEquals(TestAlbums.getAlbums().size(), getAllAlBums().size());
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

    private Collection<MPDAlbum> getAllAlBums() throws MPDDatabaseException {
        List<MPDAlbum> albums = new ArrayList<>();
        for (MPDArtist artist : artistDatabase.listAllArtists()) {
            albums.addAll(albumDatabase.listAlbumsByArtist(artist));
        }
        return albums;
    }
}