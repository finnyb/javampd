package org.bff.javampd.song;

import org.bff.javampd.server.MPDProperties;

/**
 * @author bill
 */
public class SearchProperties extends MPDProperties {
    private enum Command {
        FIND("MPD_DB_FIND"),
        SEARCH("MPD_DB_SEARCH"),
        WINDOW("MPD_DB_WINDOW");

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

    public String getWindow() {
        return getPropertyString(Command.WINDOW.getKey());
    }

    public String getSearch() {
        return getPropertyString(Command.SEARCH.getKey());
    }
}
