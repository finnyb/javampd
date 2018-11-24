package org.bff.javampd.statistics;

import com.google.inject.Inject;
import org.bff.javampd.Clock;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import org.joda.time.LocalDateTime;

/**
 * @author bill
 */
public class MPDServerStatistics implements ServerStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDServerStatistics.class);

    private long expiryInterval = 60;
    private List<String> cachedResponse;
    private Clock clock;
    private LocalDateTime responseDate;
    private ServerProperties serverProperties;
    private CommandExecutor commandExecutor;

    @Inject
    public MPDServerStatistics(ServerProperties serverProperties,
                               CommandExecutor commandExecutor,
                               Clock clock) {
        this.serverProperties = serverProperties;
        this.commandExecutor = commandExecutor;
        this.clock = clock;
        this.responseDate = clock.min();
    }

    /**
     * Returns the statistics of the requested statistics element.
     * See <code>StatList</code> for a list of possible items returned
     * by getServerStat.
     *
     * @param stat the statistic desired
     * @return the requested statistic
     */
    private Number getStat(StatList stat) {
        LocalDateTime now = this.clock.now();
        if (now.minusSeconds((int)this.expiryInterval).isAfter(this.responseDate)) {
            this.responseDate = now;
            this.cachedResponse = commandExecutor.sendCommand(serverProperties.getStats());
        }

        for (String line : cachedResponse) {
            if (line.startsWith(stat.getStatPrefix())) {
                try {
                    return (NumberFormat.getInstance()).parse(line.substring(stat.getStatPrefix().length()).trim());
                } catch (ParseException e) {
                    LOGGER.warn("Could not parse server statistic", e);
                    return 0;
                }
            }
        }

        return 0;
    }

    @Override
    public long getPlaytime() {
        return getStat(StatList.PLAYTIME).longValue();
    }

    @Override
    public long getUptime() {
        return getStat(StatList.UPTIME).longValue();
    }

    @Override
    public int getAlbumCount() {
        return getStat(StatList.ALBUMS).intValue();
    }

    @Override
    public int getArtistCount() {
        return getStat(StatList.ARTISTS).intValue();
    }

    @Override
    public int getSongCount() {
        return getStat(StatList.SONGS).intValue();
    }

    @Override
    public long getDatabasePlaytime() {
        return getStat(StatList.DBPLAYTIME).longValue();
    }

    @Override
    public long getLastUpdateTime() {
        return getStat(StatList.DBUPDATE).longValue();
    }

    @Override
    public void setExpiryInterval(long seconds) {
        this.expiryInterval = seconds;
    }

    @Override
    public void forceUpdate() {
        this.responseDate = clock.min();
    }
}
