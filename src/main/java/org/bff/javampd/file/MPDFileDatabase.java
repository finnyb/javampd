package org.bff.javampd.file;

import com.google.inject.Inject;
import org.bff.javampd.MPDException;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.properties.DatabaseProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDFileDatabase represents a file database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.DatabaseManager#getFileDatabase()}
 * method from the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDFileDatabase implements FileDatabase {

    private DatabaseProperties databaseProperties;
    private CommandExecutor commandExecutor;

    @Inject
    public MPDFileDatabase(DatabaseProperties databaseProperties,
                           CommandExecutor commandExecutor) {
        this.databaseProperties = databaseProperties;
        this.commandExecutor = commandExecutor;
    }

    @Override
    public Collection<MPDFile> listRootDirectory() {
        return listDirectory("");
    }

    @Override
    public Collection<MPDFile> listDirectory(MPDFile directory) {
        if (directory.isDirectory()) {
            return listDirectory(directory.getPath());
        } else {
            throw new MPDException(directory.getName() + " is not a directory.");
        }
    }

    private Collection<MPDFile> listDirectory(String directory) {
        return listDirectoryInfo(directory);
    }

    private Collection<MPDFile> listDirectoryInfo(String directory) {
        List<MPDFile> returnList = new ArrayList<>();
        List<String> list =
                commandExecutor.sendCommand(databaseProperties.getListInfo(), directory);

        for (String s : list) {

            if (s.startsWith(TagLister.ListInfoType.FILE.getPrefix())
                    || s.startsWith(TagLister.ListInfoType.DIRECTORY.getPrefix())) {
                MPDFile f = new MPDFile();

                String name = s;
                if (s.startsWith(TagLister.ListInfoType.FILE.getPrefix())) {
                    f.setDirectory(false);
                    name = name.substring(TagLister.ListInfoType.FILE.getPrefix().length()).trim();
                } else {
                    f.setDirectory(true);
                    name = name.substring(TagLister.ListInfoType.DIRECTORY.getPrefix().length()).trim();
                }

                f.setName(name);
                f.setPath(name);
                returnList.add(f);
            }
        }
        return returnList;
    }
}
