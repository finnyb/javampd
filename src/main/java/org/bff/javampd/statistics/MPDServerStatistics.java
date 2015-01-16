package org.bff.javampd.statistics;

import com.google.inject.Inject;
import org.bff.javampd.command.CommandExecutor;
import org.bff.javampd.server.ServerProperties;

import java.util.List;

/**
 * @author bill
 */
public class MPDServerStatistics implements ServerStatistics {

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
    private String getStat(StatList stat) {
        List<String> respList = sendMPDCommand(serverProperties.getStats());

        for (String line : respList) {
            if (line.startsWith(stat.getStatPrefix())) {
                return line.substring(stat.getStatPrefix().length()).trim();
            }
        }
        return null;
    }

    private List<String> sendMPDCommand(String stats) {
        return commandExecutor.sendCommand(stats);
    }

    @Override
    public long getPlaytime() {
        return Long.parseLong(getStat(StatList.PLAYTIME));
    }

    @Override
    public long getUptime() {
        return Long.parseLong(getStat(StatList.UPTIME));
    }

    @Override
    public int getAlbumCount() {
        return Integer.parseInt(getStat(StatList.ALBUMS));
    }

    @Override
    public int getArtistCount() {
        return Integer.parseInt(getStat(StatList.ARTISTS));
    }

    @Override
    public int getSongCount() {
        return Integer.parseInt(getStat(StatList.SONGS));
    }

    @Override
    public long getDatabasePlaytime() {
        return Integer.parseInt(getStat(StatList.DBPLAYTIME));
    }

    @Override
    public long getLastUpdateTime() {
        return Long.parseLong(getStat(StatList.DBUPDATE));
    }
}
