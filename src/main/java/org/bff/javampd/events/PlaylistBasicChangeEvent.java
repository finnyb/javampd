/*
 * PlaylistChangeEvent.java
 *
 * Created on October 10, 2005, 8:25 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd.events;

import java.util.EventObject;

/**
 * Represents a change in the status of a MPD music playlist.
 *
 * @author Bill Findeisen
 */
public class PlaylistBasicChangeEvent extends EventObject {
    private int id;
    private String msg;

    /**
     * a song was added
     */
    public static final int SONG_ADDED = 1;
    /**
     * a song was deleted
     */
    public static final int SONG_DELETED = 2;
    /**
     * a song was changed
     */
    public static final int SONG_CHANGED = 3;
    /**
     * the playlist version was changed
     */
    public static final int PLAYLIST_CHANGED = 4;
    /**
     * the end of the playlist is reached and
     * the player has stopped
     */
    public static final int PLAYLIST_ENDED = 5;

    /**
     * Creates a new instance of PlayListBasicChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public PlaylistBasicChangeEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

    /**
     * Creates a new instance of PlayListBasicChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public PlaylistBasicChangeEvent(Object source, int id, String msg) {
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
