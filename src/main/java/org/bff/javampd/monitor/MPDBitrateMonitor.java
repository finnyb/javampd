package org.bff.javampd.monitor;

import com.google.inject.Singleton;
import org.bff.javampd.Status;
import org.bff.javampd.events.PlayerBasicChangeEvent;
import org.bff.javampd.exception.MPDException;


@Singleton
public class MPDBitrateMonitor extends MPDPlayerMonitor implements BitrateMonitor {
    private int oldBitrate;
    private int newBitrate;

    @Override
    public void processResponseStatus(String line) {
        if (Status.lookupStatus(line) == Status.BITRATE) {
            newBitrate =
                    Integer.parseInt(line.substring(Status.BITRATE.getStatusPrefix().length()).trim());
        }
    }

    @Override
    public void checkStatus() throws MPDException {
        if (oldBitrate != newBitrate) {
            firePlayerChangeEvent(PlayerBasicChangeEvent.Status.PLAYER_BITRATE_CHANGE);
            oldBitrate = newBitrate;
        }
    }
}
