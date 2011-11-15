/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.bff.javampd.objects;

/**
 * MPDGenre represents a genre
 *
 * @author Bill Findeisen
 */
public class MPDGenre extends MPDItem {

    /**
     * Creates a MPDGenre object
     *
     * @param name the name of the genre
     */
    public MPDGenre(String name) {
        setName(name);
    }
}
