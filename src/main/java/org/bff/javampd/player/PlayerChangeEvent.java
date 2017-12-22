package org.bff.javampd.player;

/**
 * Represents a change in the status of a MPD music player.
 *
 * @author Bill
 */
public class PlayerChangeEvent extends java.util.EventObject {
    private Event event;

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
     * @param event  the {@link Event}
     */
    public PlayerChangeEvent(Object source, Event event) {
        super(source);
        this.event = event;
    }

    /**
     * Returns the {@link Event} that occurred.
     *
     * @return the specific {@link Event}
     */
    public Event getEvent() {
        return this.event;
    }
}
