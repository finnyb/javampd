package org.bff.javampd.artist;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.genre.MPDGenre;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MPDArtistDatabase represents a artist database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getArtistDatabase} method from
 * the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDArtistDatabase implements ArtistDatabase {
  private static final Logger LOGGER = LoggerFactory.getLogger(
    MPDArtistDatabase.class
  );

  private TagLister tagLister;

  @Inject
  public MPDArtistDatabase(TagLister tagLister) {
    this.tagLister = tagLister;
  }

  @Override
  public Collection<MPDArtist> listAllArtists() {
    return tagLister
      .list(TagLister.ListType.ARTIST)
      .stream()
      .map(s -> new MPDArtist(convertResponse(s)))
      .collect(Collectors.toList());
  }

  @Override
  public Collection<MPDArtist> listArtistsByGenre(MPDGenre genre) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.GENRE.getType());
    list.add(genre.getName());

    return tagLister
      .list(TagLister.ListType.ARTIST, list)
      .stream()
      .map(s -> new MPDArtist(convertResponse(s)))
      .collect(Collectors.toList());
  }

  @Override
  public MPDArtist listArtistByName(String name) {
    List<String> list = new ArrayList<>();
    list.add(TagLister.ListType.ARTIST.getType());
    list.add(name);

    MPDArtist artist = null;
    List<MPDArtist> artists = tagLister
      .list(TagLister.ListType.ARTIST, list)
      .stream()
      .map(s -> new MPDArtist(convertResponse(s)))
      .collect(Collectors.toList());

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
