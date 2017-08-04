package org.bff.javampd.admin;

import org.bff.javampd.server.MPDProperties;

public class AdminProperties extends MPDProperties {

    private enum Command {
        KILL("MPD_ADMIN_KILL"),
        REFRESH("MPD_ADMIN_REFRESH"),
        OUTPUTS("MPD_ADMIN_OUTPUTS"),
        OUTPUT_ENABLE("MPD_ADMIN_ENABLE_OUT"),
        OUTPUT_DISABLE("MPD_ADMIN_DISABLE_OUT");

        private final String key;

        Command(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public String getKill() {
        return getPropertyString(Command.KILL.getKey());
    }

    public String getRefresh() {
        return getPropertyString(Command.REFRESH.getKey());
    }

    public String getOutputs() {
        return getPropertyString(Command.OUTPUTS.getKey());
    }

    public String getOutputEnable() {
        return getPropertyString(Command.OUTPUT_ENABLE.getKey());
    }

    public String getOutputDisable() {
        return getPropertyString(Command.OUTPUT_DISABLE.getKey());
    }
}
