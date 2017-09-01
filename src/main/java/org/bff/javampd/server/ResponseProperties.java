package org.bff.javampd.server;

/**
 * Properties for {@link org.bff.javampd.server.MPD} responses
 *
 * @author bill
 */
public class ResponseProperties extends MPDProperties {

    private enum Command {
        OK("cmd.response.ok"),
        LIST_OK("cmd.response.list.ok"),
        ERR("cmd.response.err");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
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
