package org.bff.javampd.file;

import com.google.inject.Inject;
import org.bff.javampd.MPDException;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.DatabaseProperties;
import org.bff.javampd.database.TagLister;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.joda.time.LocalDateTime;
import org.joda.time.format.ISODateTimeFormat;

/**
 * MPDFileDatabase represents a file database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the
 * {@link org.bff.javampd.database.MusicDatabase#getFileDatabase()}
 * method from the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDFileDatabase implements FileDatabase {

    private DatabaseProperties databaseProperties;
    private CommandExecutor commandExecutor;

    private static final String PREFIX_FILE = TagLister.ListInfoType.FILE.getPrefix();
    private static final String PREFIX_DIRECTORY = TagLister.ListInfoType.DIRECTORY.getPrefix();
    private static final String PREFIX_LAST_MODIFIED = TagLister.ListInfoType.LAST_MODIFIED.getPrefix();

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
            throw new MPDException(directory.getPath() + " is not a directory.");
        }
    }

    private Collection<MPDFile> listDirectory(String directory) {
        return listDirectoryInfo(directory);
    }

    private Collection<MPDFile> listDirectoryInfo(String directory) {
        List<MPDFile> returnList = new ArrayList<>();
        List<String> commandResponse =
                commandExecutor.sendCommand(databaseProperties.getListInfo(), directory);

        Iterator<String> iterator = commandResponse.iterator();
        String line = null;
        while (iterator.hasNext()) {
            if (line == null ||
                    (!line.startsWith(PREFIX_FILE)
                            && !line.startsWith(PREFIX_DIRECTORY))) {
                line = iterator.next();
            }

            if (line.startsWith(PREFIX_FILE) ||
                    line.startsWith(PREFIX_DIRECTORY)) {
                MPDFile mpdFile = new MPDFile(line.startsWith(PREFIX_FILE) ?
                        line.substring(PREFIX_FILE.length()).trim() :
                        line.substring(PREFIX_DIRECTORY.length()).trim());

                if (line.startsWith(PREFIX_DIRECTORY)) {
                    mpdFile.setDirectory(true);
                }
                line = processFile(mpdFile, iterator);
                returnList.add(mpdFile);
            }
        }

        return returnList;
    }

    private static String processFile(MPDFile mpdFile, Iterator<String> iterator) {
        String line = iterator.next();
        while (!line.startsWith(PREFIX_FILE) &&
                !line.startsWith(PREFIX_DIRECTORY)) {

            if (line.startsWith(PREFIX_LAST_MODIFIED)) {
                mpdFile.setLastModified(processDate(line));
            }

            if (!iterator.hasNext()) {
                break;
            }
            line = iterator.next();
        }
        return line;
    }

    private static LocalDateTime processDate(String name) {
        return LocalDateTime.parse(
            name.substring(PREFIX_LAST_MODIFIED.length()).trim(),
            ISODateTimeFormat.dateTimeParser()
        );
    }
}
