/*
 * MusicPlayerStatusChangedEvent.java
 *
 * Created on June 22, 2005, 12:32 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

import org.bff.javampd.MPDOutput;

/**
 * Represents a change in the outputs of a {@link MPDOutput}.
 *
 * @author Bill
 * @version 1.0
 */
public class OutputChangeEvent
        extends java.util.EventObject {

    private OUTPUT_EVENT event;

    public enum OUTPUT_EVENT {
        OUTPUT_ADDED,
        OUTPUT_DELETED,
        OUTPUT_CHANGED
    }

    /**
     * Creates a new instance of OutputChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the output event
     */
    public OutputChangeEvent(Object source, OUTPUT_EVENT event) {
        super(source);
        this.event = event;
    }


    /**
     * Returns the {@link OUTPUT_EVENT} for this event.
     *
     * @return the event
     */
    public OUTPUT_EVENT getEvent() {
        return event;
    }
}
