package org.bff.javampd.server;

import com.google.inject.Singleton;

/**
 * @author bill
 */
@Singleton
public class ServerProperties extends MPDProperties {

    private enum Command {
        SERVERENCODING("MPD_SERVER_ENCODING"),
        CLEARERROR("MPD_CMD_CLEAR_ERROR"),
        CLOSE("MPD_CMD_CLOSE"),
        KILL("MPD_CMD_KILL"),
        STATUS("MPD_CMD_STATUS"),
        STATS("MPD_CMD_STATISTICS"),
        STARTBULK("MPD_CMD_START_BULK"),
        ENDBULK("MPD_CMD_END_BULK"),
        PASSWORD("MPD_CMD_PASSWORD"),
        PING("MPD_CMD_PING");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getClearError() {
        return getResponseCommand(Command.CLEARERROR);
    }

    public String getKill() {
        return getResponseCommand(Command.KILL);
    }

    public String getStatus() {
        return getResponseCommand(Command.STATUS);
    }

    public String getStats() {
        return getResponseCommand(Command.STATS);
    }

    public String getPing() {
        return getResponseCommand(Command.PING);
    }

    public String getPassword() {
        return getResponseCommand(Command.PASSWORD);
    }

    public String getClose() {
        return getResponseCommand(Command.CLOSE);
    }

    public String getStartBulk() {
        return getResponseCommand(Command.STARTBULK);
    }

    public String getEndBulk() {
        return getResponseCommand(Command.ENDBULK);
    }

    private String getResponseCommand(Command command) {
        return getPropertyString(command.getKey());
    }

    public String getEncoding() {
        return getResponseCommand(Command.SERVERENCODING);
    }
}
