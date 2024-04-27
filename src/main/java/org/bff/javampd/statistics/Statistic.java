package org.bff.javampd.statistics;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public enum Statistic {
  ALBUMS("albums:"),
  ARTISTS("artists:"),
  SONGS("songs:"),
  DBPLAYTIME("db_playtime:"),
  DBUPDATE("db_update:"),
  PLAYTIME("playtime:"),
  UPTIME("uptime:");

  private static final Map<String, Statistic> lookup = new HashMap<>();

  static {
    for (Statistic s : Statistic.values()) {
      lookup.put(s.getPrefix().toLowerCase(), s);
    }
  }

  /**
   * -- GETTER -- Returns the <CODE>String</CODE> prefix of the response.
   *
   * @return the prefix of the response
   */
  private final String prefix;

  /**
   * Default constructor for Statistics
   *
   * @param prefix the prefix of the line in the response
   */
  Statistic(String prefix) {
    this.prefix = prefix;
  }

  public static Statistic lookup(String line) {
    return lookup.get(line.substring(0, line.indexOf(":") + 1));
  }
}
