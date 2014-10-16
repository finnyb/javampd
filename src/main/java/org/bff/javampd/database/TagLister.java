package org.bff.javampd.database;

import java.util.Collection;
import java.util.List;

/**
 * Performs list operations against the MPD database.
 *
 * @author Bill
 */
public interface TagLister {
    public enum ListType {

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

    public enum ListInfoType {

        PLAYLIST("playlist:"),
        DIRECTORY("directory:"),
        FILE("file:");
        private String prefix;

        ListInfoType(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return prefix;
        }
    }

    Collection<String> listInfo(ListInfoType... types) throws MPDDatabaseException;

    Collection<String> list(ListType listType) throws MPDDatabaseException;

    Collection<String> list(ListType listType, List<String> params) throws MPDDatabaseException;
}
