package org.bff.javampd.genre;

import com.google.inject.Inject;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the {@link org.bff.javampd.server.MPD#getArtistDatabase()} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDGenreDatabase implements GenreDatabase {

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
}
