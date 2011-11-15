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
 * @author Bill Findeisen
 */
public class PlaylistChangeEvent extends EventObject {

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
     * a specific song was selected
     */
    public static final int SONG_SELECTED = 3;
    /**
     * a playlist was appended to this playlist
     */
    public static final int PLAYLIST_ADDED = 4;
    /**
     * the playlist version was changed
     */
    public static final int PLAYLIST_CHANGED = 5;
    /**
     * the playlist was deleted
     */
    public static final int PLAYLIST_DELETED = 6;
    /**
     * the playlist was loaded
     */
    public static final int PLAYLIST_LOADED = 7;
    /**
     * the playlist was changed
     */
    public static final int PLAYLIST_SAVED = 8;
    /**
     * the playlist was cleared
     */
    public static final int PLAYLIST_CLEARED = 9;
    /*
     * an artist was added
     */
    public static final int ARTIST_ADDED = 10;
    /*
     * an album was added
     */
    public static final int ALBUM_ADDED = 11;
    /**
     * a genre was added
     */
    public static final int GENRE_ADDED = 12;
    /**
     * a year was added
     */
    public static final int YEAR_ADDED = 13;
    /**
     * a year was added
     */
    public static final int FILE_ADDED = 14;
    /**
     * multiple songs added
     */
    public static final int MULTIPLE_SONGS_ADDED = 15;

    /**
     * Creates a new instance of PlayListChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     */
    public PlaylistChangeEvent(Object source, int id) {
        super(source);
        this.id = id;
    }

    /**
     * Creates a new instance of PlayListChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param id     the specific event that occurred
     * @param msg    an optional message
     */
    public PlaylistChangeEvent(Object source, int id, String msg) {
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
