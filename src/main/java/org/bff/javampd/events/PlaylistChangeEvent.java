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
 * Represents a change in the status of a MPD playlist.
 *
 * @author Bill
 */
public class PlaylistChangeEvent extends EventObject {

    private Event event;
    private String msg;

    public enum Event {
        SONG_ADDED,
        SONG_DELETED,
        SONG_SELECTED,
        PLAYLIST_ADDED,
        PLAYLIST_CHANGED,
        PLAYLIST_DELETED,
        PLAYLIST_LOADED,
        PLAYLIST_SAVED,
        PLAYLIST_CLEARED,
        ARTIST_ADDED,
        ALBUM_ADDED,
        GENRE_ADDED,
        YEAR_ADDED,
        FILE_ADDED,
        MULTIPLE_SONGS_ADDED
    }

    /**
     * Creates a new instance of PlayListChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the specific {@link Event} that occurred
     */
    public PlaylistChangeEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }

    /**
     * Creates a new instance of PlayListChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param event  the specific {@link Event} that occurred
     * @param msg    an optional message
     */
    public PlaylistChangeEvent(Object source, Event event, String msg) {
        super(source);
        this.event = event;
        this.msg = msg;
    }

    /**
     * Returns the {@link Event} that occurred.
     *
     * @return the specific id
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
        return msg;
    }
}
