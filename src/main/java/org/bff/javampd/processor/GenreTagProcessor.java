package org.bff.javampd.processor;

public class GenreTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public GenreTagProcessor() {
    super("Genre:");
  }

  @Override
  public TagType getType() {
    return TagType.GENRE;
  }
}
