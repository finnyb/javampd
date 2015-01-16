package org.bff.javampd.database;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.properties.DatabaseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Implementation of {@link TagLister} for MPD database
 *
 * @author Bill
 */
public class MPDTagLister implements TagLister {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDTagLister.class);

    private DatabaseProperties databaseProperties;
    private CommandExecutor commandExecutor;

    @Inject
    public MPDTagLister(DatabaseProperties databaseProperties,
                        CommandExecutor commandExecutor) {
        this.databaseProperties = databaseProperties;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Collection<String> listInfo(ListInfoType... types) {
        List<String> returnList = new ArrayList<>();
        List<String> list = commandExecutor.sendCommand(databaseProperties.getListInfo());

        for (String s : list) {
            for (ListInfoType type : types) {
                if (s.startsWith(type.getPrefix())) {
                    returnList.add(s.substring(type.getPrefix().length()).trim());
                }
            }
        }

        return returnList;
    }

    @Override
    public Collection<String> list(ListType listType) {
        return list(listType, null);
    }

    @Override
    public Collection<String> list(ListType listType, List<String> params) {
        String[] paramList = generateParamList(listType, params);

        List<String> responseList =
                commandExecutor.sendCommand(databaseProperties.getList(), paramList);

        List<String> retList = new ArrayList<>();
        for (String s : responseList) {
            try {
                retList.add(s.substring(s.split(":")[0].length() + 1).trim());
            } catch (ArrayIndexOutOfBoundsException e) {
                LOGGER.error("Problem with response array {}", s, e);
                retList.add("");
            }
        }
        return retList;
    }

    private String[] generateParamList(ListType listType, List<String> params) {
        String[] paramList;

        int i = 0;

        if (params != null) {
            paramList = new String[params.size() + 1];
        } else {
            paramList = new String[1];
        }

        paramList[i++] = listType.getType();

        if (params != null) {
            for (String s : params) {
                paramList[i++] = s;
            }
        }

        return paramList;
    }
}
