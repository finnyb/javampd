package org.bff.javampd.album;

import java.util.HashMap;
import java.util.Map;
import org.bff.javampd.processor.*;

public enum AlbumProcessor {
  ALBUM_ARTIST(new AlbumArtistTagProcessor()),
  ARTIST(new ArtistTagProcessor()),
  DATE(new DateTagProcessor()),
  ALBUM(new AlbumTagProcessor()),
  GENRE(new GenreTagProcessor());

  private final transient TagResponseProcessor albumTagResponseProcessor;
  private static final Map<String, AlbumProcessor> lookup = new HashMap<>();

  static {
    for (AlbumProcessor a : AlbumProcessor.values()) {
      lookup.put(a.getProcessor().getPrefix().toLowerCase(), a);
    }
  }

  AlbumProcessor(TagResponseProcessor albumTagResponseProcessor) {
    this.albumTagResponseProcessor = albumTagResponseProcessor;
  }

  public static AlbumProcessor lookup(String line) {
    return lookup.get(line.substring(0, line.indexOf(":") + 1).toLowerCase());
  }

  public TagResponseProcessor getProcessor() {
    return this.albumTagResponseProcessor;
  }

  /**
   * Returns the line prefix that delimits songs in the response list
   *
   * @return the prefix that breaks songs in the list
   */
  public static String getDelimitingPrefix() {
    return ALBUM.getProcessor().getPrefix();
  }
}
