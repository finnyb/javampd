package org.bff.javampd.genre;

import org.bff.javampd.BaseTest;
import org.bff.javampd.integrationdata.TestGenres;
import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MPDGenreDatabaseIT extends BaseTest {
    private GenreDatabase genreDatabase;

    @BeforeEach
    public void setUp() {
        this.genreDatabase = getMpd().getMusicDatabase().getGenreDatabase();
    }

    @Test
    public void testListAllGenres() {
        for (MPDGenre testGenre : TestGenres.getGenres()) {
            boolean exists = false;
            for (MPDGenre genre : genreDatabase.listAllGenres()) {
                if (testGenre.equals(genre)) {
                    exists = true;
                    break;
                }
            }

            if (!exists) {
                fail("Genre " + testGenre + " does not exist.");
            }
        }
    }

    @Test
    public void testListGenreByName() {
        String testGenre = "Rock";
        MPDGenre genre = genreDatabase.listGenreByName(testGenre);

        assertNotNull(genre);
        assertEquals(testGenre, genre.getName());
    }

    @Test
    public void testGenreCount() {
        assertEquals(TestGenres.getGenres().size(), genreDatabase.listAllGenres().size());
    }
}