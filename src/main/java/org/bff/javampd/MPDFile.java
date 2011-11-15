/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.bff.javampd;

/**
 * Represents a file within the mpd songs directory.
 *
 * @author Bill
 */
public class MPDFile {
    private String name;
    private boolean directory;
    private String path;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        if (name.contains("/")) {
            name = name.replaceAll("^.*/", "");
        }
        this.name = name;
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
        return getName();
    }
}
