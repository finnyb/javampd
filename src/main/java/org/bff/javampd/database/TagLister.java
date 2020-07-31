package org.bff.javampd.database;

import java.util.List;

/**
 * Performs list operations against the MPD database.
 *
 * @author Bill
 */
public interface TagLister {
  enum ListType {
    ALBUM("album"),
    ARTIST("artist"),
    GENRE("genre"),
    DATE("date");

    private String type;

    ListType(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  enum GroupType {
    ALBUM("album"),
    ARTIST("artist"),
    GENRE("genre"),
    DATE("date");

    private String type;

    GroupType(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  enum ListInfoType {
    PLAYLIST("playlist:"),
    DIRECTORY("directory:"),
    FILE("file:"),
    LAST_MODIFIED("Last-Modified:");

    private String prefix;

    ListInfoType(String prefix) {
      this.prefix = prefix;
    }

    public String getPrefix() {
      return prefix;
    }
  }

  List<String> listInfo(ListInfoType... types);

  List<String> list(ListType listType);

  List<String> list(ListType listType, GroupType... groupTypes);

  List<String> list(
    ListType listType,
    List<String> params,
    GroupType... groupTypes
  );
}
