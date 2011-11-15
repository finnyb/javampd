/*
 * TrackPositionChangeEvent.java
 *
 * Created on October 10, 2005, 8:26 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

/**
 * Represents a change in the position of a playing song.
 *
 * @author Bill
 * @version 1.0
 */
public class TrackPositionChangeEvent extends java.util.EventObject {
    private long newElapsedTime = 0;

    /**
     * Creates a new instance of TrackPositionEvent.
     *
     * @param source  the object on which the Event initially occurred
     * @param newTime the new elapsed time of the song
     */
    public TrackPositionChangeEvent(Object source, long newTime) {
        super(source);
        this.newElapsedTime = newTime;
    }

    /**
     * Returns the elapsed time of the playing song.
     *
     * @return the new elapsed time
     */
    public long getElapsedTime() {
        return newElapsedTime;
    }
}