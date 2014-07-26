package org.bff.javampd.exception;

/**
 * Represents a security or permission issue when trying to execute commands
 *
 * @author bill
 * @since: 7/26/14 7:34 AM
 */
public class MPDSecurityException extends MPDResponseException {

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public MPDSecurityException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     */
    public MPDSecurityException(String message, String command) {
        super(message, command);
    }
}
