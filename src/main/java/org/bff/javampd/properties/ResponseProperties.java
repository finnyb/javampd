package org.bff.javampd.properties;

/**
 * Properties for {@link org.bff.javampd.MPD} responses
 *
 * @author bill
 * @since: 11/21/13 7:10 PM
 */
public class ResponseProperties extends MPDProperties {

    private enum Command {
        OK("MPD_CMD_RESPONSE_OK"),
        ERR("MPD_CMD_RESPONSE_ERR");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getResponseCommand(Command command) {
        return getPropertyString(command.getKey());
    }

    public String getOk() {
        return getPropertyString(Command.OK.getKey());
    }

    public String getError() {
        return getPropertyString(Command.ERR.getKey());
    }
}
