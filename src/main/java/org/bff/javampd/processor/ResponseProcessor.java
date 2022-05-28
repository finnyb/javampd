package org.bff.javampd.processor;

public interface ResponseProcessor {
  enum TagType {
    ALBUM_ARTIST,
    ALBUM,
    ARTIST,
    COMMENT,
    DATE,
    DISC,
    FILE,
    ID,
    NAME,
    POSITION,
    TIME,
    TITLE,
    TRACK,
    GENRE
  }

  /**
   * Returns the {@link TagType} of the processor
   *
   * @return the {@link TagType}
   */
  TagType getType();

  /**
   * Returns the line prefix that delimits songs in the response list
   *
   * @return the prefix that breaks songs in the list
   */
  String getPrefix();

  /**
   * Process the response line and set the appropriate @{link MPDSong} property
   *
   * @param line the line to process
   */
  String processTag(String line);
}
