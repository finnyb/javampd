package org.bff.javampd.command;

import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * MPDCommand represents a command along with optional command parameters to be
 * sent to a MPD server.
 *
 * @author Bill
 * @version 1.0
 */
@EqualsAndHashCode
public class MPDCommand {
    private String command;
    private List<String> params;

    /**
     * Constructor for MPD command for a command requiring more than 1 parameter.
     *
     * @param command    the command to send
     * @param parameters the parameters to send
     */
    public MPDCommand(String command, String... parameters) {
        if (command == null) {
            throw new IllegalArgumentException("command can't be null");
        }

        this.command = command;
        this.params = new ArrayList<>();
        Collections.addAll(this.params, Arrays.copyOf(parameters, parameters.length));
    }

    /**
     * Returns the command od this object.
     *
     * @return the command
     */
    public String getCommand() {
        return command;
    }

    /**
     * Returns the parameter(s) of this command as a {@link List} of {@link String}s.
     * Returns null of there is no parameter for the command.
     *
     * @return the parameters for the command
     */
    public List<String> getParams() {
        return params;
    }
}
