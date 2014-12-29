package org.bff.javampd.file;

import org.bff.javampd.database.MPDDatabaseException;

import java.util.Collection;

/**
 * Database for file related items
 *
 * @author bill
 */
public interface FileDatabase {

    /**
     * Lists all {@link org.bff.javampd.file.MPDFile}s for the root directory of the file system.
     *
     * @return a {@code Collection} of {@link org.bff.javampd.file.MPDFile}
     * @throws MPDDatabaseException if the MPD responded with an error
     */
    Collection<MPDFile> listRootDirectory() throws MPDDatabaseException;

    /**
     * Lists all {@link org.bff.javampd.file.MPDFile}s for the given directory of the file system.
     *
     * @param directory the directory to list
     * @return a {@code Collection} of {@link org.bff.javampd.file.MPDFile}
     * @throws MPDDatabaseException if the MPD responded with an error or the {@link org.bff.javampd.file.MPDFile}
     *                              is not a directory.
     */
    Collection<MPDFile> listDirectory(MPDFile directory) throws MPDDatabaseException;
}
