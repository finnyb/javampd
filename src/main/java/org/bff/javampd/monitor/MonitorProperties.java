package org.bff.javampd.monitor;

import org.bff.javampd.server.MPDProperties;

/**
 * Properties for the {@link org.bff.javampd.monitor.MPDStandAloneMonitor}.  All
 * properties are in seconds
 *
 * @author bill
 */
public class MonitorProperties extends MPDProperties {

    private enum Delay {
        OUTPUT("MPD_OUTPUT_DELAY"),
        CONNECTION("MPD_CONNECTION_DELAY"),
        PLAYLIST("MPD_PLAYLIST_DELAY"),
        ERROR("MPD_ERROR_DELAY"),
        PLAYER("MPD_PlAYER_DELAY"),
        TRACK("MPD_TRACK_DELAY"),
        MONITOR("MPD_MONITOR_DELAY"),
        EXCEPTION("MPD_EXCEPTION_DELAY");

        private final String key;

        Delay(String key) {
            this.key = key;
        }

        public String getKey() {
            return key;
        }
    }

    public int getOutputDelay() {
        return Integer.parseInt(getPropertyString(Delay.OUTPUT.getKey()));
    }

    public int getConnectionDelay() {
        return Integer.parseInt(getPropertyString(Delay.CONNECTION.getKey()));
    }

    public int getPlaylistDelay() {
        return Integer.parseInt(getPropertyString(Delay.PLAYLIST.getKey()));
    }

    public int getPlayerDelay() {
        return Integer.parseInt(getPropertyString(Delay.PLAYER.getKey()));
    }

    public int getErrorDelay() {
        return Integer.parseInt(getPropertyString(Delay.ERROR.getKey()));
    }

    public int getTrackDelay() {
        return Integer.parseInt(getPropertyString(Delay.TRACK.getKey()));
    }

    public int getMonitorDelay() {
        return Integer.parseInt(getPropertyString(Delay.MONITOR.getKey()));
    }

    public int getExceptionDelay() {
        return Integer.parseInt(getPropertyString(Delay.EXCEPTION.getKey()));
    }
}
