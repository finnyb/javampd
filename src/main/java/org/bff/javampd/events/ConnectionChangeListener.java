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
 * The listener interface for receiving connection change events. The class
 * that is interested in processing a connection event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addConnectionChangeListener</CODE> method. When the
 * connection event occurs, that object's <CODE>connectionChangeEventReceived</CODE>
 * method is invoked.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public interface ConnectionChangeListener {
    /**
     * Invoked when a connection change event occurs.
     *
     * @param event the event received
     */
    public void connectionChangeEventReceived(ConnectionChangeEvent event);
}
