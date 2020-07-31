package org.bff.javampd.database;

import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
public class DatabaseProperties extends MPDProperties {

  private enum Command {
    FIND("db.find"),
    LIST("db.list.tag"),
    GROUP("db.group"),
    LISTINFO("db.list.info"),
    SEARCH("db.search"),
    LISTSONGS("db.list.songs");

    private final String key;

    Command(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
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
    return getPropertyString(Command.LISTINFO.getKey());
  }

  public String getSearch() {
    return getPropertyString(Command.SEARCH.getKey());
  }

  public String getListSongs() {
    return getPropertyString(Command.LISTSONGS.getKey());
  }
}
