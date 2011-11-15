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

/**
 * An event used to identify an administrative action.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDChangeEvent
        extends java.util.EventObject {
    private int id;
    private String msg;

    /**
     * administrator killed the connection
     */
    public static final int MPD_KILLED = 0;
    /**
     * administrator refreshed the database
     */
    public static final int MPD_REFRESHED = 1;

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public MPDChangeEvent(Object source, int id) {
        this(source, id, null);
    }

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public MPDChangeEvent(Object source, int id, String msg) {
        super(source);
        this.id = id;
        this.msg = msg;
    }

    /**
     * Returns specific id of the event that occurred.  The ids are public static
     * fields in the class.
     *
     * @return the specific id
     */
    public int getId() {
        return (id);
    }

    /**
     * Returns the message attached to this event.  If there is no message null
     * is returned.
     *
     * @return the optional message
     */
    public String getMsg() {
        return msg;
    }
}
