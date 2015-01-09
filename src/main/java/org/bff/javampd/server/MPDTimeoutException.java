package org.bff.javampd.server;

/**
 * Represents an error with the MPD connection.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDTimeoutException extends MPDConnectionException {

    /**
     * Constructor.
     */
    public MPDTimeoutException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public MPDTimeoutException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public MPDTimeoutException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public MPDTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }
}
