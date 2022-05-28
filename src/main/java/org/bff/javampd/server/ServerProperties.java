package org.bff.javampd.server;

/** @author bill */
public class ServerProperties extends MPDProperties {

  private enum Command {
    SERVERENCODING("server.encoding"),
    CLEARERROR("cmd.clear.error"),
    CLOSE("cmd.close"),
    KILL("cmd.kill"),
    STATUS("cmd.status"),
    STATS("cmd.statistics"),
    STARTBULK("cmd.start.bulk"),
    ENDBULK("cmd.end.bulk"),
    PASSWORD("cmd.password"),
    PING("cmd.ping");

    private final String key;

    Command(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }
  }

  public String getClearError() {
    return getResponseCommand(Command.CLEARERROR);
  }

  public String getStatus() {
    return getResponseCommand(Command.STATUS);
  }

  public String getStats() {
    return getResponseCommand(Command.STATS);
  }

  public String getPing() {
    return getResponseCommand(Command.PING);
  }

  public String getPassword() {
    return getResponseCommand(Command.PASSWORD);
  }

  public String getClose() {
    return getResponseCommand(Command.CLOSE);
  }

  public String getStartBulk() {
    return getResponseCommand(Command.STARTBULK);
  }

  public String getEndBulk() {
    return getResponseCommand(Command.ENDBULK);
  }

  private String getResponseCommand(Command command) {
    return getPropertyString(command.getKey());
  }

  public String getEncoding() {
    return getResponseCommand(Command.SERVERENCODING);
  }
}
