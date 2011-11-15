/*
 * MPDResponseException.java
 *
 * Created on January 9, 2006, 3:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bff.javampd.exception;

/**
 * Represents an error with the MPD response.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public class MPDResponseException extends MPDException {
    private String command = null;

    /**
     * Constructor.
     */
    public MPDResponseException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public MPDResponseException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the message and command generating the
     * error.
     *
     * @param message the exception message
     * @param command the command generating the exception
     */
    public MPDResponseException(String message, String command) {
        super(message);
        this.command = command;
    }

    /**
     * Class constructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public MPDResponseException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public MPDResponseException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getCommand() {
        return (command);
    }
}
