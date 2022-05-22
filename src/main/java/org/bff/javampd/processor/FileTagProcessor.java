package org.bff.javampd.processor;

public class FileTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public FileTagProcessor() {
    super("file:");
  }

  @Override
  public TagType getType() {
    return TagType.FILE;
  }
}
