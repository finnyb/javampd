package org.bff.javampd.genre;

import org.bff.javampd.database.TagLister;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MPDGenreDatabaseTest {
    private static final String GENRE_PREFIX = "Genre: ";

    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDGenreDatabase genreDatabase;

    @Test
    void testListAllGenres() {
        MPDGenre testGenre1 = new MPDGenre("Genre1");
        MPDGenre testGenre2 = new MPDGenre("Genre2");

        List<String> testGenres = new ArrayList<>();
        testGenres.add(GENRE_PREFIX + testGenre1.getName());
        testGenres.add(GENRE_PREFIX + testGenre2.getName());

        when(tagLister.list(TagLister.ListType.GENRE)).thenReturn(testGenres);
        List<MPDGenre> genres = new ArrayList<>(genreDatabase.listAllGenres());

        assertEquals(testGenres.size(), genres.size());
        assertEquals(testGenre1, genres.get(0));
        assertEquals(testGenre2, genres.get(1));
    }

    @Test
    void testListGenreByName() {
        MPDGenre genre = new MPDGenre("Genre1");

        List<String> testGenres = new ArrayList<>();
        testGenres.add(GENRE_PREFIX + genre.getName());

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre.getName());

        when(tagLister.list(TagLister.ListType.GENRE, list)).thenReturn(testGenres);

        assertEquals(genre, genreDatabase.listGenreByName(genre.getName()));
    }

    @Test
    void testListGenreByNameMultiples() {
        MPDGenre genre1 = new MPDGenre("Genre1");
        MPDGenre genre2 = new MPDGenre("Genre2");

        List<String> testGenres = new ArrayList<>();
        testGenres.add(GENRE_PREFIX + genre1.getName());
        testGenres.add(GENRE_PREFIX + genre2.getName());

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre1.getName());

        when(tagLister.list(TagLister.ListType.GENRE, list)).thenReturn(testGenres);

        assertEquals(genre1, genreDatabase.listGenreByName(genre1.getName()));
    }
}
