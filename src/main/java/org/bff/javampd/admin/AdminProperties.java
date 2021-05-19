package org.bff.javampd.admin;

import org.bff.javampd.server.MPDProperties;

public class AdminProperties extends MPDProperties {

    private enum Command {
        KILL("admin.kill"),
        REFRESH("admin.refresh"),
        RESCAN("admin.rescan"),
        OUTPUTS("admin.outputs"),
        OUTPUT_ENABLE("admin.enable.out"),
        OUTPUT_DISABLE("admin.disable.out");

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

    public String getRescan() {
        return getPropertyString(Command.RESCAN.getKey());
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
