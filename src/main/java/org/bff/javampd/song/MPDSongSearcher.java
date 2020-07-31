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
  private SearchProperties searchProperties;
  private CommandExecutor commandExecutor;
  private SongConverter songConverter;

  @Inject
  public MPDSongSearcher(
    SearchProperties searchProperties,
    CommandExecutor commandExecutor,
    SongConverter songConverter
  ) {
    this.searchProperties = searchProperties;
    this.commandExecutor = commandExecutor;
    this.songConverter = songConverter;
  }

  @Override
  public Collection<MPDSong> search(ScopeType searchType, String criteria) {
    return search(generateParams(searchType, criteria));
  }

  @Override
  public Collection<MPDSong> search(
    ScopeType searchType,
    String criteria,
    int start,
    int end
  ) {
    return search(
      addWindowedParams(generateParams(searchType, criteria), start, end)
    );
  }

  private Collection<MPDSong> search(String[] params) {
    return songConverter.convertResponseToSong(
      commandExecutor.sendCommand(searchProperties.getSearch(), params)
    );
  }

  @Override
  public Collection<MPDSong> find(ScopeType scopeType, String criteria) {
    return find(generateParams(scopeType, criteria));
  }

  @Override
  public Collection<MPDSong> find(
    ScopeType scopeType,
    String criteria,
    int start,
    int end
  ) {
    return find(
      addWindowedParams(generateParams(scopeType, criteria), start, end)
    );
  }

  private Collection<MPDSong> find(String[] params) {
    return songConverter.convertResponseToSong(
      commandExecutor.sendCommand(searchProperties.getFind(), params)
    );
  }

  private static String[] generateParams(ScopeType scopeType, String criteria) {
    String[] paramList;

    if (criteria != null) {
      paramList = new String[2];
      paramList[1] = criteria;
    } else {
      paramList = new String[1];
    }
    paramList[0] = scopeType.getType();

    return paramList;
  }

  private String[] addWindowedParams(String[] params, int start, int end) {
    String[] paramList = Arrays.copyOf(params, params.length + 2);
    paramList[params.length] = searchProperties.getWindow();
    paramList[params.length + 1] = start + "." + end;

    return paramList;
  }
}
