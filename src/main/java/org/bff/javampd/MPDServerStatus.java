package org.bff.javampd;

import com.google.inject.Inject;
import org.bff.javampd.exception.MPDResponseException;
import org.bff.javampd.properties.ServerProperties;

import java.util.Collection;
import java.util.List;

/**
 * @author bill
 */
public class MPDServerStatus implements ServerStatus {
    @Inject
    private ServerProperties serverProperties;
    @Inject
    private CommandExecutor commandExecutor;

    /**
     * Returns the current status of the requested status element.
     * See <code>StatusList</code> for a list of possible items returned
     * by getStatus.
     *
     * @param status the status desired
     * @return the desired status information
     * @throws MPDResponseException if the MPD response generates an error
     */
    protected String getStatus(Status status) throws MPDResponseException {
        List<String> respList = commandExecutor.sendCommand(serverProperties.getStatus());

        for (String line : respList) {
            if (line.startsWith(status.getStatusPrefix())) {
                return line.substring(status.getStatusPrefix().length()).trim();
            }
        }
        return null;
    }

    @Override
    public Collection<String> getStatus() throws MPDResponseException {
        return commandExecutor.sendCommand(serverProperties.getStatus());
    }

    @Override
    public int getPlaylistVersion() throws MPDResponseException {
        return Integer.parseInt(getStatus(Status.PLAYLIST));
    }

    @Override
    public String getState() throws MPDResponseException {
        return getStatus(Status.STATE);
    }

    @Override
    public String getXFade() throws MPDResponseException {
        return getStatus(Status.XFADE);
    }

    @Override
    public String getAudio() throws MPDResponseException {
        return getStatus(Status.AUDIO);
    }

    @Override
    public long getTime() throws MPDResponseException {
        return lookupTime(TimeType.ELAPSED);
    }

    @Override
    public long getTotalTime() throws MPDResponseException {
        return lookupTime(TimeType.TOTAL);
    }

    @Override
    public int getBitrate() throws MPDResponseException {
        return Integer.parseInt(getStatus(Status.BITRATE));
    }

    @Override
    public int getVolume() throws MPDResponseException {
        return Integer.parseInt(getStatus(Status.VOLUME));
    }

    @Override
    public boolean isRepeat() throws MPDResponseException {
        return "1".equals(getStatus(Status.REPEAT));
    }

    @Override
    public boolean isRandom() throws MPDResponseException {
        return "1".equals(getStatus(Status.RANDOM));
    }

    private long lookupTime(TimeType type) throws MPDResponseException {
        String time = getStatus(Status.TIME);

        if (time == null || !time.contains(":")) {
            return 0;
        } else {
            return Integer.parseInt(time.trim().split(":")[type.getIndex()]);
        }
    }

    private enum TimeType {
        ELAPSED(0),
        TOTAL(1);

        private int index;

        TimeType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return this.index;
        }
    }
}
