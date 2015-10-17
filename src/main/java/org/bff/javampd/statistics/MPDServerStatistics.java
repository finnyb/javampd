package org.bff.javampd.statistics;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

/**
 * @author bill
 */
public class MPDServerStatistics implements ServerStatistics {
    private static final Logger LOGGER = LoggerFactory.getLogger(MPDServerStatistics.class);

    private ServerProperties serverProperties;
    private CommandExecutor commandExecutor;

    @Inject
    public MPDServerStatistics(ServerProperties serverProperties,
                               CommandExecutor commandExecutor) {
        this.serverProperties = serverProperties;
        this.commandExecutor = commandExecutor;
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
        List<String> respList = sendMPDCommand(serverProperties.getStats());

        for (String line : respList) {
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

    private List<String> sendMPDCommand(String stats) {
        return commandExecutor.sendCommand(stats);
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
}
