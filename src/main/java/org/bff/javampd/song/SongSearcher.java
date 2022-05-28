package org.bff.javampd.song;

import java.util.Collection;

/**
 * Provides search and list functionality for {@link MPDSong}s
 *
 * @author Bill
 */
public interface SongSearcher {
  /** Defines the scope of items such as find, search. */
  enum ScopeType {
    ALBUM("album"),
    ALBUM_ARTIST("albumartist"),
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
    private final String type;

    ScopeType(String type) {
      this.type = type;
    }

    public String getType() {
      return type;
    }
  }

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching all tags.
   * Please note this returns a partial match of a title. To find an exact match use {@link
   * #findAny(String)}.
   *
   * @param criteria the search criteria
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
   */
  Collection<MPDSong> searchAny(String criteria);

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching the provided
   * scope type. Please note this returns a partial match of a title. To find an exact match use
   * {@link #find(ScopeType, String)}.
   *
   * @param searchType the {@link ScopeType}
   * @param criteria the search criteria
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
   */
  Collection<MPDSong> search(ScopeType searchType, String criteria);

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching all provided
   * scope types. Please note this returns a partial match of a title. To find an exact match use
   * {@link #find(ScopeType, String)}.
   *
   * @param criteria the search {@link SearchCriteria}
   * @return a {@link java.util.Collection} of {@link org.bff.javampd.song.MPDSong}s
   */
  Collection<MPDSong> search(SearchCriteria... criteria);

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching all scope
   * types. Please note this only returns an exact match of artist. To find a partial match use
   * {@link #search(ScopeType, String)}.
   *
   * @param criteria the search criteria
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> findAny(String criteria);

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching the provided
   * scope type. Please note this only returns an exact match of artist. To find a partial match use
   * {@link #search(ScopeType, String)}.
   *
   * @param scopeType the {@link ScopeType}
   * @param criteria the search criteria
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> find(ScopeType scopeType, String criteria);

  /**
   * Returns a {@link java.util.Collection} of {@link MPDSong}s for searches matching all the
   * provided scope types. Please note this only returns an exact match of artist. To find a partial
   * match use {@link #search(ScopeType, String)}.
   *
   * @param criteria the search {@link SearchCriteria}
   * @return a {@link java.util.Collection} of {@link MPDSong}s
   */
  Collection<MPDSong> find(SearchCriteria... criteria);
}
