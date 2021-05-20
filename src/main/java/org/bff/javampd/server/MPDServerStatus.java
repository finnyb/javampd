package org.bff.javampd.server;

import com.google.inject.Inject;
import org.bff.javampd.Clock;
import org.bff.javampd.command.CommandExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.Integer.parseInt;

public class MPDServerStatus implements ServerStatus {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDServerStatus.class);

    private long expiryInterval = 5;
    private List<String> cachedResponse;
    private final Clock clock;
    private LocalDateTime responseDate;

    private final ServerProperties serverProperties;
    private final CommandExecutor commandExecutor;

    private enum TimeType {
        ELAPSED(0),
        TOTAL(1);

        private final int index;

        TimeType(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }
    }

    @Inject
    public MPDServerStatus(ServerProperties serverProperties,
                           CommandExecutor commandExecutor,
                           Clock clock) {
        this.serverProperties = serverProperties;
        this.commandExecutor = commandExecutor;
        this.clock = clock;
        responseDate = clock.min();
    }

    /**
     * Returns the current status of the requested status element.
     * See {@link Status} for a list of possible items returned
     * by getStatus.  If the status isn't part of the response
     * "" is returned.
     *
     * @param status the status desired
     * @return the desired status information
     */
    protected String getStatus(Status status) {
        LocalDateTime now = clock.now();
        if (now.minusSeconds(expiryInterval).isAfter(responseDate)) {
            responseDate = now;
            cachedResponse = commandExecutor.sendCommand(serverProperties.getStatus());
        }

        for (String line : cachedResponse) {
            if (line.startsWith(status.getStatusPrefix())) {
                return line.substring(status.getStatusPrefix().length()).trim();
            }
        }
        LOGGER.warn("Response did not contain status {}", status.getStatusPrefix());
        return "";
    }

    @Override
    public Collection<String> getStatus() {
        return commandExecutor.sendCommand(serverProperties.getStatus());
    }

    @Override
    public int getPlaylistVersion() {
        String version = getStatus(Status.PLAYLIST);
        try {
            return parseInt("".equals(version) ? "0" : version);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format playlist version response {}", version, nfe);
            return 0;
        }
    }

    @Override
    public String getState() {
        return getStatus(Status.STATE);
    }

    @Override
    public int getXFade() {
        String xFade = getStatus(Status.XFADE);
        try {
            return parseInt("".equals(xFade) ? "0" : xFade);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format xfade response {}", xFade, nfe);
            return 0;
        }
    }

    @Override
    public String getAudio() {
        return getStatus(Status.AUDIO);
    }

    @Override
    public boolean isError() {
        return !"".equals(getStatus(Status.ERROR));
    }

    @Override
    public String getError() {
        return getStatus(Status.ERROR);
    }

    @Override
    public long getElapsedTime() {
        return lookupTime(TimeType.ELAPSED);
    }

    @Override
    public long getTotalTime() {
        return lookupTime(TimeType.TOTAL);
    }

    @Override
    public int getBitrate() {
        String bitrate = getStatus(Status.BITRATE);
        try {
            return parseInt("".equals(bitrate) ? "0" : bitrate);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format bitrate response {}", bitrate, nfe);
            return 0;
        }
    }

    @Override
    public int getVolume() {
        String volume = getStatus(Status.VOLUME);
        try {
            return parseInt("".equals(volume) ? "0" : volume);
        } catch (NumberFormatException nfe) {
            LOGGER.error("Could not format volume response {}", volume, nfe);
            return 0;
        }
    }

    @Override
    public boolean isRepeat() {
        return "1".equals(getStatus(Status.REPEAT));
    }

    @Override
    public boolean isRandom() {
        return "1".equals(getStatus(Status.RANDOM));
    }

    @Override
    public boolean isDatabaseUpdating() {
        return !"".equals(getStatus(Status.UPDATINGDB));
    }

    @Override
    public boolean isConsume() {
        return "1".equals(getStatus(Status.CONSUME));
    }

    @Override
    public boolean isSingle() {
        return "1".equals(getStatus(Status.SINGLE));
    }

    @Override
    public Optional<Integer> playlistSongNumber() {
        var songNumber = getStatus(Status.CURRENTSONG);

        return "".equals(songNumber) ? Optional.empty() : Optional.of(parseInt(songNumber));
    }

    @Override
    public Optional<String> playlistSongId() {
        var id = getStatus(Status.CURRENTSONGID);

        return "".equals(id) ? Optional.empty() : Optional.ofNullable(id);
    }

    @Override
    public Optional<Integer> playlistNextSongNumber() {
        var songNumber = getStatus(Status.NEXT_SONG);

        return "".equals(songNumber) ? Optional.empty() : Optional.of(parseInt(songNumber));
    }

    @Override
    public Optional<String> playlistNextSongId() {
        var id = getStatus(Status.NEXT_SONG_ID);

        return "".equals(id) ? Optional.empty() : Optional.ofNullable(id);
    }

    @Override
    public Optional<Integer> durationCurrentSong() {
        var duration = getStatus(Status.DURATION);

        return "".equals(duration) ? Optional.empty() : Optional.of(parseInt(duration));
    }

    @Override
    public Optional<Integer> elapsedCurrentSong() {
        var elapsed = getStatus(Status.ELAPSED);

        return "".equals(elapsed) ? Optional.empty() : Optional.of(parseInt(elapsed));
    }

    @Override
    public Optional<Integer> getMixRampDb() {
        var db = getStatus(Status.MIX_RAMP_DB);

        return "".equals(db) ? Optional.empty() : Optional.of((int) Float.parseFloat(db));
    }

    @Override
    public Optional<Integer> getMixRampDelay() {
        var delay = getStatus(Status.MIX_RAMP_DELAY);

        return "".equals(delay) ? Optional.empty() : Optional.of(parseInt(delay));
    }

    @Override
    public void setExpiryInterval(long seconds) {
        expiryInterval = seconds;
    }

    @Override
    public void forceUpdate() {
        responseDate = clock.min();
    }

    private long lookupTime(TimeType type) {
        String time = getStatus(Status.TIME);

        if ("".equals(time) || !time.contains(":")) {
            return 0;
        } else {
            try {
                return parseInt(time.trim().split(":")[type.getIndex()]);
            } catch (NumberFormatException nfe) {
                LOGGER.error("Could not format time {}", time, nfe);
                return 0;
            }
        }
    }
}
