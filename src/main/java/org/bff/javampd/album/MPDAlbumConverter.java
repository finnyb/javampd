package org.bff.javampd.album;

import java.util.*;
import lombok.extern.slf4j.Slf4j;

/**
 * Converts a response from the server to an {@link MPDAlbum}
 *
 * @author bill
 */
@Slf4j
public class MPDAlbumConverter implements AlbumConverter {

  private static final String DELIMITING_PREFIX = AlbumProcessor.getDelimitingPrefix();

  @Override
  public Collection<MPDAlbum> convertResponseToAlbum(List<String> list) {
    var hashMap = new LinkedHashMap<String, MPDAlbum>();
    Iterator<String> iterator = list.iterator();

    var artists = new ArrayList<String>();
    var genres = new ArrayList<String>();
    var dates = new ArrayList<String>();
    String albumArtist = null;
    var album = "";

    String line;
    while (iterator.hasNext()) {
      line = iterator.next();

      var albumProcessor = AlbumProcessor.lookup(line);
      if (albumProcessor != null) {
        var tag = albumProcessor.getProcessor().processTag(line);
        switch (albumProcessor.getProcessor().getType()) {
          case ALBUM:
            album = tag;
            break;
          case ALBUM_ARTIST:
            albumArtist = tag;
            break;
          case ARTIST:
            artists.add(tag);
            break;
          case GENRE:
            genres.add(tag);
            break;
          case DATE:
            dates.add(tag);
            break;
          default:
            log.warn("Unprocessed album type {} found.", tag);
            break;
        }
      } else {
        log.warn("Processor not found - {}", line);
      }

      if (line.startsWith(DELIMITING_PREFIX)) {
        var a = hashMap.get(album);
        if (a == null) {
          hashMap.put(
              album,
              MPDAlbum.builder(album)
                  .albumArtist(albumArtist)
                  .artistNames(artists)
                  .genres(genres)
                  .dates(dates)
                  .build());
        } else {
          a.addArtists(artists);
          a.addGenres(genres);
          a.addDates(dates);
        }

        artists = new ArrayList<>();
        genres = new ArrayList<>();
        dates = new ArrayList<>();
        albumArtist = null;
        album = "";
      }
    }

    return hashMap.values();
  }
}
