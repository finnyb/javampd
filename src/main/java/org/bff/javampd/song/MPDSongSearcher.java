package org.bff.javampd.song;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.server.MPDResponseException;

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
    public Collection<MPDSong> search(ScopeType searchType, String param) throws MPDDatabaseException {
        String[] paramList;

        if (param != null) {
            paramList = new String[2];
            paramList[1] = param;
        } else {
            paramList = new String[1];
        }

        paramList[0] = searchType.getType();
        List<String> titleList;

        try {
            titleList = commandExecutor.sendCommand(searchProperties.getSearch(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.convertResponseToSong(titleList);
    }

    @Override
    public Collection<MPDSong> find(ScopeType scopeType, String param) throws MPDDatabaseException {
        String[] paramList;

        if (param != null) {
            paramList = new String[2];
            paramList[1] = param;
        } else {
            paramList = new String[1];
        }
        paramList[0] = scopeType.getType();
        List<String> titleList;

        try {
            titleList = commandExecutor.sendCommand(searchProperties.getFind(), paramList);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.convertResponseToSong(titleList);
    }
}
