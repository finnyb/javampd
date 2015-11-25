package org.bff.javampd.genre;

import com.google.inject.Inject;
import org.bff.javampd.database.TagLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MPDGenreDatabase represents a genre database to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getGenreDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDGenreDatabase implements GenreDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDGenreDatabase.class);

    private TagLister tagLister;

    @Inject
    public MPDGenreDatabase(TagLister tagLister) {
        this.tagLister = tagLister;
    }

    @Override
    public Collection<MPDGenre> listAllGenres() {
        return tagLister.list(TagLister.ListType.GENRE)
                .stream()
                .map(MPDGenre::new)
                .collect(Collectors.toList());
    }

    @Override
    public MPDGenre listGenreByName(String name) {

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(name);

        MPDGenre genre = null;
        List<String> genres = new ArrayList<>(tagLister.list(TagLister.ListType.GENRE, list));

        if (genres.size() > 1) {
            LOGGER.warn("Multiple genres returned for name {}", name);
        }

        if (!genres.isEmpty()) {
            genre = new MPDGenre(genres.get(0));
        }

        return genre;
    }
}
