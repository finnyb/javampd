package org.bff.javampd.album;

import static org.bff.javampd.processor.ResponseProcessor.TagType.ALBUM;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.bff.javampd.processor.ResponseProcessor;

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

    MPDAlbum.MPDAlbumBuilder albumBuilder = new MPDAlbum.MPDAlbumBuilder();

    String line;
    while (iterator.hasNext()) {
      line = iterator.next();

      var albumProcessor = AlbumProcessor.lookup(line);
      if (albumProcessor != null) {
        var tagType = albumProcessor.getProcessor().getType();
        var tagValue = albumProcessor.getProcessor().processTag(line);
        albumBuilder = mergeTag(albumBuilder, tagType, tagValue);
        if (tagType == ALBUM) {
          MPDAlbum album = albumBuilder.build();
          if (album.getAlbumArtist() != null
              && album.getName() != null
              && album.getDates() != null
              && !album.getDates().isEmpty()) {
            String mapKey =
                String.format(
                    "%s - %s [%s]",
                    album.getAlbumArtist(), album.getName(), album.getDates().get(0));
            hashMap.putIfAbsent(mapKey, album);
          }
        }
      } else {
        log.warn("Processor not found - {}", line);
      }
    }

    return hashMap.values();
  }

  /**
   * Integrates the provided tag into the MPDAlbumBuilder.
   *
   * <p>It is assumed that all album metadata is grouped hierarchically in this (descending) order:
   *
   * <ol>
   *   <li>{@link ResponseProcessor.TagType#ALBUM_ARTIST}
   *   <li>{@link ResponseProcessor.TagType#GENRE}
   *   <li>{@link ResponseProcessor.TagType#DATE}
   *   <li>{@link ResponseProcessor.TagType#ARTIST}
   *   <li>{@link ResponseProcessor.TagType#ALBUM}
   * </ol>
   *
   * <p>Any tag that exists in higher levels of the hierarchy implies erasure of lower-hierarchy
   * tags.
   *
   * @param accumulator The current album builder instance.
   * @param tagValue The extracted, parsed value for the tag
   * @param tagType The type of tag
   * @return A reference to the builder provided as parameter, updated according to the rest of the
   *     supplied parameters plus the assumptions explained above.
   */
  private static MPDAlbum.MPDAlbumBuilder mergeTag(
      MPDAlbum.MPDAlbumBuilder accumulator, ResponseProcessor.TagType tagType, String tagValue) {
    MPDAlbum cachedResult = accumulator.build();
    return switch (tagType) {
      case ALBUM_ARTIST -> accumulator
          .albumArtist(tagValue)
          .genres(new ArrayList<>())
          .dates(new ArrayList<>())
          .artistNames(new ArrayList<>())
          .name(null);
      case GENRE -> {
        cachedResult.addGenre(tagValue);
        yield accumulator
            .genres(cachedResult.getGenres())
            .dates(new ArrayList<>())
            .artistNames(new ArrayList<>())
            .name(null);
      }
      case DATE -> accumulator
          .dates(new ArrayList<>(List.of(tagValue)))
          .artistNames(new ArrayList<>())
          .name(null);
      case ARTIST -> {
        cachedResult.addArtist(tagValue);
        yield accumulator.artistNames(cachedResult.getArtistNames()).name(null);
      }
      case ALBUM -> accumulator.name(tagValue);
      default -> {
        log.warn("Unprocessed tagValue type {} found.", tagValue);
        yield accumulator;
      }
    };
  }
}
