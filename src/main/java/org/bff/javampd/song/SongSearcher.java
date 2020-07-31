package org.bff.javampd.song;

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
  enum ScopeType {
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
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for a searches
   * matching the scope type any.  Please note this returns a partial match of a title.  To find an
   * exact match use {@link #find(ScopeType, String)}.
   *
   * @param searchType the {@link ScopeType}
   * @param criteria   the search criteria
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
   */
  Collection<MPDSong> search(ScopeType searchType, String criteria);

  /**
   * Returns a windowed {@link java.util.Collection} of {@link MPDSong}s for a searches
   * matching the scope type any.  Please note this returns a partial match of a title.  To find an
   * exact match use {@link #find(ScopeType, String)}.
   *
   * @param searchType the {@link ScopeType}
   * @param criteria   the search criteria
   * @param start      the starting index
   * @param end        the ending index
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> search(
    ScopeType searchType,
    String criteria,
    int start,
    int end
  );

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for a searches
   * matching the scope type any.  Please note this only returns an exact match of artist.  To find a partial
   * match use {@link #search(ScopeType, String)}.
   *
   * @param scopeType the {@link ScopeType}
   * @param criteria  the search criteria
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> find(ScopeType scopeType, String criteria);

  /**
   * Returns a windowed {@link java.util.Collection} of {@link MPDSong}s for a searches matching the scope type any.
   * Please note this only returns an exact match of artist.  To find a partial
   * match use {@link #search(ScopeType, String)}.
   *
   * @param scopeType the {@link ScopeType}
   * @param criteria  the search criteria
   * @param start     the starting index
   * @param end       the ending index
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> find(
    ScopeType scopeType,
    String criteria,
    int start,
    int end
  );
}
