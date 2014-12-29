package org.bff.javampd.song;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.server.MPDResponseException;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

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
    public MPDSongSearcher(SearchProperties searchProperties,
                           CommandExecutor commandExecutor,
                           SongConverter songConverter) {
        this.searchProperties = searchProperties;
        this.commandExecutor = commandExecutor;
        this.songConverter = songConverter;
    }

    @Override
    public Collection<MPDSong> search(ScopeType searchType, String criteria) throws MPDDatabaseException {
        return search(generateParams(searchType, criteria));
    }

    @Override
    public Collection<MPDSong> search(ScopeType searchType, String criteria, int start, int end) throws MPDDatabaseException {
        return search(
                addWindowedParams(
                        generateParams(searchType, criteria),
                        start,
                        end));
    }

    private Collection<MPDSong> search(String[] params) throws MPDDatabaseException {
        List<String> titleList;

        try {
            titleList = commandExecutor.sendCommand(searchProperties.getSearch(), params);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.convertResponseToSong(titleList);

    }

    @Override
    public Collection<MPDSong> find(ScopeType scopeType, String criteria) throws MPDDatabaseException {
        return find(generateParams(scopeType, criteria));
    }

    @Override
    public Collection<MPDSong> find(ScopeType scopeType, String criteria, int start, int end) throws MPDDatabaseException {
        return find(addWindowedParams(
                generateParams(scopeType, criteria),
                start,
                end));
    }

    private Collection<MPDSong> find(String[] params) throws MPDDatabaseException {
        List<String> titleList;

        try {
            titleList = commandExecutor.sendCommand(searchProperties.getFind(), params);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.convertResponseToSong(titleList);
    }

    private String[] generateParams(ScopeType scopeType,
                                    String criteria) {
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

    private String[] addWindowedParams(String[] params,
                                       int start,
                                       int end) {
        String[] paramList = Arrays.copyOf(params, params.length + 2);
        paramList[params.length] = searchProperties.getWindow();
        paramList[params.length + 1] = start + "." + end;

        return paramList;
    }
}
