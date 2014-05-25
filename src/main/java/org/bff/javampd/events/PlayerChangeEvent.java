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
 * Represents a change in the status of a MPD music player.
 *
 * @author Bill
 */
public class PlayerChangeEvent extends java.util.EventObject {
	private static final long serialVersionUID = 1L;
	private Event event;
    private String msg;

    public enum Event {
        PLAYER_STOPPED,
        PLAYER_STARTED,
        PLAYER_PAUSED,
        PLAYER_NEXT,
        PLAYER_PREVIOUS,
        PLAYER_MUTED,
        PLAYER_UNMUTED,
        PLAYER_SONG_SET,
        PLAYER_UNPAUSED,
        PLAYER_SEEKING
    }

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the specific {@link Event} that occurred
     */
    public PlayerChangeEvent(Object source, Event event) {
        this(source, event, null);
    }

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the {@link Event}
     * @param msg    an optional message
     */
    public PlayerChangeEvent(Object source, Event event, String msg) {
        super(source);
        this.event = event;
        this.msg = msg;
    }

    /**
     * Returns the {@link Event} that occurred.
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
