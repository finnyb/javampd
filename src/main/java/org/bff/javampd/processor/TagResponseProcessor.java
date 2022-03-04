package org.bff.javampd.processor;

public abstract class TagResponseProcessor implements ResponseProcessor {

  private String prefix;

  protected TagResponseProcessor(String prefix) {
    this.prefix = prefix;
  }

  public String getPrefix() {
    return prefix;
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
