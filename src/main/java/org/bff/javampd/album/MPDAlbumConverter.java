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

  @Override
  public Collection<MPDAlbum> convertResponseToAlbum(List<String> list) {
    var hashMap = new LinkedHashMap<String, MPDAlbum>();
    Iterator<String> iterator = list.iterator();

    List<String> artists = new ArrayList<>();
    List<String> genres = new ArrayList<>();
    String date = null;
    String albumArtist = null;
    String albumName = "";

    String line;
    while (iterator.hasNext()) {
      line = iterator.next();

      var albumProcessor = AlbumProcessor.lookup(line);
      if (albumProcessor != null) {
        var tag = albumProcessor.getProcessor().processTag(line);
        switch (albumProcessor.getProcessor().getType()) {
          case ALBUM_ARTIST:
            albumArtist = tag;
            artists = new ArrayList<>();
            date = null;
            genres = new ArrayList<>();
            break;
          case GENRE:
            genres.add(tag);
            artists = new ArrayList<>();
            date = null;
            break;
          case DATE:
            date = tag;
            artists = new ArrayList<>();
            break;
          case ARTIST:
            artists.add(tag);
            break;
          case ALBUM:
            albumName = tag;
            if (albumArtist != null
                && !albumArtist.isBlank()
                && !albumName.isBlank()
                && date != null
                && !date.isEmpty()) {
              String mapKey = String.format("%s - %s [%s]", albumArtist, albumName, date);
              MPDAlbum a = hashMap.get(mapKey);
              if (a == null) {
                hashMap.put(
                    mapKey,
                    MPDAlbum.builder(albumName)
                        .albumArtist(albumArtist)
                        .artistNames(artists)
                        .genres(genres)
                        .dates(new ArrayList<>(List.of(date)))
                        .build());
              }
            }
            break;
          default:
            log.warn("Unprocessed albumName type {} found.", tag);
            break;
        }
      } else {
        log.warn("Processor not found - {}", line);
      }
    }

    return hashMap.values();
  }
}
