package org.bff.javampd.admin;

import org.bff.javampd.server.MPDResponseException;

/**
 * Represents an error with the MPD administration.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDAdminException extends MPDResponseException {

    /**
     * Constructor.
     */
    public MPDAdminException() {
        super();
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     * @param cause   the cause of the exception
     */
    public MPDAdminException(String message, String command, Throwable cause) {
        super(message, command, cause);
    }

    /**
     * Class constructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public MPDAdminException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public MPDAdminException(String message, Throwable cause) {
        super(message, cause);
    }
}
