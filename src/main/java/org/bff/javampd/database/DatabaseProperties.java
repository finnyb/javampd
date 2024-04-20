package org.bff.javampd.database;

import lombok.Getter;
import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
public class DatabaseProperties extends MPDProperties {
  @Getter
  private enum Command {
    FIND("db.find"),
    LIST("db.list.tag"),
    GROUP("db.group"),
    LIST_INFO("db.list.info"),
    SEARCH("db.search"),
    LIST_SONGS("db.list.songs");

    private final String key;

    Command(String key) {
      this.key = key;
    }
  }

  public String getFind() {
    return getPropertyString(Command.FIND.getKey());
  }

  public String getList() {
    return getPropertyString(Command.LIST.getKey());
  }

  public String getGroup() {
    return getPropertyString(Command.GROUP.getKey());
  }

  public String getListInfo() {
    return getPropertyString(Command.LIST_INFO.getKey());
  }

  public String getSearch() {
    return getPropertyString(Command.SEARCH.getKey());
  }

  public String getListSongs() {
    return getPropertyString(Command.LIST_SONGS.getKey());
  }
}
