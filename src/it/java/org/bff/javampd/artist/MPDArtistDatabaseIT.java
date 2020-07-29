package org.bff.javampd.artist;

import org.bff.javampd.BaseTest;
import org.bff.javampd.genre.MPDGenre;
import org.bff.javampd.integrationdata.TestArtists;
import org.bff.javampd.integrationdata.TestGenres;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MPDArtistDatabaseIT extends BaseTest {
    private ArtistDatabase artistDatabase;

    @BeforeEach
    public void setUp() {
        this.artistDatabase = getMpd().getMusicDatabase().getArtistDatabase();
    }

    @Test
    public void testListAllArtists() {
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
    public void testListArtistsByGenre() {
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
    public void testListArtistsByName() {
        for (MPDArtist testArtist : TestArtists.getArtists()) {
            MPDArtist artist = artistDatabase.listArtistByName(testArtist.getName());
            assertEquals(testArtist, artist);
        }
    }

    @Test
    public void testArtistCount() {
        assertEquals(TestArtists.getArtists().size(), artistDatabase.listAllArtists().size());
    }
}