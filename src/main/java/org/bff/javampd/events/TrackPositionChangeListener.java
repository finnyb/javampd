/*
 * TrackPositionChangeListener.java
 *
 * Created on October 10, 2005, 8:28 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

/**
 * The listener interface for receiving track position change events. The class
 * that is interested in processing a position event implements this interface,
 * and the object created with that class is registered with a component
 * using the component's <CODE>addTrackPositionChangeListener</CODE> method.
 * When the position event occurs, that object's <CODE>trackPositionChanged</CODE>
 * method is invoked.
 *
 * @author Bill
 * @version 1.0
 */
public interface TrackPositionChangeListener {
    /**
     * Invoked when a track position change occurs.
     *
     * @param event the event fired
     */
    void trackPositionChanged(TrackPositionChangeEvent event);
}
