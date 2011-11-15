/*
 * MusicPlayerChangedListener.java
 *
 * Created on June 22, 2005, 12:37 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

/**
 * The listener interface for receiving player events. The class that is
 * interested in processing a player event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addPlayerChangeListener</CODE> method. When the
 * player event occurs, that object's <CODE>playerChanged</CODE> method is
 * invoked.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public interface PlayerChangeListener {
    /**
     * Invoked when a player change event occurs.
     *
     * @param event the event fired
     */
    public void playerChanged(PlayerChangeEvent event);
}
