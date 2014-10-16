package org.bff.javampd.artist;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestGenres;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MPDArtistDatabaseIT extends BaseTest {
    private ArtistDatabase artistDatabase;

    @Before
    public void setUp() throws Exception {
        this.artistDatabase = getMpd().getDatabaseManager().getArtistDatabase();
    }

    @Test
    public void testListAllArtists() throws Exception {
        for (MPDArtist testArtist : TestArtists.getArtists()) {
            boolean exists = false;
            for (MPDArtist artist : artistDatabase.listAllArtists()) {
                if (testArtist.equals(artist)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Artist " + testArtist + " does not exist.");
            }
        }
    }

    @Test
    public void testListArtistsByGenre() throws Exception {
        for (MPDGenre testGenre : TestGenres.getGenres()) {
            for (MPDArtist testArtist : TestGenres.getArtistsForGenre(testGenre)) {
                boolean exists = false;
                for (MPDArtist artist : artistDatabase.listArtistsByGenre(testGenre)) {
                    if (testArtist.equals(artist)) {
                        exists = true;
                        break;
                    }
                }
                if (!exists) {
                    Assert.fail("Artist " + testArtist + " does not exist.");
                }
            }

        }
    }

    @Test
    public void testArtistCount() throws MPDException {
        Assert.assertEquals(TestArtists.getArtists().size(), artistDatabase.listAllArtists().size());
    }
}