package org.bff.javampd;

/**
 * Base class for MPD Exceptions.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDException extends RuntimeException {
    private final String command;

    /**
     * Constructor.
     */
    public MPDException() {
        super();
        this.command = null;
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public MPDException(String message) {
        super(message);
        this.command = null;
    }

    /**
     * Class constructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public MPDException(Throwable cause) {
        super(cause);
        this.command = null;
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public MPDException(String message, Throwable cause) {
        super(message, cause);
        this.command = null;
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     */
    public MPDException(String message, String command) {
        super(message);
        this.command = command;
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     * @param cause   the cause of the exception
     */
    public MPDException(String message, String command, Throwable cause) {
        super(message, cause);
        this.command = command;
    }

    public String getCommand() {
        return command;
    }
}
