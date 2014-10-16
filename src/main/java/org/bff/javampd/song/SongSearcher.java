package org.bff.javampd.song;

import org.bff.javampd.database.MPDDatabaseException;

import java.util.Collection;

/**
 * Provides search and list functionality for {@link MPDSong}s
 *
 * @author Bill
 */
public interface SongSearcher {
    /**
     * Defines the scope of items such as find, search.
     */
    public enum ScopeType {

        ALBUM("album"),
        ARTIST("artist"),
        TITLE("title"),
        TRACK("track"),
        NAME("name"),
        GENRE("genre"),
        DATE("date"),
        COMPOSER("composer"),
        PERFORMER("performer"),
        COMMENT("comment"),
        DISC("disc"),
        FILENAME("filename"),
        ANY("any");
        private String type;

        ScopeType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
    }

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a searches matching the scope type any.
     * Please note this returns a partial match of a title.  To find an
     * exact match use {@link #find(ScopeType, String)}.
     *
     * @param searchType the {@link ScopeType}
     * @param param      the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws org.bff.javampd.database.MPDDatabaseException if the database throws an exception during the search
     */
    Collection<MPDSong> search(ScopeType searchType, String param) throws MPDDatabaseException;

    /**
     * Returns a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s for a searches matching the scope type any.
     * Please note this only returns an exact match of artist.  To find a partial
     * match use {@link #search(ScopeType, String)}.
     *
     * @param scopeType the {@link ScopeType}
     * @param param     the search criteria
     * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
     * @throws MPDDatabaseException if the database throws an exception during the search
     */
    Collection<MPDSong> find(ScopeType scopeType, String param) throws MPDDatabaseException;
}
