package org.bff.javampd.file;

import org.joda.time.LocalDateTime;

/**
 * Represents a file within the mpd songs directory.
 *
 * @author Bill
 */
public class MPDFile {
    private boolean directory;
    private String path;
    private LocalDateTime lastModified;

    public MPDFile(String path) {
        this.path = path;
    }

    /**
     * @return the directory
     */
    public boolean isDirectory() {
        return directory;
    }

    /**
     * @param directory the directory to set
     */
    public void setDirectory(boolean directory) {
        this.directory = directory;
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public String toString() {
        return getPath();
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }
}
