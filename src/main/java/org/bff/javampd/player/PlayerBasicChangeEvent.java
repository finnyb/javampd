package org.bff.javampd.player;

/**
 * Represents a change in the status of a music player.
 *
 * @author Bill
 */
public class PlayerBasicChangeEvent extends java.util.EventObject {
    private Status status;

    public enum Status {
        PLAYER_STOPPED,
        PLAYER_STARTED,
        PLAYER_PAUSED,
        PLAYER_UNPAUSED
    }

    /**
     * Creates a new instance of PlayerBasicChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param status the {@link Status}
     */
    public PlayerBasicChangeEvent(Object source, Status status) {
        super(source);
        this.status = status;
    }

    /**
     * Returns the {@link Status} that occurred.
     *
     * @return the {@link Status}
     */
    public Status getStatus() {
        return this.status;
    }
}
