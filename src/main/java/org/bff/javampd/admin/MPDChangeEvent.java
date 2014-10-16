/*
 * MusicPlayerStatusChangedEvent.java
 *
 * Created on June 22, 2005, 12:32 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.admin;

/**
 * An event used to identify an administrative action.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDChangeEvent
        extends java.util.EventObject {
    private Event event;
    private String msg;

    public enum Event {
        MPD_KILLED,
        MPD_REFRESHED
    }

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the specific {@link Event} that occurred
     */
    public MPDChangeEvent(Object source, Event event) {
        this(source, event, null);
    }

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the specific {@link Event} that occurred
     * @param msg    an optional message
     */
    public MPDChangeEvent(Object source, Event event, String msg) {
        super(source);
        this.event = event;
        this.msg = msg;
    }

    /**
     * Returns the specific {@link Event} that occurred.
     *
     * @return the specific {@link Event}
     */
    public Event getEvent() {
        return this.event;
    }

    /**
     * Returns the message attached to this event.  If there is no message null
     * is returned.
     *
     * @return the optional message
     */
    public String getMsg() {
        return this.msg;
    }
}
