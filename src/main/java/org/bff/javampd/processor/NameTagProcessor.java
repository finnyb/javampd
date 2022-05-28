package org.bff.javampd.processor;

public class NameTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public NameTagProcessor() {
    super("Name:");
  }

  @Override
  public TagType getType() {
    return TagType.NAME;
  }
}
