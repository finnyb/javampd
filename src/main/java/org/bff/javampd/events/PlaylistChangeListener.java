/*
 * PlaylistChangeListener.java
 *
 * Created on October 10, 2005, 8:28 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

/**
 * The listener interface for receiving playlist events. The class that is
 * interested in processing a playlist event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addPlaylistChangeListener</CODE> method. When the
 * playlist event occurs, that object's <CODE>playlistChanged</CODE> method is
 * invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface PlaylistChangeListener {
    /**
     * Invoked when a playlist event occurs.
     *
     * @param event the event fired
     */
    public void playlistChanged(PlaylistChangeEvent event);
}
