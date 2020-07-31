package org.bff.javampd.processor;

import org.bff.javampd.album.MPDAlbum;

public interface AlbumTagResponseProcessor {
  /**
   * Returns the line prefix that delimits {@link MPDAlbum}s in the response list
   *
   * @return the prefix that breaks songs in the list
   */
  String getPrefix();

  /**
   * Process the response line and set the appropriate {@link MPDAlbum} property
   *
   * @param album the {@link MPDAlbum}
   * @param line  the line to process
   */
  void processTag(MPDAlbum album, String line);
}
