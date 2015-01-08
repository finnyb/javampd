package org.bff.javampd.genre;

import com.google.inject.Inject;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.DatabaseManager#getArtistDatabase} method from
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
    public Collection<MPDGenre> listAllGenres() throws MPDDatabaseException {
        List<MPDGenre> genres = new ArrayList<>();
        for (String str : tagLister.list(TagLister.ListType.GENRE)) {
            genres.add(new MPDGenre(str));
        }
        return genres;
    }

    @Override
    public MPDGenre listGenreByName(String name) throws MPDDatabaseException {

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
