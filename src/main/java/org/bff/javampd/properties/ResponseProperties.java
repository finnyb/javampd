package org.bff.javampd.properties;

import com.google.inject.Singleton;

/**
 * Properties for {@link org.bff.javampd.MPD} responses
 *
 * @author bill
 */
@Singleton
public class ResponseProperties extends MPDProperties {

    private enum Command {
        OK("MPD_CMD_RESPONSE_OK"),
        LIST_OK("MPD_CMD_RESPONSE_LIST_OK"),
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

    public String getListOk() {
        return getPropertyString(Command.LIST_OK.getKey());
    }

    public String getError() {
        return getPropertyString(Command.ERR.getKey());
    }
}
