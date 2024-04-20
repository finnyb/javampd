package org.bff.javampd.processor;

import lombok.Getter;

@Getter
public abstract class TagResponseProcessor implements ResponseProcessor {

  private final String prefix;

  protected TagResponseProcessor(String prefix) {
    this.prefix = prefix;
  }

  protected boolean startsWith(String line) {
    return line.startsWith(getPrefix());
  }

  @Override
  public String processTag(String line) {
    if (startsWith(line)) {
      return line.substring(getPrefix().length()).trim();
    }

    return null;
  }
}
