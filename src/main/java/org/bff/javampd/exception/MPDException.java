/*
 * MPDException.java
 *
 * Created on December 28, 2005, 10:29 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.bff.javampd.exception;

/**
 * Abstract base class for MPD Exceptions.
 *
 * @author Bill Findeisen
 * @version 1.0
 */
public abstract class MPDException extends Exception {

    /**
     * Constructor.
     */
    public MPDException() {
        super();
    }

    /**
     * Class constructor specifying the message.
     *
     * @param message the exception message
     */
    public MPDException(String message) {
        super(message);
    }

    /**
     * Class constructor specifying the cause.
     *
     * @param cause the cause of this exception
     */
    public MPDException(Throwable cause) {
        super(cause);
    }

    /**
     * Class constructor specifying the message and cause.
     *
     * @param message the exception message
     * @param cause   the cause of this exception
     */
    public MPDException(String message, Throwable cause) {
        super(message, cause);
    }
}
