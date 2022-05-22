package org.bff.javampd.processor;

public class AlbumArtistTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public AlbumArtistTagProcessor() {
    super("AlbumArtist:");
  }

  @Override
  public TagType getType() {
    return TagType.ALBUM_ARTIST;
  }
}
