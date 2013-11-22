package org.bff.javampd.properties;

/**
 * @author bill
 * @since: 11/21/13 7:09 PM
 */
public class DatabaseProperties extends MPDProperties {
    private enum Command {

        FIND("MPD_DB_FIND"),
        LIST("MPD_DB_LIST_TAG"),
        LISTALL("MPD_DB_LIST_ALL"),
        LISTALLINFO("MPD_DB_LIST_ALL_INFO"),
        LISTINFO("MPD_DB_LIST_INFO"),
        SEARCH("MPD_DB_SEARCH"),
        LISTSONGS("MPD_DP_LIST_SONGS");

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

    public String getListAll() {
        return getPropertyString(Command.LISTALL.getKey());
    }

    public String getListAllInfo() {
        return getPropertyString(Command.LISTALLINFO.getKey());
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
