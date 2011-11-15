/*
 * Utils.java
 *
 * Created on December 27, 2005, 7:58 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bff.javampd;

/**
 * Utils contains only public static utility methods.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class Utils {

    /**
     * Removes leading or trailing slashes from a string.
     *
     * @param path the string to remove leading or trailing slashes
     * @return the string without leading or trailing slashes
     */
    public static String removeSlashes(String path) {
        String retString = path;
        String slash = System.getProperty("file.separator");

        //if non-Unix slashes replace
        retString = retString.replace(slash, "/");

        retString = retString.replaceFirst("^/", "");
        retString = retString.replaceFirst("/$", "");

        return (retString);
    }
}
