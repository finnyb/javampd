package org.bff.javampd.artist;

import com.google.inject.Inject;
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
 * {@link org.bff.javampd.database.MusicDatabase#getArtistDatabase} method from
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
        List<MPDArtist> list = new ArrayList<>();
        for (String s : tagLister.list(TagLister.ListType.ARTIST)) {
            MPDArtist mpdArtist = new MPDArtist(convertResponse(s));
            list.add(mpdArtist);
        }
        return list;
    }

    @Override
    public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) {
        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.GENRE.getType());
        list.add(genre.getName());

        List<MPDArtist> result = new ArrayList<>();
        for (String s : tagLister.list(TagLister.ListType.ARTIST, list)) {
            MPDArtist mpdArtist = new MPDArtist(convertResponse(s));
            result.add(mpdArtist);
        }
        return result;
    }

    @Override
    public MPDArtist listArtistByName(String name) {

        List<String> list = new ArrayList<>();
        list.add(TagLister.ListType.ARTIST.getType());
        list.add(name);

        MPDArtist artist = null;
        List<MPDArtist> result = new ArrayList<>();
        for (String s : tagLister.list(TagLister.ListType.ARTIST, list)) {
            MPDArtist mpdArtist = new MPDArtist(convertResponse(s));
            result.add(mpdArtist);
        }
        List<MPDArtist> artists = new ArrayList<>(result);

        if (artists.size() > 1) {
            LOGGER.warn("Multiple artists returned for name {}", name);
        }

        if (!artists.isEmpty()) {
            artist = artists.get(0);
        }

        return artist;
    }

    private static String convertResponse(String s) {
        return s.substring(s.split(":")[0].length() + 1).trim();
    }
}
