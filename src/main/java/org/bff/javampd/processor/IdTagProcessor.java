package org.bff.javampd.processor;

public class IdTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public IdTagProcessor() {
    super("Id:");
  }

  @Override
  public TagType getType() {
    return TagType.ID;
  }
}
