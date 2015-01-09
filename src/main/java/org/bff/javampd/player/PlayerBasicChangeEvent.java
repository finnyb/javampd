package org.bff.javampd.player;

/**
 * Represents a change in the status of a music player.
 *
 * @author Bill
 */
public class PlayerBasicChangeEvent extends java.util.EventObject {
    private Status status;
    private String msg;

    public enum Status {
        PLAYER_STOPPED,
        PLAYER_STARTED,
        PLAYER_PAUSED,
        PLAYER_UNPAUSED,
        PLAYER_BITRATE_CHANGE
    }

    /**
     * Creates a new instance of PlayerBasicChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param status the {@link Status}
     */
    public PlayerBasicChangeEvent(Object source, Status status) {
        this(source, status, null);
    }

    /**
     * Creates a new instance of PlayerBasicChangeEvent
     *
     * @param source the object on which the Event initially occurred
     * @param status the {@link Status}
     * @param msg    an optional message
     */
    public PlayerBasicChangeEvent(Object source, Status status, String msg) {
        super(source);
        this.status = status;
        this.msg = msg;
    }

    /**
     * Returns the {@link Status} that occurred.
     *
     * @return the {@link Status}
     */
    public Status getStatus() {
        return this.status;
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
