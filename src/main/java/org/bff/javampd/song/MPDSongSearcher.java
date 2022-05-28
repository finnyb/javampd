package org.bff.javampd.song;

import com.google.inject.Inject;
import java.util.Arrays;
import java.util.Collection;
import org.bff.javampd.command.CommandExecutor;

/**
 * Implementation of {@link SongSearcher} for MPD
 *
 * @author Bill
 */
public class MPDSongSearcher implements SongSearcher {

  private final SearchProperties searchProperties;
  private final CommandExecutor commandExecutor;
  private final SongConverter songConverter;

  @Inject
  public MPDSongSearcher(
      SearchProperties searchProperties,
      CommandExecutor commandExecutor,
      SongConverter songConverter) {
    this.searchProperties = searchProperties;
    this.commandExecutor = commandExecutor;
    this.songConverter = songConverter;
  }

  @Override
  public Collection<MPDSong> searchAny(String criteria) {
    return search(new SearchCriteria(ScopeType.ANY, criteria));
  }

  @Override
  public Collection<MPDSong> search(ScopeType searchType, String criteria) {
    return search(new SearchCriteria(searchType, criteria));
  }

  @Override
  public Collection<MPDSong> search(SearchCriteria... criteria) {
    var command =
        String.join(
            " AND ",
            Arrays.stream(criteria)
                .map(c -> generateSearchParams(c.getSearchType(), c.getCriteria()))
                .toArray(String[]::new));

    return criteria.length == 1 ? search(command) : search("(" + command + ")");
  }

  @Override
  public Collection<MPDSong> findAny(String criteria) {
    return find(new SearchCriteria(ScopeType.ANY, criteria));
  }

  @Override
  public Collection<MPDSong> find(ScopeType scopeType, String criteria) {
    return find(new SearchCriteria(scopeType, criteria));
  }

  @Override
  public Collection<MPDSong> find(SearchCriteria... criteria) {
    var command =
        String.join(
            " AND ",
            Arrays.stream(criteria)
                .map(c -> generateFindParams(c.getSearchType(), c.getCriteria()))
                .toArray(String[]::new));

    return criteria.length == 1 ? find(command) : find("(" + command + ")");
  }

  private Collection<MPDSong> find(String params) {
    return songConverter.convertResponseToSongs(
        commandExecutor.sendCommand(searchProperties.getFind(), params));
  }

  private Collection<MPDSong> search(String params) {
    return songConverter.convertResponseToSongs(
        commandExecutor.sendCommand(searchProperties.getSearch(), params));
  }

  private static String generateFindParams(ScopeType scopeType, String criteria) {
    return String.format("(%s == '%s')", scopeType.getType(), escape(criteria));
  }

  private static String generateSearchParams(ScopeType scopeType, String criteria) {
    return String.format("(%s contains '%s')", scopeType.getType(), escape(criteria));
  }

  private static String escape(String s) {
    return s.replace("'", "\\\\'");
  }
}
