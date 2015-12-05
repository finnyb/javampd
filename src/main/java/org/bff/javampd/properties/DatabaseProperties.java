package org.bff.javampd.properties;

import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
public class DatabaseProperties extends MPDProperties {
    private enum Command {
        FIND("MPD_DB_FIND"),
        LIST("MPD_DB_LIST_TAG"),
        LISTINFO("MPD_DB_LIST_INFO"),
        SEARCH("MPD_DB_SEARCH"),
        LISTSONGS("MPD_DB_LIST_SONGS");

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
