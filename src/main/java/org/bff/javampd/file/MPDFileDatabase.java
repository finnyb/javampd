package org.bff.javampd.file;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.database.MPDDatabaseException;
import org.bff.javampd.database.TagLister;
import org.bff.javampd.properties.DatabaseProperties;
import org.bff.javampd.server.MPDResponseException;
import org.bff.javampd.song.SongConverter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * MPDFileDatabase represents a file database controller to a {@link org.bff.javampd.server.MPD}.
 * To obtain an instance of the class you must use the {@link org.bff.javampd.server.MPD#getFileDatabase()}
 * method from the {@link org.bff.javampd.server.MPD} connection class.
 *
 * @author Bill
 */
public class MPDFileDatabase implements FileDatabase {

    private DatabaseProperties databaseProperties;
    private CommandExecutor commandExecutor;
    private SongConverter songConverter;

    @Inject
    public MPDFileDatabase(DatabaseProperties databaseProperties,
                           CommandExecutor commandExecutor,
                           SongConverter songConverter) {
        this.databaseProperties = databaseProperties;
        this.commandExecutor = commandExecutor;
        this.songConverter = songConverter;
    }

    @Override
    public Collection<MPDFile> listRootDirectory() throws MPDDatabaseException {
        return listDirectory("");
    }

    @Override
    public Collection<MPDFile> listDirectory(MPDFile directory) throws MPDDatabaseException {
        if (directory.isDirectory()) {
            return listDirectory(directory.getPath());
        } else {
            throw new MPDDatabaseException(directory.getName() + " is not a directory.");
        }
    }

    @Override
    public Collection<String> listAllFiles() throws MPDDatabaseException {
        try {
            return commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<String> listAllFiles(String path) throws MPDDatabaseException {
        try {
            return commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }
    }

    @Override
    public Collection<String> listAllSongFiles() throws MPDDatabaseException {
        List<String> fileList;

        try {
            fileList = commandExecutor.sendCommand(databaseProperties.getListAll());
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.getSongNameList(fileList);
    }

    @Override
    public Collection<String> listAllSongFiles(String path) throws MPDDatabaseException {
        List<String> fileList;
        try {
            fileList = commandExecutor.sendCommand(databaseProperties.getListAll(), path);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

        return songConverter.getSongNameList(fileList);
    }

    private Collection<MPDFile> listDirectory(String directory) throws MPDDatabaseException {
        return listDirectoryInfo(directory);
    }

    private Collection<MPDFile> listDirectoryInfo(String directory) throws MPDDatabaseException {
        List<MPDFile> returnList = new ArrayList<>();
        List<String> list;

        try {
            list = commandExecutor.sendCommand(databaseProperties.getListInfo(), directory);
        } catch (MPDResponseException re) {
            throw new MPDDatabaseException(re.getMessage(), re.getCommand(), re);
        } catch (Exception e) {
            throw new MPDDatabaseException(e);
        }

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
