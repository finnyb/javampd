package org.bff.javampd.processor;

public class AlbumTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public AlbumTagProcessor() {
    super("Album:");
  }

  @Override
  public TagType getType() {
    return TagType.ALBUM;
  }
}
