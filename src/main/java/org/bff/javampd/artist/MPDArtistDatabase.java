package org.bff.javampd.artist;

import com.google.inject.Inject;
import org.bff.javampd.database.MusicDatabase;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link MusicDatabase#getArtistDatabase} method from
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
    public Collection<MPDArtist> listAllArtists() {
        return tagLister.list(TagLister.ListType.ARTIST)
                .stream()
                .map(MPDArtist::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre.getName());

        return tagLister.list(TagLister.ListType.ARTIST, list)
                .stream()
                .map(MPDArtist::new)
                .collect(Collectors.toList());
    }

    @Override
    public MPDArtist listArtistByName(String name) {

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.ARTIST.getType());
        list.add(name);

        MPDArtist artist = null;
        List<String> artists = new ArrayList<>(tagLister.list(TagLister.ListType.ARTIST, list));

        if (artists.size() > 1) {
            LOGGER.warn("Multiple artists returned for name {}", name);
        }

        if (!artists.isEmpty()) {
            artist = new MPDArtist(artists.get(0));
        }

        return artist;
    }
}
