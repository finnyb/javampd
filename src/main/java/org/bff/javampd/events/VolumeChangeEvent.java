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
 * Represents a change in the volume of a MPD player.
 *
 * @author Bill
 * @version 1.0
 */
public class VolumeChangeEvent
        extends java.util.EventObject {
    private int volume;
    private String msg;

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param volume the new volume
     */
    public VolumeChangeEvent(Object source, int volume) {
        this(source, volume, null);
    }

    /**
     * Creates a new instance of MusicPlayerStatusChangedEvent
     *
     * @param source the object on which the Event initially occurred
     * @param volume the new volume
     * @param msg    an optional message
     */
    public VolumeChangeEvent(Object source, int volume, String msg) {
        super(source);
        this.volume = volume;
        this.msg = msg;
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

    /**
     * Returns the new volume level.
     *
     * @return the new volume
     */
    public int getVolume() {
        return volume;
    }
}
