package org.bff.javampd.genre;

import com.google.inject.Inject;
import org.bff.javampd.database.TagLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
        List<MPDGenre> list = new ArrayList<>();
        for (String s : tagLister.list(TagLister.ListType.GENRE)) {
            MPDGenre mpdGenre = new MPDGenre(s.substring(s.split(":")[0].length() + 1).trim());
            list.add(mpdGenre);
        }
        return list;
    }

    @Override
    public MPDGenre listGenreByName(String name) {

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(name);

        MPDGenre genre = null;
        List<MPDGenre> genres = new ArrayList<>();

        for (String response : tagLister.list(TagLister.ListType.GENRE, list)) {
            genres.add(new MPDGenre(response.split(":")[1].trim()));
        }

        if (genres.size() > 1) {
            LOGGER.warn("Multiple genres returned for name {}", name);
        }

        if (!genres.isEmpty()) {
            genre = genres.get(0);
        }

        return genre;
    }
}
