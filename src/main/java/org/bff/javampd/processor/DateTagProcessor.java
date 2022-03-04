package org.bff.javampd.processor;

public class DateTagProcessor extends TagResponseProcessor implements ResponseProcessor {

  public DateTagProcessor() {
    super("Date:");
  }

  @Override
  public TagType getType() {
    return TagType.DATE;
  }
}
