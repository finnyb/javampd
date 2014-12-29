package org.bff.javampd.artist;

import com.google.inject.Inject;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
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
public class MPDArtistDatabase implements ArtistDatabase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDArtistDatabase.class);

    private TagLister tagLister;

    @Inject
    public MPDArtistDatabase(TagLister tagLister) {
        this.tagLister = tagLister;
    }

    @Override
    public Collection<MPDArtist> listAllArtists() throws MPDDatabaseException {
        List<MPDArtist> artists = new ArrayList<>();
        for (String str : tagLister.list(TagLister.ListType.ARTIST)) {
            artists.add(new MPDArtist(str));
        }

        return artists;
    }

    @Override
    public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) throws MPDDatabaseException {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDArtist> artists = new ArrayList<>();
        for (String str : tagLister.list(TagLister.ListType.ARTIST, list)) {
            artists.add(new MPDArtist(str));
        }

        return artists;
    }

    @Override
    public MPDArtist listArtistByName(String name) throws MPDDatabaseException {

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.ARTIST.getType());
        list.add(name);

        MPDArtist artist = null;
        List<String> artists = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST, list));

        if (artists.size() > 1) {
            LOGGER.warn("Multiple artists returned for name {}", name);
        }

        if (artists.size() > 0) {
            artist = new MPDArtist(artists.get(0));
        }

        return artist;
    }
}
