package org.bff.javampd.genre;

import org.bff.javampd.BaseTest;
import org.bff.javampd.MPDException;
import org.bff.javampd.integrationdata.TestGenres;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MPDGenreDatabaseTest extends BaseTest {
    private GenreDatabase genreDatabase;

    @Before
    public void setUp() throws Exception {
        this.genreDatabase = getMpd().getDatabaseManager().getGenreDatabase();
    }

    @Test
    public void testListAllGenres() throws Exception {
        for (MPDGenre testGenre : TestGenres.getGenres()) {
            boolean exists = false;
            for (MPDGenre genre : genreDatabase.listAllGenres()) {
                if (testGenre.equals(genre)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                Assert.fail("Genre " + testGenre + " does not exist.");
            }
        }
    }

    @Test
    public void testGenreCount() throws MPDException {
        Assert.assertEquals(TestGenres.getGenres().size(), genreDatabase.listAllGenres().size());
    }
}