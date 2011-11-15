/*
 * MPDCommand.java
 *
 * Created on October 5, 2005, 7:52 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package org.bff.javampd;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * MPDCommand represents a command along with optional command paramaters to be
 * sent to a MPD server.
 *
 * @author Bill
 * @version 1.0
 */
public class MPDCommand {
    private String command;
    private List<String> params;

    /**
     * Constructor for MPD command for a command requiring no parameters.
     *
     * @param command the parameterless command to send.
     */
    public MPDCommand(String command) {
        this(command, new String[]{null});
    }

    /**
     * Constructor for MPD command for a command requiring a single parameter.
     *
     * @param command the command to send
     * @param param   the parameter for the command
     */
    public MPDCommand(String command, String param) {
        this(command, new String[]{param});

    }

    /**
     * Constructor for MPD command for a command requiring more than 1 paramaeter.
     *
     * @param command the command to send
     * @param params  the parameters to send
     */
    public MPDCommand(String command, String[] params) {
        this.command = command;
        this.params = new ArrayList<String>();
        Collections.addAll(this.params, params);
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
