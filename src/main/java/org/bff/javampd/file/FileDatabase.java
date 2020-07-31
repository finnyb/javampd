package org.bff.javampd.file;

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
   */
  Collection<MPDFile> listRootDirectory();

  /**
   * Lists all {@link org.bff.javampd.file.MPDFile}s for the given directory of the file system.
   *
   * @param directory the directory to list
   * @return a {@code Collection} of {@link org.bff.javampd.file.MPDFile}
   */
  Collection<MPDFile> listDirectory(MPDFile directory);
}
