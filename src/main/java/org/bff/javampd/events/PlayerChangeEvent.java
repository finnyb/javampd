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
 * @author Bill Findeisen
 */
public class PlayerChangeEvent
        extends java.util.EventObject {
    private int id;
    private String msg;

    /**
     * the player was stopped
     */
    public static final int PLAYER_STOPPED = 0;
    /**
     * the player was started
     */
    public static final int PLAYER_STARTED = 1;
    /**
     * the player was paused
     */
    public static final int PLAYER_PAUSED = 2;
    /**
     * the player requesting the next song in the playlist
     */
    public static final int PLAYER_NEXT = 3;
    /**
     * the player requesting the previous song in the playlist
     */
    public static final int PLAYER_PREVIOUS = 4;
    /**
     * the volume was muted
     */
    public static final int PLAYER_MUTED = 5;
    /**
     * the volume was unmuted
     */
    public static final int PLAYER_UNMUTED = 6;
    /**
     * a specific song was requested by the player
     */
    public static final int PLAYER_SONG_SET = 9;
    /**
     * the player was taken off pause
     */
    public static final int PLAYER_UNPAUSED = 10;
    /**
     * the player is seeking within a song
     */
    public static final int PLAYER_SEEKING = 11;

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public PlayerChangeEvent(Object source, int id) {
        this(source, id, null);
    }

    /**
     * Creates a new instance of PlayerChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public PlayerChangeEvent(Object source, int id, String msg) {
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
