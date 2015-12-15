package org.bff.javampd.genre;

import org.bff.javampd.database.TagLister;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MPDGenreDatabaseTest {
    @Mock
    private TagLister tagLister;

    @InjectMocks
    private MPDGenreDatabase genreDatabase;

    @Test
    public void testListAllGenres() throws Exception {
        List<String> testGenres = new ArrayList<>();
        testGenres.add("Genre1");
        testGenres.add("Genre2");

        when(tagLister.list(TagLister.ListType.GENRE)).thenReturn(testGenres);
        List<MPDGenre> genres = new ArrayList<>(genreDatabase.listAllGenres());

        assertEquals(testGenres.size(), genres.size());
        assertEquals(testGenres.get(0), genres.get(0).getName());
        assertEquals(testGenres.get(1), genres.get(1).getName());
    }

    @Test
    public void testListGenreByName() throws Exception {
        String genreName = "Genre1";

        List<String> testGenres = new ArrayList<>();
        testGenres.add(genreName);

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genreName);

        when(tagLister.list(TagLister.ListType.GENRE, list)).thenReturn(testGenres);

        assertEquals(genreName, genreDatabase.listGenreByName(genreName).getName());
    }

    @Test
    public void testListGenreByNameMultiples() throws Exception {
        String genreName1 = "Genre1";
        String genreName2 = "Genre2";

        List<String> testGenres = new ArrayList<>();
        testGenres.add(genreName1);
        testGenres.add(genreName2);

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genreName1);

        when(tagLister.list(TagLister.ListType.GENRE, list)).thenReturn(testGenres);

        assertEquals(genreName1, genreDatabase.listGenreByName(genreName1).getName());
    }
}